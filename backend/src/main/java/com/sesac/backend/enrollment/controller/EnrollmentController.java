package com.sesac.backend.enrollment.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.enrollment.dto.EnrollmentDto;
import com.sesac.backend.enrollment.service.EnrollmentService;
import com.sesac.backend.entity.CourseOpening;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "수강신청", description = "전체 강의 목록을 로드하고 원하는 강의를 신청, 삭제할 수 있으며 신청된 강의는 수강 신청 목록 및 시간표에 표시")
public class EnrollmentController {

    @Autowired
    EnrollmentService enrollmentService;

    private final RateLimiter rateLimiter;

    // 트래픽 테스트용
    @Autowired
    CourseOpeningRepository courseOpeningRepository;

    // 트래픽 테스트용
    @Autowired
    RedisTemplate<String, String> redisTemplate;


    public EnrollmentController() {
        // 초당 100개 요청으로 제한
        this.rateLimiter = RateLimiter.create(100.0);
    }

    @Operation(summary = "신청 강의 저장", description = "수강신청아이디(enrollmentId), 학생아이디(studentId), 강의개설아이디(openingId), 강의수강신청일(enrollmentDate)")
    @PostMapping("/enrollment")
    public Map<String, Object> enrollment(@RequestBody EnrollmentDto enrollmentDto) {

        Map<String, Object> res = new HashMap<>();

        // 요청 제한 확인
        if (!rateLimiter.tryAcquire()) {
            res.put("status", "error");
            res.put("message", "서버가 과부화 상태입니다. 잠시 후 다시 시도해주세요");
        }

        if (enrollmentDto.getStudentId() == null || enrollmentDto.getOpeningId() == null) {
            throw new IllegalArgumentException("studentId와 openingId는 필수 값입니다.");
        }

        enrollmentService.requestEnrollment(
                enrollmentDto.getStudentId(),
                enrollmentDto.getOpeningId()
        );

        res.put("status", "pending");
        res.put("message", "수강신청이 요청되었습니다. 결과를 기다려주세요");

        return res;
    }

    @Operation(summary = "전체 강의 목록 불러오기")
    @GetMapping("/allcourses")
    public Map allClasses() {
        Map map = new HashMap<>();
        map.put("allcourses", enrollmentService.getAllClasses());
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

//    // 트래픽 테스트용
//    @GetMapping("/enrollment/status/{studentId}/{openingId}")
//    public ResponseEntity<?> getEnrollmentStatus(
//            @PathVariable UUID studentId,
//            @PathVariable UUID openingId
//    ) {
//        try {
//            // 1. 수강신청 처리 결과 확인 (Redis)
//            String resultKey = "enrollment:result:" + studentId + ":" + openingId;
//            String result = redisTemplate.opsForValue().get(resultKey);
//
//            // 2. 현재 수강 인원 확인 (Redis)
//            String enrollmentKey = "course:" + openingId + ":enrollment";
//            String currentEnrollmentStr = redisTemplate.opsForValue().get(enrollmentKey);
//
//            // 3. 강의 정보 확인 (DB)
//            CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
//                    .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다"));
//
//            if (result == null) {
//                // 아직 처리 중
//                return ResponseEntity.ok(Map.of(
//                        "success", false,
//                        "message", "처리 중입니다",
//                        "processed", false,
//                        "currentEnrollment", currentEnrollmentStr != null ? Integer.parseInt(currentEnrollmentStr) : 0,
//                        "maxEnrollment", courseOpening.getMaxStudents()
//                ));
//            }
//
//            boolean isSuccess = Boolean.parseBoolean(result);
//            return ResponseEntity.ok(Map.of(
//                    "success", isSuccess,
//                    "message", isSuccess ? "수강신청이 완료되었습니다" : "수강인원이 초과되었습니다",
//                    "processed", true,
//                    "currentEnrollment", currentEnrollmentStr != null ? Integer.parseInt(currentEnrollmentStr) : 0,
//                    "maxEnrollment", courseOpening.getMaxStudents()
//            ));
//
//        } catch (Exception e) {
//            log.error("수강신청 상태 조회 실패: student={}, opening={}, error={}",
//                    studentId, openingId, e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of(
//                            "success", false,
//                            "message", "상태 조회 중 오류가 발생했습니다",
//                            "processed", false
//                    ));
//        }
//    }
//
//    // 트래픽 테스트용
//    @GetMapping("/course-status/{openingId}")
//    public ResponseEntity<?> getCourseStatus(@PathVariable UUID openingId) {
//        try {
//            // 1. Redis에서 현재 수강 인원 확인
//            String enrollmentKey = "course:" + openingId + ":enrollment";
//            String currentEnrollmentStr = redisTemplate.opsForValue().get(enrollmentKey);
//            int currentEnrollment = currentEnrollmentStr != null ?
//                    Integer.parseInt(currentEnrollmentStr) : 0;
//
//            // 2. DB에서 강의 정보 확인
//            CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
//                    .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다"));
//
//            return ResponseEntity.ok(Map.of(
//                    "currentEnrollment", currentEnrollment,
//                    "maxEnrollment", courseOpening.getMaxStudents(),
//                    "courseCode", courseOpening.getCourse().getCourseCode(),
//                    "courseName", courseOpening.getCourse().getCourseName()
//            ));
//
//        } catch (Exception e) {
//            log.error("강의 상태 조회 실패: opening={}, error={}", openingId, e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of(
//                            "message", "강의 상태 조회 중 오류가 발생했습니다"
//                    ));
//        }
//    }
}