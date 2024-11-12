package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.dto.EnrollmentDto;
import com.sesac.backend.enrollment.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    EnrollmentService enrollmentService;

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

    @GetMapping("/allclasses")
    public Map allClasses() {
        Map map = new HashMap<>();
        map.put("allClasses", enrollmentService.getAllClasses());
        return map;
    }

    @GetMapping("/myclasslist/{studentid}")
    public Map myclasslist(@PathVariable("studentid") String student) {
        Map map = new HashMap();

        UUID studentId = UUID.fromString(student);
        map.put("myClassList", enrollmentService.getEnrolledClassForStudent(studentId));
        map.put("myTimeTable", enrollmentService.getTimeTableById(studentId));

//        System.out.println("수업 리스트 : " + map.get("myClassList"));
//        System.out.println("시간표 배열 : " + map.get("myTimeTable"));
        return map;
    }

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