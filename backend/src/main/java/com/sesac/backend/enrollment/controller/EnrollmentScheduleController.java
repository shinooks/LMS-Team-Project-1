package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.dto.SchedulerDto;
import com.sesac.backend.enrollment.service.InterestEnrollmentScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EnrollmentScheduleController {
    @Autowired
    private InterestEnrollmentScheduler interestEnrollmentScheduler;

    // 관심강의 등록 기간 설정
    @PostMapping("/setInterestPeriod")
    public ResponseEntity<?> setInterestPeriod(@RequestBody SchedulerDto schedulerDto) {
        System.out.println("요청받은 등록 시작 시간 : " + schedulerDto.getStartTime());
        System.out.println("요청받은 등록 종료 시간 : " + schedulerDto.getEndTime());
        try {
            LocalDateTime interestStart = schedulerDto.getStartTime();
            LocalDateTime interestEnd = schedulerDto.getEndTime();
            interestEnrollmentScheduler.setInterestPeriod(interestStart, interestEnd);

            // 설정 성공 시 true 반환
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    // 현재 관심 강의 등록 상태 조회
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getInterestStatus() {
        Map<String, Object> status = new HashMap<>();

        boolean isActive = interestEnrollmentScheduler.isInterestActive();

        status.put("status", isActive);

        return ResponseEntity.ok(status);
    }
}
