package com.sesac.backend.enrollment.domain;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class KafkaTopicInitializer_OnlyForTest {

    private final AdminClient adminClient;

    public KafkaTopicInitializer_OnlyForTest(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    @PostConstruct
    public void initializeTopics() {
        List<String> topicNames = Arrays.asList("enrollment-requests", "enrollment-updates");

        for (String topicName : topicNames) {
            try {
                // 1. 토픽 존재 여부 확인
                boolean topicExists = topicExists(topicName);

                // 2. 토픽이 존재하면 삭제
                if (topicExists) {
                    log.info("토픽 '{}'이(가) 존재하여 삭제를 시도합니다.", topicName);
                    deleteTopicAndWait(topicName);
                }

                // 3. 새 토픽 생성
                createTopic(topicName);
                log.info("새 Kafka Topic 생성 완료: {}", topicName);

            } catch (Exception e) {
                log.error("Kafka Topic 초기화 중 오류 발생: {}", e.getMessage(), e);
            }
        }
    }

    private boolean topicExists(String topicName) {
        try {
            adminClient.describeTopics(Collections.singletonList(topicName))
                    .values()
                    .get(topicName)
                    .get();
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    private void deleteTopicAndWait(String topicName) {
        try {
            adminClient.deleteTopics(Collections.singletonList(topicName))
                    .all()
                    .get(10, TimeUnit.SECONDS); // 10초 타임아웃 설정

            // 삭제 완료 대기
            Thread.sleep(2000); // 토픽 삭제가 완전히 처리될 때까지 잠시 대기
        } catch (Exception e) {
            log.warn("토픽 삭제 중 오류 발생: {}", e.getMessage());
        }
    }

    private void createTopic(String topicName) {
        try {
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(newTopic))
                    .all()
                    .get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("토픽 생성 실패: " + topicName, e);
        }
    }
}
