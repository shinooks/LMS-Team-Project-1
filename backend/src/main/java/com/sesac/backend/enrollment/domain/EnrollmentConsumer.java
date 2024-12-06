package com.sesac.backend.enrollment.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.backend.enrollment.domain.exceptionControl.TimeOverlapException;
import com.sesac.backend.enrollment.dto.EnrollmentMessageDto;
import com.sesac.backend.enrollment.dto.EnrollmentUpdateMessageDto;
import com.sesac.backend.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentConsumer {

    private final ObjectMapper objectMapper;
    private final EnrollmentService enrollmentService;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = "enrollment-requests",
            groupId = "enrollment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeEnrollmentRequest(String message) {
        try {
            EnrollmentMessageDto enrollmentMessageDto = objectMapper.readValue(message, EnrollmentMessageDto.class);

            // 수강신청 처리 로직
            enrollmentService.processEnrollment(
                    enrollmentMessageDto.getStudentId(),
                    enrollmentMessageDto.getOpeningId()
            );

        } catch (JsonProcessingException e) {
            log.error("Consumer메시지 역직렬화 실패", e);
        }
    }

    @KafkaListener(
            topics = "enrollment-updates",
            groupId = "enrollment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeEnrollmentUpdate(String message) {
        try {
            EnrollmentUpdateMessageDto updateDto = objectMapper.readValue(message, EnrollmentUpdateMessageDto.class);

            messagingTemplate.convertAndSend(
                    "/topic/enrollment-updates/" + updateDto.getOpeningId(), updateDto);

            log.info("Consumer수강인원 업데이트 전송: openingId={}, currentEnrollment={}",
                    updateDto.getOpeningId(), updateDto.getCurrentEnrollment());
        } catch (JsonProcessingException e) {
            log.error("Consumer수강인원 업데이트 메시지 처리 실패", e);
        } catch (Exception e) {
            log.error("Consumer수강인원 업데이트 처리 중 오류 발생", e);
        }
    }

}