package com.sesac.backend.enrollment.controller;

import com.sesac.backend.enrollment.dto.CourseEnrollmentDto;
import com.sesac.backend.enrollment.service.ClassEnrollmentService;
import com.sesac.backend.entity.Student;
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

        CourseEnrollmentDto courseEnrollmentDto = new CourseEnrollmentDto();
        Student student = (Student) req.get("student"); //응답을 student type으로 파싱

        courseEnrollmentDto.setCourseName(req.get("className").toString());
        courseEnrollmentDto.setStudent( student );

        // 관심 강의 등록 시도
        classEnrollmentService.saveClassEnrollment(courseEnrollmentDto);

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
        UUID studentId = UUID.fromString(studentid);
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
