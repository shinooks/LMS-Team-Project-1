package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.dto.CourseEnrollmentDto;
import com.sesac.backend.enrollment.service.ClassEnrollmentService;
import com.sesac.backend.entity.Student;
import com.sesac.backend.entity.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class ClassEnrollmentController {

    @Autowired
    ClassEnrollmentService classEnrollmentService;

    @PostMapping("/enrollment")
    public Map<String, Object> enrollment(@RequestBody Map<String, Object> req) {
        Map<String, Object> res = new HashMap<>();

        // 받은 요청 값 String 으로 변환
        UserAuthentication studentId = (UserAuthentication) req.get("studentId");
        UUID openingId = req.get("openingId") != null ? UUID.fromString(req.get("openingId").toString()) : null;

        // 관심 강의 등록 시도
        classEnrollmentService.saveClassEnrollment(studentId, openingId);

        res.put("status", "success");
        res.put("message", "관심 강의가 성공적으로 등록되었습니다.");

        return res;
    }

    @GetMapping("/allclasses")
    public Map allClasses() {
        Map map = new HashMap<>();
        map.put("allClasses", classEnrollmentService.getAllClasses());
        return map;
    }

    @GetMapping("/myclasslist/{studentid}")
    public Map myclasslist(@PathVariable("studentid") UserAuthentication studentId) {
        Map map = new HashMap();
//        UUID studentId = UUID.fromString(studentid);
        map.put("myClassList", classEnrollmentService.getEnrolledClassById(studentId));
        map.put("myTimeTable", classEnrollmentService.getTimeTableById(studentId));
        return map;
    }

    @DeleteMapping("/myclasslist/delete/{enrollmentid}")
    public void deleteClassEnrollment(@PathVariable("enrollmentid") long enrollmentid) {
        try {
            classEnrollmentService.deleteClassEnrollmentById(enrollmentid);
            System.out.println("삭제 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
