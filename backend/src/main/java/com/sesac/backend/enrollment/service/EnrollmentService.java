package com.sesac.backend.enrollment.service;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.course.repository.CourseTimeRepository;
import com.sesac.backend.enrollment.domain.EnrollmentProducer;
import com.sesac.backend.enrollment.domain.ScheduleChecker;
import com.sesac.backend.enrollment.dto.EnrollmentMessageDto;
import com.sesac.backend.enrollment.dto.EnrollmentResultDto;
import com.sesac.backend.enrollment.dto.EnrollmentUpdateMessageDto;
import com.sesac.backend.enrollment.dto.TimeTableCellDto;
import com.sesac.backend.enrollment.repository.EnrollmentRepository;
import com.sesac.backend.enrollment.repository.StudentRepositoryTmp;
import com.sesac.backend.entity.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ScheduleChecker scheduleChecker;

    @Autowired
    private CourseOpeningRepository courseOpeningRepository;

    @Autowired
    private CourseTimeRepository courseTimeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepositoryTmp studentRepository;

    private final EnrollmentProducer enrollmentProducer;
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public List<Map<String, Object>> getAllClasses() {
        List<CourseOpening> tmpList = courseOpeningRepository.findAll();

        List<Map<String, Object>> allCoursesList = new ArrayList<>();

        for (CourseOpening c : tmpList) {

            Map<String, Object> courseInfo = new HashMap<>();

            UUID courseOpeningId = c.getOpeningId();
            UUID courseId = c.getCourse().getCourseId();

            Course course = courseRepository.findById(courseId).orElse(null);
            CourseOpening opening = courseOpeningRepository.findById(courseOpeningId).orElse(null);
            List<CourseTime> times = courseTimeRepository.findByCourseOpeningOpeningId(courseOpeningId);

            String courseCode = course.getCourseCode();
            String courseName = course.getCourseName();
            Integer credit = course.getCredits();

            String professorName = opening.getProfessor().getName();
            Integer maxStudents = opening.getMaxStudents();
            Integer currentStudents = opening.getCurrentStudents();

            LocalTime startTime = null;
            LocalTime endTime = null;
            String day = null;

            for (CourseTime time : times) {
                startTime = time.getStartTime();
                endTime = time.getEndTime();
                day = time.getDayOfWeek().getDescription();
            }

            courseInfo.put("openingId", courseOpeningId);
            courseInfo.put("courseId", courseId);
            courseInfo.put("courseCode", courseCode);
            courseInfo.put("courseName", courseName);
            courseInfo.put("professorName", professorName);
            courseInfo.put("credit", credit);
            courseInfo.put("day", day);
            courseInfo.put("startTime", startTime);
            courseInfo.put("endTime", endTime);
            courseInfo.put("maxStudents", maxStudents);
            courseInfo.put("currentStudents", currentStudents);

            allCoursesList.add(courseInfo);
        }

        return allCoursesList;
    }

    public List<Map<String, Object>> getEnrolledClassById(UUID studentId) {
        List<Enrollment> tmpList = enrollmentRepository.findByStudent_StudentId(studentId);

        List<Map<String, Object>> enrolledCourseList = new ArrayList<>();

        for (Enrollment c : tmpList) {

            Map<String, Object> enrolledCourseInfo = new HashMap<>();

            UUID enrollmentId = c.getEnrollmentId();
            UUID openingId = c.getCourseOpening().getOpeningId();
            String courseCode = c.getCourseOpening().getCourse().getCourseCode();
            String courseName = c.getCourseOpening().getCourse().getCourseName();
            Integer credit = c.getCourseOpening().getCourse().getCredits();

            String professorName = c.getCourseOpening().getProfessor().getName();
            Integer maxStudents = c.getCourseOpening().getMaxStudents();
            Integer currentStudents = c.getCourseOpening().getCurrentStudents();

            String day = null;
            LocalTime startTime = null;
            LocalTime endTime = null;

            for (CourseTime courseTime : c.getCourseOpening().getCourseTimes()) {
                day = courseTime.getDayOfWeek().getDescription();
                startTime = courseTime.getStartTime();
                endTime = courseTime.getEndTime();
            }

            enrolledCourseInfo.put("enrollmentId", enrollmentId);
            enrolledCourseInfo.put("openingId", openingId);
            enrolledCourseInfo.put("courseCode", courseCode);
            enrolledCourseInfo.put("courseName", courseName);
            enrolledCourseInfo.put("professorName", professorName);
            enrolledCourseInfo.put("credit", credit);
            enrolledCourseInfo.put("day", day);
            enrolledCourseInfo.put("startTime", startTime);
            enrolledCourseInfo.put("endTime", endTime);
            enrolledCourseInfo.put("maxStudents", maxStudents);
            enrolledCourseInfo.put("currentStudents", currentStudents);

            enrolledCourseList.add(enrolledCourseInfo);
        }
        return enrolledCourseList;
    }

    public TimeTableCellDto[][] getTimeTableById(UUID studentId) {

        List<Enrollment> enrollments = enrollmentRepository.findByStudent_StudentId(studentId);

        List<TimeTableCellDto> enrollmentsForTable = enrollments.stream()
                .map(enrollment -> new TimeTableCellDto(
                        enrollment.getEnrollmentId(),
                        enrollment.getCourseOpening().getOpeningId(),
                        enrollment.getCourseOpening().getCourse().getCourseCode(),
                        enrollment.getCourseOpening().getCourse().getCourseName()
                ))
                .toList();
        return scheduleChecker.timeTableMaker(enrollmentsForTable);
    }

    @Transactional
    public void deleteClassEnrollmentById(UUID enrollmentId) {
        // 수업 등록 정보 조회
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 등록 정보가 존재하지 않습니다: " + enrollmentId));

        UUID openingId = enrollment.getCourseOpening().getOpeningId();
        UUID studentId = enrollment.getStudent().getStudentId();
        CourseOpening courseOpening = enrollment.getCourseOpening();

        try {
            // 1. DB에서 현재 수강인원 확인
            int currentDBCount = courseOpening.getCurrentStudents();

            // 2. Redis에서 현재 수강인원 확인 및 감소
            String redisKey = "course:" + openingId + ":enrollment";
            String redisCount = redisTemplate.opsForValue().get(redisKey);
            int currentRedisCount = redisCount != null ? Integer.parseInt(redisCount) : currentDBCount;

            // 3. 수강인원 감소 (음수 방지)
            int newCount = Math.max(0, currentRedisCount - 1);

            // 4. DB 업데이트
            courseOpening.setCurrentStudents(newCount);
            courseOpeningRepository.save(courseOpening);

            // 5. Redis 업데이트 (decrement 대신 명시적 설정)
            redisTemplate.opsForValue().set(redisKey, String.valueOf(newCount));

            // 6. DB에서 수강신청 정보 삭제
            enrollmentRepository.deleteById(enrollmentId);

            // 7. 시간표에서 삭제할 강의 처리
            TimeTableCellDto[][] deleteTargetTable = getTimeTableById(studentId);
            if (deleteTargetTable != null) {
                for (int i = 0; i < deleteTargetTable.length; i++) {
                    for (int j = 0; j < deleteTargetTable[i].length; j++) {
                        if (deleteTargetTable[i][j] != null &&
                                enrollmentId.equals(deleteTargetTable[i][j].getEnrollmentId())) {
                            deleteTargetTable[i][j] = null;
                        }
                    }
                }
            }

            // 8. WebSocket으로 업데이트 알림(모든 사용자에게)
            notifyAllStudents(openingId, newCount, courseOpening.getMaxStudents());

            //9. WebSocket으로 업데이트 알림(Kafka)
            enrollmentProducer.sendEnrollmentUpdate(new EnrollmentUpdateMessageDto(
                    openingId,
                    studentId,
                    newCount
            ));

            // 10. 성공 메시지 전송
            EnrollmentResultDto result = new EnrollmentResultDto(
                    openingId,
                    courseOpening.getCourse().getCourseCode(),
                    courseOpening.getCourse().getCourseName(),
                    "수강신청이 취소되었습니다",
                    courseOpening.getMaxStudents(),
                    newCount,
                    true
            );
            notifyStudent(studentId, result);

            log.info("수강신청 취소 완료: student={}, course={}, newCount={}",
                    studentId, openingId, newCount);

        } catch (Exception e) {
            // Redis 복구 시도
            try {
                String redisKey = "course:" + openingId + ":enrollment";
                redisTemplate.opsForValue().set(redisKey, String.valueOf(courseOpening.getCurrentStudents()));
            } catch (Exception redisError) {
                log.error("Redis 복구 실패: {}", redisError.getMessage());
            }

            log.error("수강신청 취소 실패: student={}, course={}, error={}",
                    studentId, openingId, e.getMessage());
            throw new RuntimeException("수강신청 취소 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public void initializeEnrollmentCount() {
        try {
            log.info("수강신청 카운트 초기화 시작");
            List<CourseOpening> infos = courseOpeningRepository.findAll();

            for (CourseOpening info : infos) {
                String redisKey = "course:" + info.getOpeningId() + ":enrollment";

                // Redis에 초기 수강인원 설정
                redisTemplate.opsForValue().set(
                        redisKey, String.valueOf(info.getCurrentStudents())
                );

                log.debug("Redis 초기화: course={}, count={}",
                        info.getOpeningId(),
                        info.getCurrentStudents());
            }
            log.info("수강신청 카운트 초기화 완료: {} 개 강의", infos.size());

        } catch (Exception e) {
            log.error("수강신청 카운트 초기화 실패: {}", e.getMessage());
            throw new RuntimeException("수강신청 초기화 실패", e);
        }
    }


    public void requestEnrollment(UUID studentId, UUID openingId) {
        try {
            EnrollmentMessageDto message = new EnrollmentMessageDto(
                    studentId,
                    openingId,
                    LocalDateTime.now()
            );

            enrollmentProducer.sendEnrollmentRequest(message);

            log.info("수강신청 요청 전송: student={}, openingId={}", studentId, openingId);

        } catch (Exception e) {
            log.error("수강신청 요청 실패: student={}, openingId={}, error={}", studentId, openingId, e.getMessage());
            throw new RuntimeException("수강신청 요청 처리 중 오류가 발생했습니다.", e);
        }
    }


    @Transactional
    public void processEnrollment(UUID studentId, UUID openingId) {
        CourseOpening courseInfo = courseOpeningRepository.findById(openingId).orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다"));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("학생을 찾을 수 없습니다."));

        String redisKey = "course:" + openingId + ":enrollment";

        try {
            // 1. 동일 강의 중복 검사
            if (enrollmentRepository.existsByStudent_StudentIdAndCourseOpening_OpeningId(studentId, openingId)) {
                log.info("중복 수강신청 감지: student={}, course={}", studentId, openingId);
                notifyStudent(studentId, new EnrollmentResultDto(
                        openingId,
                        courseInfo.getCourse().getCourseCode(),
                        courseInfo.getCourse().getCourseName(),
                        "이미 수강신청된 강의입니다",
                        courseInfo.getMaxStudents(),
                        courseInfo.getCurrentStudents(),
                        false
                ));
                return;
            }

            // 2. 시간 중복 검사
            List<CourseTime> times = courseInfo.getCourseTimes();
            List<Enrollment> conflictingEnrollments = new ArrayList<>();

            for (CourseTime ct : times) {
                conflictingEnrollments.addAll(
                        enrollmentRepository.findConflictingEnrollments(
                                student,
                                ct.getDayOfWeek(),
                                ct.getStartTime(),
                                ct.getEndTime()
                        )
                );
            }

            if (!conflictingEnrollments.isEmpty()) {
                log.info("시간 중복 감지: student={}, course={}", studentId, openingId);
                notifyStudent(studentId, new EnrollmentResultDto(
                        openingId,
                        courseInfo.getCourse().getCourseCode(),
                        courseInfo.getCourse().getCourseName(),
                        "시간이 겹치는 강의가 이미 등록되어 있습니다",
                        courseInfo.getMaxStudents(),
                        courseInfo.getCurrentStudents(),
                        false
                ));
                return;
            }

            // Redis의 increment 작업은 원자적(atomic)으로 실행됨
            // 3. Redis 수강인원 증가 (실시간 처리)
            Integer currentEnrollment = redisTemplate.opsForValue().increment(redisKey).intValue();
            Integer maxEnrollment = courseInfo.getMaxStudents();

            // 4. 수강인언 초과 체크
            if (currentEnrollment > maxEnrollment) {
                // 수강인원 초과 시 카운트 감소
                redisTemplate.opsForValue().decrement(redisKey);
                notifyStudent(studentId, new EnrollmentResultDto(
                        openingId,
                        courseInfo.getCourse().getCourseCode(),
                        courseInfo.getCourse().getCourseName(),
                        "수강인원이 초과되었습니다",
                        maxEnrollment,
                        currentEnrollment - 1,
                        false
                ));
                return;
            }

            // 5. DB에 수강신청 정보 저장 (기존 saveClassEnrollment 로직)
            Enrollment enrollment = Enrollment.builder()
                    .student(student)
                    .courseOpening(courseInfo)
                    .enrollmentDate(LocalDateTime.now())
                    .build();

            enrollmentRepository.save(enrollment);


            // 6. 성공 알림 및 후속 처리
            EnrollmentResultDto successResult = new EnrollmentResultDto(
                    openingId,
                    courseInfo.getCourse().getCourseCode(),
                    courseInfo.getCourse().getCourseName(),
                    "수강신청이 완료되었습니다",
                    maxEnrollment,
                    currentEnrollment,
                    true
            );
            notifyStudent(studentId, successResult);
            notifyAllStudents(openingId, currentEnrollment, maxEnrollment);

            // 7. DB 업데이트를 위한 메시지 발행
            enrollmentProducer.sendEnrollmentUpdate(new EnrollmentUpdateMessageDto(
                    openingId,
                    studentId,
                    currentEnrollment
            ));

            log.warn("수강신청 성공: student={}, course={}", studentId, openingId);

        } catch (Exception e) {
            if (redisTemplate.opsForValue().get(redisKey) != null) {
                redisTemplate.opsForValue().decrement(redisKey);
            }
            log.error("수강신청 실패: student={}, course={}, error={}",
                    studentId, openingId, e.getMessage());
            throw e;
        }
    }

private void notifyStudent(UUID studentId, EnrollmentResultDto result) {
    log.info("수강신청 결과 전송: student={}, success={}, message={}",
            studentId, result.isSuccess(), result.getMessage());
    messagingTemplate.convertAndSendToUser(
            studentId.toString(),
            "/topic/enrollment-result",
            result
    );
}

private void notifyAllStudents(UUID openingId, int currentEnrollment, int maxEnrollment) {
    try {
        CourseOpening courseInfo = courseOpeningRepository.findById(openingId).orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다"));

        Map<String, Object> status = Map.of(
                "openingId", openingId,
                "courseCode", courseInfo.getCourse().getCourseCode(),
                "courseName", courseInfo.getCourse().getCourseName(),
                "currentEnrollment", currentEnrollment,
                "maxEnrollment", maxEnrollment
        );

        messagingTemplate.convertAndSend("/topic/course-status/" + openingId, status);

    } catch (Exception e) {
        log.error("전체 알림 전송 실패: course={}, error={}", openingId, e.getMessage());
    }
}

}



