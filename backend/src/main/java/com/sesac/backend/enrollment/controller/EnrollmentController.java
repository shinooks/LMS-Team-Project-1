package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.dto.EnrollmentDto;
import com.sesac.backend.enrollment.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "수강신청", description = "전체 강의 목록을 로드하고 원하는 강의를 신청, 삭제할 수 있으며 신청된 강의는 수강 신청 목록 및 시간표에 표시")
public class EnrollmentController {

    @Autowired
    EnrollmentService enrollmentService;

    @Operation(summary = "신청 강의 저장", description = "수강신청아이디(enrollmentId), 학생아이디(studentId), 강의개설아이디(openingId), 강의수강신청일(enrollmentDate)")
    @PostMapping("/enrollment")
    public Map<String, Object> enrollment(@RequestBody EnrollmentDto enrollmentDto) {

        if (enrollmentDto.getStudentId() == null || enrollmentDto.getOpeningId() == null) {
            throw new IllegalArgumentException("studentId와 openingId는 필수 값입니다.");
        }

        Map<String, Object> res = new HashMap<>();

        // 관심 강의 등록 시도 -> saveService에 학생과 강의에 대한 정보를 찾기 위해 studentId와 classCode를 보냄
        enrollmentService.saveClassEnrollment(enrollmentDto.getStudentId(), enrollmentDto.getOpeningId());

        res.put("status", "success");
        res.put("message", "관심 강의가 성공적으로 등록되었습니다.");

        return res;
    }

    @Operation(summary = "전체 강의 목록 불러오기")
    @GetMapping("/allclasses")
    public Map allClasses() {
        Map map = new HashMap<>();
        map.put("allClasses", enrollmentService.getAllClasses());
        return map;
    }

    @Operation(summary = "학생별 신청 강의 목록 불러오기", description = "학생아이디(studentId)")
    @GetMapping("/myclasslist/{studentid}")
    public Map myclasslist(@PathVariable("studentid") UUID studentId) {
        Map map = new HashMap();

        map.put("myClassList", enrollmentService.getEnrolledClassById(studentId));
        map.put("myTimeTable", enrollmentService.getTimeTableById(studentId));

        return map;
    }

    @Operation(summary = "수강신청취소(삭제)", description = "수강신청아이디(enrollmentid)")
    @DeleteMapping("/myclasslist/delete/{enrollmentid}")
    public void deleteClassEnrollment(@PathVariable("enrollmentid") UUID enrollmentid) {
        try {
            enrollmentService.deleteClassEnrollmentById(enrollmentid);
            System.out.println("삭제 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}