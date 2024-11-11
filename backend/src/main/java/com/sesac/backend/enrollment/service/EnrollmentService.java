package com.sesac.backend.enrollment.service;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.course.repository.CourseTimeRepository;
//import com.sesac.backend.enrollment.domain.ScheduleChecker;
import com.sesac.backend.enrollment.domain.exceptionControl.TimeOverlapException;
import com.sesac.backend.enrollment.dto.EnrollmentDto;
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

//    @Autowired
//    private ScheduleChecker scheduleChecker;

    @Autowired
    private CourseOpeningRepository courseOpeningRepository;

    @Autowired
    private CourseTimeRepository courseTimeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepositoryTmp studentRepository;


    private List<EnrollmentDto> convertToDto(UUID studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Set<Enrollment> tmpList = enrollmentRepository.findByStudent(student);
        List<EnrollmentDto> classListById = new ArrayList<>();

        for (Enrollment c : tmpList) {
            classListById.add(new EnrollmentDto(
                    c.getEnrollmentId(),
                    c.getStudent().getStudentId(),
                    c.getCourseOpening().getOpeningId(),
                    c.getEnrollmentDate()
            ));
        }
        return classListById;
    }

    ////////////////////////////// save기능 set ////////////////////////////////////
<<<<<<< HEAD
    public void saveClassEnrollment(UUID studentId, UUID courseId) {

        // classId로 Course의 정보를 조회
        List<Course> course = courseRepository.findCourseByCourseId(courseId);
=======
    public void saveClassEnrollment(UUID studentId, UUID openingId) {
//        // classCode로 Course의 정보를 조회
//        Course course = courseRepository.findById(courseId).orElse(null);
//
//        if( course == null ) {
//            throw new IllegalArgumentException("Course not found");
//        }
//
//        // Student 객체 조회
//        Student student = enrollStudentRepository.findById(studentId)
//                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
//
//        System.out.println(student);
//
//        // 강의 개설 정보 조회
//        List<CourseOpening> courseOpenings = courseOpeningRepository.findByCourse(course);
//
//        System.out.println(courseOpenings);
//
//
//        // list type에서 CourseOpening type으로 변경... repository의 type도 변경해야 하지 않을까..?
//        CourseOpening openingCourseInfo = courseOpenings.get(0); // 첫 번째 요소 선택
//
//        // 중복 및 시간 겹침 검사
//        List<CourseTime> openingCourseTimes = openingCourseInfo.getCourseTimes();
//
//        for(CourseTime ct : openingCourseTimes){
//            List<Enrollment> conflictingEnrollments = enrollmentRepository.findConflictingEnrollments(
//                    student,
//                    ct.getDayOfWeek(),
//                    ct.getStartTime(),
//                    ct.getEndTime()
//            );
//
//            System.out.println("중복검사 통과-------------------------------------------------------------------------");
//
//            if(!conflictingEnrollments.isEmpty()) {
//                throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다." + course.getCourseName());
//            }
//        }
//
//        // 중복검사를 통과하면 새로운 등록강의 생성
//        Enrollment enrollment = new Enrollment(
//                student, openingCourseInfo, openingCourseInfo.getCourse().getCourseName()
//        );
//
//        System.out.println("생성하려는 정보 : " + enrollment);
//
//        enrollmentRepository.save(enrollment);
>>>>>>> d54f7c357a10ea6352a75d159528718d92d75cb4

//        courseOpeningRepository.findByCourseCourseId(courseId);
//
//        System.out.println("courseOpening : " + courseOpeningRepository.findByCourseCourseId(courseId));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("학생을 찾을 수 없습니다"));
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId).orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다"));

<<<<<<< HEAD
        Course course1 = course.get(0);

        // 강의 개설 정보 조회
        List<CourseOpening> courseOpenings = courseOpeningRepository.findByCourse(course1);

        // list type에서 CourseOpening type으로 변경... repository의 type도 변경해야 하지 않을까..?
        CourseOpening openingCourseInfo = courseOpenings.get(0); // 첫 번째 요소 선택

        // 중복 및 시간 겹침 검사
        List<CourseTime> openingCourseTimes = openingCourseInfo.getCourseTimes();

        for(CourseTime ct : openingCourseTimes){
            List<Enrollment> conflictingEnrollments = enrollmentRepository.findConflictingEnrollments(
                    student,
                    ct.getDayOfWeek(),
                    ct.getStartTime(),
                    ct.getEndTime()
            );

            if(!conflictingEnrollments.isEmpty()) {
                throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다." + course.get(0).getCourseName());
            }
        }

        // 중복검사를 통과하면 새로운 등록강의 생성
        Enrollment enrollment = new Enrollment(
                student, openingCourseInfo, openingCourseInfo.getCourse().getCourseName()
        );
=======
        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourseOpening(courseOpening);
        enrollment.setEnrollmentDate(LocalDateTime.now());
>>>>>>> d54f7c357a10ea6352a75d159528718d92d75cb4

        enrollmentRepository.save(enrollment);
    }

    public List<Map<String, Object>> getAllClasses() {
        List<CourseOpening> tmpList = courseOpeningRepository.findAll();

        List<Map<String, Object>> allCoursesList = new ArrayList<>();

        for (CourseOpening c : tmpList) {

            Map<String, Object> courseInfo = new HashMap<>();

            UUID courseOpeningId = c.getOpeningId();
            UUID courseId = c.getCourse().getCourseId();

            List<Course> courses = courseRepository.findCourseByCourseId(courseId);
            List<CourseTime> times = courseTimeRepository.findByCourseOpeningOpeningId(courseOpeningId);

            String courseCode = "";
            String courseName = "";
            Integer credit = 0;

            for (Course cs : courses) {
                courseCode = cs.getCourseCode();
                courseName = cs.getCourseName();
                credit = cs.getCredits();
            }

            LocalTime startTime = null;
            LocalTime endTime = null;
            DayOfWeek day = null;

            for (CourseTime time : times) {
                startTime = time.getStartTime();
                endTime = time.getEndTime();
                day = time.getDayOfWeek();
            }

<<<<<<< HEAD
            courseInfo.add(courseId);
            courseInfo.add(courseCode);
            courseInfo.add(courseName);
            courseInfo.add(credit);
            courseInfo.add(day);
            courseInfo.add(startTime);
            courseInfo.add(endTime);
=======
            courseInfo.put("openingId", courseOpeningId);
            courseInfo.put("courseId", courseId);
            courseInfo.put("courseCode", courseCode);
            courseInfo.put("courseName", courseName);
            courseInfo.put("credit", credit);
            courseInfo.put("day", day);
            courseInfo.put("startTime", startTime);
            courseInfo.put("endTime", endTime);
>>>>>>> d54f7c357a10ea6352a75d159528718d92d75cb4

            allCoursesList.add(courseInfo);
        }

        return allCoursesList;
    }

    public List<Object> getEnrolledCourses(UUID studentId) {
        List<Object> enrollmentList = enrollmentRepository.findByStudent_StudentId(studentId);

        System.out.println(studentId + "가 등록한 거 List : " + enrollmentList);

        return enrollmentList;
    }

