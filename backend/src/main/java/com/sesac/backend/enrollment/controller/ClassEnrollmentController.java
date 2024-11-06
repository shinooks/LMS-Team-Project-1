package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.domain.classEnrollment.ClassEnrollment;
import com.sesac.backend.enrollment.domain.tempClasses.Classes;
import com.sesac.backend.enrollment.dto.ClassEnrollmentDto;
import com.sesac.backend.enrollment.dto.ClassesDto;
import com.sesac.backend.enrollment.service.ClassEnrollmentService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ClassEnrollmentController {

    @Autowired
    ClassEnrollmentService classEnrollmentService;

    @PostMapping("/enrollment")
    public Map<String, Object> enrollment(@RequestBody Map<String, Object> req) {
        Map<String, Object> res = new HashMap<>();

        // exceptionHandler를 만들어 줬기 때문에 exception을 던지는 과정이 필요가 없어졌음 -> 코드 간결함 증가
//        try {
//            String className = (String) req.get("className");
//            String studentId = (String) req.get("studentId");
//            classEnrollmentService.saveClassEnrollment(className, studentId);
//            res.put("status", "success");
//            res.put("message", "관심 강의가 성공적으로 등록되었습니다.");
//        } catch (DataIntegrityViolationException e) {
//            Throwable cause = e.getCause();
//
//            if (cause instanceof ConstraintViolationException) {
//                res.put("status", "error");
//                res.put("message", "이미 관심 강의에 등록된 강의입니다.");
//            } else {
//                res.put("status", "error");
//                res.put("message", "데이터베이스 제약 조건 위반이 발생했습니다.");
//            }
//        } catch (IllegalArgumentException e) {
//            res.put("status", "error");
//            res.put("message", e.getMessage());
//        }
//
//        return res;

        ClassEnrollmentDto classEnrollmentDto = new ClassEnrollmentDto();
        classEnrollmentDto.setClassName(req.get("className").toString());
        classEnrollmentDto.setStudentId(req.get("studentId").toString());

        // 관심 강의 등록 시도
        classEnrollmentService.saveClassEnrollment(classEnrollmentDto);

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
    public Map myclasslist(@PathVariable("studentid") String studentid) {
        Map map = new HashMap();
        map.put("myClassList", classEnrollmentService.getEnrolledClassById(studentid));
        map.put("myTimeTable", classEnrollmentService.getTimeTableById(studentid));
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
