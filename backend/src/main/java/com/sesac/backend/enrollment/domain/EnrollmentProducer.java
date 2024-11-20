package com.sesac.backend.enrollment.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.backend.enrollment.dto.EnrollmentMessageDto;
import com.sesac.backend.enrollment.dto.EnrollmentUpdateMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String ENROLLMENT_REQUEST_TOPIC = "enrollment-requests";
    private static final String ENROLLMENT_UPDATE_TOPIC = "enrollment-updates";

    public void sendEnrollmentRequest(EnrollmentMessageDto message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            String partitionKey = message.getOpeningId().toString();

            kafkaTemplate.send(ENROLLMENT_REQUEST_TOPIC, partitionKey, messageJson)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.debug("수강신청 메시지 전송 선공: opening={}, partition={}, offset={}",
                                    partitionKey,
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        } else {
                            log.error("수강신청 요청 메시지 전송 실패: opening={}, error={}", partitionKey, ex.getMessage());
                        }
                    });

        } catch (JsonProcessingException e) {
            log.error("메시지 직렬화 실패: {}", e.getMessage());
            throw new RuntimeException("메시지 전송 실패", e);
        }
    }

    public void sendEnrollmentUpdate(EnrollmentUpdateMessageDto message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            String partitionKey = message.getOpeningId().toString();

            kafkaTemplate.send(ENROLLMENT_UPDATE_TOPIC, partitionKey, messageJson)
                            .whenComplete((result, ex) -> {
                                if (ex == null) {
                                    log.debug("수강인원 업데이트 메시지 전송 성공: opening={}, count={}, partition={}, offset={}",
                                            partitionKey,
                                            message.getCurrentEnrollment(),
                                            result.getRecordMetadata().partition(),
                                            result.getRecordMetadata().offset());
                                } else {
                                    log.error("수강인원 업데이트 메시지 전송 실패: opening={}, error={}", partitionKey, ex.getMessage());
                                }
                            });

        } catch (JsonProcessingException e) {
            log.error("메시지 직렬화 실패: {}", e.getMessage());
            throw new RuntimeException("메시지 전송 실패", e);
        }
    }
}
