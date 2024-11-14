package com.sesac.backend.enrollment.domain;

import jakarta.persistence.SecondaryTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMonitor {

    private final ConsumerFactory<String, String> consumerFactory;
    private static final String TOPIC_NAME = "enrollment-requests";

    @Value("${spring.kafka.monitor.lag-threshold}")
    private long lagThreshold;

    // 각 파티션의 Lag를 조회
    public Map<TopicPartition, Long> getLag() {
        Map<TopicPartition, Long> lags = new HashMap<>();

        try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            // 토픽의 파티션 할당
            List<PartitionInfo> partitionInfos = consumer.partitionsFor(TOPIC_NAME);
            if (partitionInfos != null && !partitionInfos.isEmpty()) {
                Set<TopicPartition> topicPartitions = partitionInfos.stream()
                        .map(pi -> new TopicPartition(TOPIC_NAME, pi.partition()))
                        .collect(Collectors.toSet());

                consumer.assign(topicPartitions);

                // 각 파티션의 마지막 offset 조회
                Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);

                // 각 파티션의 현재 offset과 lag 계산
                for (TopicPartition partition : topicPartitions) {
                    long currentOffset = consumer.position(partition);
                    long endOffset = endOffsets.get(partition);
                    long lag = Math.max(0, endOffset - currentOffset);

                    lags.put(partition, lag);

                    log.debug("Partition: {}, Current Offset: {}, End Offset: {}, Lag: {}",
                            partition.partition(), currentOffset, endOffset, lag);
                }
            } else {
                log.warn("토픽 {}에 대한 파티션 정보를 찾을 수 없습니다.", TOPIC_NAME);
            }

        } catch (Exception e) {
            log.error("Lag 조회 실패: {}", e.getMessage(), e);
        }
        return lags;
    }

    // 주기적으로 Lag를 체크하고 임계값을 초과하면 알림
    @Scheduled(fixedRateString = "${spring.kafka.monitor.check-interval}")
    public void checkLag() {
        try {
            Map<TopicPartition, Long> lags = getLag();

            // 전체 Lag 합계 계산
            long totalLag = lags.values().stream()
                    .mapToLong(Long::longValue)
                    .sum();

            log.info("현재 총 Lag: {}", totalLag);

            // 파티션별 Lag 체크
            lags.forEach((partition, lag) -> {
                if (lag > lagThreshold) {
                    String message = String.format(
                            "파티션의 %d의 Lag(%d)가 임계값(%d)을 초과했습니다",
                            partition.partition(), lag, lagThreshold
                    );
                    notifyAdmin(message);
                }
            });

            // 전체 Lag 체크
            if (totalLag > lagThreshold * lags.size()) {
                String message = String.format(
                        "전체 Lag(%d)가 심각한 수준입니다. 확인이 필요합니다.",
                        totalLag
                );
                notifyAdmin(message);
            }

        } catch (Exception e) {
            log.error("Lag 체크 중 오류 발생: {}", e.getMessage(), e);
        }

    }

    // 관리자에게 알림 전송
    private void notifyAdmin(String message) {
        log.warn("관리자 알림: {}", message);
        // 실제 알림 로직 구현 필요 (Slack, Email 알림 등)
    }
}
