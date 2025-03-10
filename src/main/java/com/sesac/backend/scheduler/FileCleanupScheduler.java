package com.sesac.backend.scheduler;

import com.sesac.backend.board.repository.PostFileRepository;
import com.sesac.backend.entity.PostFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// 파일 정리 스케줄러 삭제 표시된 파일들을 주기적으로 실제 삭제하는 배치 작업을 수행
@Slf4j
@Component
@RequiredArgsConstructor
public class FileCleanupScheduler {

    private final PostFileRepository postFileRepository;

    // 삭제된 파일 정리 매일 새벽 2시에 실행되며, 3일 이전에 삭제 표시된 파일들을 실제로 삭제

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupDeletedFiles() {
        log.info("Starting file cleanup task...");

        // 3일 전 시간 계산
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        log.info("Checking files deleted before: {}", threeDaysAgo);

        // 3일 전에 삭제 표시된 파일들 조회
        List<PostFile> filesToDelete = postFileRepository.findByIsDeletedTrueAndDeletedAtBefore(threeDaysAgo);

        if (!filesToDelete.isEmpty()) {
            log.info("Found {} files to delete", filesToDelete.size());
            postFileRepository.deleteAll(filesToDelete);
            log.info("Successfully deleted {} files", filesToDelete.size());
        } else {
            log.info("No files to delete");
        }
    }
}