package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.service.EnrollmentService;
import com.sesac.backend.entity.UserAuthentication;
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
    public Map<String, Object> enrollment(@RequestBody Map<String, Object> req) {
        Map<String, Object> res = new HashMap<>();

        //String enrollStudentId = (String) req.get("studentId");

        UUID studentId = req.get("studentId") != null ? UUID.fromString(req.get("studentId").toString()) : null;

        UUID courseId = req.get("classId") != null ? UUID.fromString(req.get("classId").toString()) : null;
        
        // 관심 강의 등록 시도 -> saveService에 학생과 강의에 대한 정보를 찾기 위해 studentId와 classCode를 보냄
        enrollmentService.saveClassEnrollment(studentId, courseId);

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
        //UserAuthentication studentId = UserAuthentication.fromString(student);
        UUID studentId = UUID.fromString(student);
        map.put("myClassList", enrollmentService.getEnrolledCourses(studentId));
        map.put("myTimeTable", enrollmentService.getTimeTableById(studentId));
        return map;
    }

    @DeleteMapping("/myclasslist/delete/{enrollmentid}")
    public void deleteClassEnrollment(@PathVariable("enrollmentid") long enrollmentid) {
        try {
            enrollmentService.deleteClassEnrollmentById(enrollmentid);
            System.out.println("삭제 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
