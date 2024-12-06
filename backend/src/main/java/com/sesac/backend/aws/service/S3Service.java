package com.sesac.backend.aws.service;

import com.sesac.backend.aws.dto.FileDownloadResponse;
import com.sesac.backend.aws.dto.FileUploadResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final CloudWatchLogsClient cloudWatchLogsClient;

    @Value("${cloud.aws.cloudwatch.log-group}")
    private String logGroupName;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 업로드
    public FileUploadResponse uploadFile(MultipartFile file, String userName) {
        String originalFileName = file.getOriginalFilename();
        String savedFileName = createFileName(originalFileName);

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(savedFileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            cloudWatchLogsClient.putLogEvents(r -> r
                    .logGroupName(logGroupName)
                    .logStreamName("s3-file-upload")
                    .logEvents(
                            software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent.builder()
                                    .timestamp(Instant.now().toEpochMilli())
                                    .message(String.format("[%s] S3 파일 업로드 완료 - 원본 파일명: %s, 저장 파일명: %s, 크기: %d bytes",
                                            userName, originalFileName, savedFileName, file.getSize()))
                                    .build()
                    )
            );

            return FileUploadResponse.builder()
                    .originalFileName(originalFileName)
                    .savedFileName(savedFileName)
                    .fileUrl(getFileUrl(savedFileName))
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();

        } catch (IOException e) {
            String errorMessage = String.format("파일 업로드 실패: %s", originalFileName);
            log.error(errorMessage, e);
            logError("s3-file-upload-errors", userName, originalFileName, e.getMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }

    // 파일 다운로드
    public FileDownloadResponse downloadFile(String savedFileName, String userName) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(savedFileName)
                    .build();

            ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);
            String originalFileName = getOriginalName(savedFileName);

            cloudWatchLogsClient.putLogEvents(r -> r
                    .logGroupName(logGroupName)
                    .logStreamName("s3-file-download")
                    .logEvents(
                            software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent.builder()
                                    .timestamp(Instant.now().toEpochMilli())
                                    .message(String.format("[%s] S3 파일 다운로드 완료 - 저장 파일명: %s, 원본 파일명: %s, 크기: %d bytes",
                                            userName, savedFileName, originalFileName, response.response().contentLength()))
                                    .build()
                    )
            );

            return FileDownloadResponse.builder()
                    .fileContent(response.asByteArray())
                    .originalFileName(originalFileName)
                    .contentType(response.response().contentType())
                    .size(response.response().contentLength())
                    .build();

        } catch (S3Exception e) {
            String errorMessage = String.format("파일 다운로드 실패: %s", savedFileName);
            log.error(errorMessage, e);
            logError("s3-file-download-errors", userName, savedFileName, e.getMessage());
            throw new RuntimeException(errorMessage, e);
        }
    }

    // 파일 삭제 (boolean 반환으로 수정)
    public boolean deleteFile(String fileName, String userName) {
        try {
            // 파일 존재 여부 확인
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            try {
                s3Client.headObject(headObjectRequest);
            } catch (NoSuchKeyException e) {
                log.warn("[{}] 삭제할 파일이 존재하지 않음: {}", userName, fileName);
                return false;
            }

            // 파일 삭제 요청
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteRequest);

            // CloudWatch에 성공 로그 기록
            try {
                log.info("CloudWatch 로그 전송 시도 시작");

                // sequence token 조회
                DescribeLogStreamsRequest describeLogStreamsRequest = DescribeLogStreamsRequest.builder()
                        .logGroupName(logGroupName)
                        .logStreamNamePrefix("s3-file-deletion")
                        .build();

                log.info("로그 스트림 정보 조회 시도");
                var logStreams = cloudWatchLogsClient.describeLogStreams(describeLogStreamsRequest)
                        .logStreams();

                if (logStreams.isEmpty()) {
                    log.error("로그 스트림을 찾을 수 없음: s3-file-deletion");
                    return true;
                }

                String sequenceToken = logStreams.get(0).uploadSequenceToken();
                log.info("Sequence token 조회됨: {}", sequenceToken);

                // 로그 이벤트 전송
                var putLogsResponse = cloudWatchLogsClient.putLogEvents(r -> r
                        .logGroupName(logGroupName)
                        .logStreamName("s3-file-deletion")
                        .sequenceToken(sequenceToken)
                        .logEvents(
                                InputLogEvent.builder()
                                        .timestamp(Instant.now().toEpochMilli())
                                        .message(String.format("[%s] S3 파일 삭제 완료 - 파일명: %s", userName, fileName))
                                        .build()
                        )
                );

                log.info("CloudWatch 로그 전송 완료. 다음 sequence token: {}",
                        putLogsResponse.nextSequenceToken());
                return true;

            } catch (Exception e) {
                log.error("CloudWatch 로그 전송 실패: {}", e.getMessage(), e);
                return true; // S3 삭제는 성공했으므로 true 반환
            }

        } catch (S3Exception e) {
            String errorMessage = String.format("파일 삭제 실패: %s", fileName);
            log.error("[{}] {}: {}", userName, errorMessage, e.getMessage());
            logError("s3-file-deletion-errors", userName, fileName, e.getMessage());
            return false;
        }
    }

    // CloudWatch 에러 로그 기록
    private void logError(String streamName, String userName, String fileName, String errorMessage) {
        try {
            cloudWatchLogsClient.putLogEvents(r -> r
                    .logGroupName(logGroupName)
                    .logStreamName(streamName)
                    .logEvents(
                            software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent.builder()
                                    .timestamp(Instant.now().toEpochMilli())
                                    .message(String.format("[%s] S3 작업 실패 - 파일명: %s, 에러: %s",
                                            userName, fileName, errorMessage))
                                    .build()
                    )
            );
        } catch (Exception e) {
            log.error("CloudWatch 로그 전송 실패", e);
        }
    }

    // 파일명 생성
    private String createFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    // 원본 파일명 추출
    private String getOriginalName(String savedFileName) {
        return savedFileName.substring(savedFileName.lastIndexOf('_') + 1);
    }

    // 파일 URL 생성
    private String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, "ap-northeast-2", fileName);
    }
}