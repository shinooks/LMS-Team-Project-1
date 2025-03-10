package com.sesac.backend.aws.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CloudWatchConfig {

    private final CloudWatchLogsClient cloudWatchLogsClient;

    @Value("${cloud.aws.cloudwatch.log-group}")
    private String logGroupName;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final List<String> STREAM_NAMES = Arrays.asList(
            "s3-file-upload",
            "s3-file-upload-errors",
            "s3-file-download",
            "s3-file-download-errors",
            "s3-file-deletion",
            "s3-file-deletion-errors"
    );

    @PostConstruct
    public void initializeCloudWatch() {
        try {
            // 1. 로그 그룹 생성
            createLogGroupIfNotExists();

            // 2. 로그 스트림 생성
            STREAM_NAMES.forEach(this::createLogStreamIfNotExists);

            // 3. 로그 보존 기간 설정 (30일)
            setLogRetentionPolicy();

            // 4. 로그 내보내기 설정
            setupLogExport();

            // 5. 현재 로그 스트림 상태 출력
            displayCurrentLogStreams();

        } catch (Exception e) {
            log.error("CloudWatch 초기화 실패", e);
        }
    }

    private void createLogGroupIfNotExists() {
        try {
            CreateLogGroupRequest request = CreateLogGroupRequest.builder()
                    .logGroupName(logGroupName)
                    .build();
            cloudWatchLogsClient.createLogGroup(request);
            log.info("로그 그룹 생성 완료: {}", logGroupName);
        } catch (ResourceAlreadyExistsException e) {
            log.info("로그 그룹이 이미 존재함: {}", logGroupName);
        }
    }

    private void createLogStreamIfNotExists(String streamName) {
        try {
            CreateLogStreamRequest request = CreateLogStreamRequest.builder()
                    .logGroupName(logGroupName)
                    .logStreamName(streamName)
                    .build();
            cloudWatchLogsClient.createLogStream(request);

            // 초기 로그 이벤트 추가
            PutLogEventsRequest putLogEventsRequest = PutLogEventsRequest.builder()
                    .logGroupName(logGroupName)
                    .logStreamName(streamName)
                    .logEvents(Collections.singletonList(
                            InputLogEvent.builder()
                                    .timestamp(Instant.now().toEpochMilli())
                                    .message("로그 스트림 초기화 완료")
                                    .build()
                    ))
                    .build();

            cloudWatchLogsClient.putLogEvents(putLogEventsRequest);
            log.info("CloudWatch 로그 스트림 생성 및 초기화 완료: {}", streamName);
        } catch (ResourceAlreadyExistsException e) {
            log.info("CloudWatch 로그 스트림이 이미 존재함: {}", streamName);
        }
    }

    private void setLogRetentionPolicy() {
        try {
            PutRetentionPolicyRequest request = PutRetentionPolicyRequest.builder()
                    .logGroupName(logGroupName)
                    .retentionInDays(30)
                    .build();
            cloudWatchLogsClient.putRetentionPolicy(request);
            log.info("로그 보존 기간 설정 완료: 30일");
        } catch (Exception e) {
            log.error("로그 보존 기간 설정 실패", e);
        }
    }

    private void setupLogExport() {
        try {
            String taskName = "export-" + LocalDate.now() + "-" + System.currentTimeMillis();

            CreateExportTaskRequest request = CreateExportTaskRequest.builder()
                    .taskName(taskName)
                    .logGroupName(logGroupName)
                    .from(Instant.now().minus(Duration.ofDays(1)).toEpochMilli())
                    .to(Instant.now().toEpochMilli())
                    .destination(bucketName)
                    .destinationPrefix("cloudwatch-logs/" + LocalDate.now())
                    .build();

            var response = cloudWatchLogsClient.createExportTask(request);
            log.info("로그 내보내기 작업 생성 완료 - TaskId: {}", response.taskId());
        } catch (Exception e) {
            log.error("로그 내보내기 작업 생성 실패", e);
        }
    }

    private void displayCurrentLogStreams() {
        try {
            log.info("=== 현재 존재하는 모든 로그 스트림 목록 ===");
            DescribeLogStreamsRequest request = DescribeLogStreamsRequest.builder()
                    .logGroupName(logGroupName)
                    .build();

            cloudWatchLogsClient.describeLogStreams(request)
                    .logStreams()
                    .forEach(stream -> {
                        log.info("스트림 이름: {} (마지막 이벤트: {})",
                                stream.logStreamName(),
                                stream.lastEventTimestamp() != null
                                        ? Instant.ofEpochMilli(stream.lastEventTimestamp())
                                        : "없음");
                    });
            log.info("=====================================");
        } catch (Exception e) {
            log.error("로그 스트림 조회 실패", e);
        }
    }
}