//    public EnrollmentDto[][] getTimeTableById (UUID studentId){
//        List<EnrollmentDto> courseListById = convertToDto(studentId);
//        return scheduleChecker.timeTableMaker(courseListById);
//    }


//    public void deleteClassEnrollmentById ( long enrollmentId){
//        // 수업 등록 정보 조회
//        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 등록 정보가 존재하지 않습니다: " + enrollmentId));
//        CourseOpening courseInfo = enrollment.getCourseOpening(); // 삭제할 강의 정보
//        List<CourseTime> times = courseInfo.getCourseTimes();
//
//        int startTime = 0; // 시작 시간
//        int endTime = 0;   // 종료 시간
//        DayOfWeek day = null;       // 요일
//
//        for (CourseTime c : times) {
//            startTime = c.getStartTime().getHour();
//            endTime = c.getEndTime().getHour();
//            day = c.getDayOfWeek();
//        }
//
//
//        // 시간과 요일 인덱스 계산
//        Integer startPeriodIndex = scheduleChecker.periods.get(startTime);
//        Integer dayIndex = scheduleChecker.days.get(day.toString());
//        int period = endTime - startTime;
//
//        EnrollmentDto[][] deleteTargetTable = getTimeTableById(enrollment.getStudent().getStudentId());
//
//        // 강의 삭제
//        for (int i = 0; i < (period); i++) {
//            if (deleteTargetTable[startPeriodIndex + i][dayIndex] != null) {
//                deleteTargetTable[startPeriodIndex + i][dayIndex] = null; // 강의 삭제
//            }
//        }
//
//        // DB에서 삭제
//        enrollmentRepository.deleteById(enrollmentId);
//    }
}


