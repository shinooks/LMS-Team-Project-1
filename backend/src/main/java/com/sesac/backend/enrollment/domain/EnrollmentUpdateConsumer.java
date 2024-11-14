package com.sesac.backend.enrollment.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.enrollment.dto.EnrollmentUpdateMessageDto;
import com.sesac.backend.entity.CourseOpening;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EnrollmentUpdateConsumer {

    private final CourseOpeningRepository courseOpeningRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "enrollment-updates", groupId = "enrollment-update-group")
    public void processEnrollmentUpdate(String message) {
        try {
            // JSON 문자열을 DTO로 변환
            EnrollmentUpdateMessageDto dto = objectMapper.readValue(message, EnrollmentUpdateMessageDto.class);

            CourseOpening info = courseOpeningRepository.findById(dto.getOpeningId()).orElseThrow();

            info.setCurrentStudents(dto.getCurrentEnrollment());

            courseOpeningRepository.save(info);

            log.info("DB 수강인원 업데이트 완료: opening={}, count={}", dto.getOpeningId(), dto.getCurrentEnrollment());

        } catch (Exception e) {
            log.error("DB 업데이트 실패: {}", e.getMessage());
        }
    }
}
