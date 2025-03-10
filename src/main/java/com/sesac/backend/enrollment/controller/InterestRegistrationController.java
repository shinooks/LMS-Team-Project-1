package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.service.InterestEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class InterestRegistrationController {

    @Autowired
    InterestEnrollmentService interestEnrollmentService;

    @PostMapping("/saveStudentInterest")
    public ResponseEntity<String> saveStudentInterest(@RequestBody Map<String, String> req) {
        UUID studentId = UUID.fromString(req.get("studentId"));
        UUID openingId = UUID.fromString(req.get("openingId"));
        try {
            interestEnrollmentService.saveStudentInterest(studentId, openingId);
            return ResponseEntity.status(HttpStatus.CREATED).body("관심 강의가 등록되었습니다.");
        } catch (RuntimeException e) {
            // RuntimeException이 발생한 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/deleteStudentInterest/{studentId}/{openingId}")
    public void deleteStudentInterest(@PathVariable UUID studentId, @PathVariable UUID openingId) {
        interestEnrollmentService.deleteStudentInterest(studentId, openingId);
    }

    @GetMapping("/interestList/{studentId}")
    public List<Map<String, Object>> getStudentInterest(@PathVariable UUID studentId) {
        return interestEnrollmentService.getStudentInterests(studentId);
    }

    @GetMapping("/interestTimeTable/{studentId}")
    public Map getInterestTimeTable(@PathVariable UUID studentId) {
        Map map = new HashMap();
        map.put("interestTimeTable", interestEnrollmentService.getTimeTableById(studentId));

        //System.out.println("프런트로 넘어갈 배열 : " + Arrays.deepToString((Object[]) map.get("interestTimeTable")));
        return map;
    }

}
