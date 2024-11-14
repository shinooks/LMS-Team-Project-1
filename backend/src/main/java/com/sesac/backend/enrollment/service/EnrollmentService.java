package com.sesac.backend.enrollment.service;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.course.repository.CourseTimeRepository;
import com.sesac.backend.enrollment.domain.InterestScheduleChecker;
import com.sesac.backend.enrollment.domain.exceptionControl.TimeOverlapException;
import com.sesac.backend.enrollment.dto.TimeTableCellDto;
import com.sesac.backend.enrollment.repository.EnrollmentRepository;
import com.sesac.backend.enrollment.repository.StudentRepositoryTmp;
import com.sesac.backend.entity.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private InterestScheduleChecker scheduleChecker;

    @Autowired
    private CourseOpeningRepository courseOpeningRepository;

    @Autowired
    private CourseTimeRepository courseTimeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepositoryTmp studentRepository;

    ////////////////////////////// save기능 set ////////////////////////////////////
    public void saveClassEnrollment(UUID studentId, UUID openingId) {
        Student student = studentRepository.findById(studentId).orElseThrow(()
                -> new EntityNotFoundException("학생을 찾을 수 없습니다"));
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId).orElseThrow(()
                -> new EntityNotFoundException("강의를 찾을 수 없습니다"));

        // 강의의 모든 CourseTime 가져옴
        List<CourseTime> openingCourseTimes = courseOpening.getCourseTimes();

        // 중복 및 시간 겹침 검사
        List<Enrollment> conflictingEnrollments = new ArrayList<>();

        for (CourseTime ct : openingCourseTimes) {
            List<Enrollment> enrollments = enrollmentRepository.findConflictingEnrollments(
                    student,
                    ct.getDayOfWeek(),
                    ct.getStartTime(),
                    ct.getEndTime()
            );

            conflictingEnrollments.addAll(enrollments);
        }

        //중복검사를 통과하지 못하는 강의가 있을 때 예외 발생
        if (!conflictingEnrollments.isEmpty()) {
            throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다." + courseOpening.getCourse().getCourseName());
        }

        // 통과 시 강의 등록
        System.out.println("중복검사 통과");

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourseOpening(courseOpening);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }

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


    public void deleteClassEnrollmentById(UUID enrollmentId) {
        // 수업 등록 정보 조회
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 등록 정보가 존재하지 않습니다: " + enrollmentId));

        TimeTableCellDto[][] deleteTargetTable = getTimeTableById(enrollment.getStudent().getStudentId());

        if (deleteTargetTable != null) {
            for (int i = 0; i < deleteTargetTable.length; i++) {
                for (int j = 0; j < deleteTargetTable[i].length; j++) {
                    if (deleteTargetTable[i][j] != null) {
                        if (enrollmentId.equals(deleteTargetTable[i][j].getEnrollmentId())) {
                            deleteTargetTable[i][j] = null;
                        }
                    }
                }
            }
        }
        // DB에서 삭제
        enrollmentRepository.deleteById(enrollmentId);
    }
}


