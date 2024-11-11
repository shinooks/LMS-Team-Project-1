package com.sesac.backend.enrollment.service;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.course.repository.CourseTimeRepository;
import com.sesac.backend.enrollment.domain.ScheduleChecker;
import com.sesac.backend.enrollment.domain.exceptionControl.TimeOverlapException;
import com.sesac.backend.enrollment.dto.EnrollmentDto;
import com.sesac.backend.enrollment.repository.EnrollStudentRepository;
import com.sesac.backend.enrollment.repository.EnrollmentRepository;
import com.sesac.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
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
    private EnrollStudentRepository enrollStudentRepository;
    @Autowired
    private CourseRepository courseRepository;


    private List<EnrollmentDto> convertToDto(UUID studentId) {
        Student student = enrollStudentRepository.findByStudentId(studentId).orElse(null);
        Set<Enrollment> tmpList = enrollmentRepository.findByStudent(student);
        List<EnrollmentDto> classListById = new ArrayList<>();

        for (Enrollment c : tmpList) {
            classListById.add(new EnrollmentDto(
                    c.getEnrollmentId(),
                    c.getStudent(),
                    c.getCourseOpening(),
                    c.getCourseName(),
                    c.getEnrollmentDate()
            ));
        }
        return classListById;
    }

    ////////////////////////////// save기능 set ////////////////////////////////////
    public void saveClassEnrollment(UUID studentId, String courseCode) {
        // classCode로 Course의 정보를 조회
        Course course = courseRepository.findCourseByCourseCode(courseCode);

        if( course == null ) {
            throw new IllegalArgumentException("Course not found");
        }

        // Student 객체 조회
        Student student = enrollStudentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // 강의 개설 정보 조회
        List<CourseOpening> courseOpenings = courseOpeningRepository.findByCourse(course);

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
                throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다." + course.getCourseName());
            }
        }

        // 중복검사를 통과하면 새로운 등록강의 생성
        Enrollment enrollment = new Enrollment(
                student, openingCourseInfo, openingCourseInfo.getCourse().getCourseName()
        );

        enrollmentRepository.save(enrollment);
    }




//    public void saveClassEnrollment(UUID studentId, String courseCode) {
//        // classCode로 Course의 정보를 조회
//        Course course = courseRepository.findCourseByCourseCode(courseCode);
//        // System.out.println("강의 정보 : " + course.getCourseId());
//        // 강의 정보 : 10cdef34-5678-90ab-cdef-123456789abc
//
//        // 정진욱
//        // Course 정보가 있을 때 중복 검사 수행
//        if (course != null) {
//
//        }
//
//
//        // Student studentId로 enrollment entity 조회/ 관심강의에 등록하려는 학생이 이미 등록한 강의 목록
//        List<Enrollment> studentEnrollments = enrollmentRepository.findByStudent_StudentId(studentId);
//
//
//
//        Enrollment entity = new Enrollment(student, openingCourseInfo,
//                openingCourseInfo.getCourse().getCourseName());
//        enrollmentRepository.save(entity);
//    }
//
//    // enrollment 생성 시 중복 검사 서비스
//    public void checkMultiClass(CourseOpening openingCourseInfo, Student student) {
//        // 관심강의를 등록하려는 학생 id(dto.studentId)로 이미 있는
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println("학생정보 : " + student);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//
//        Set<Enrollment> existEnrollments = enrollmentRepository.findByStudent(studentId);
//
//        // 등록할 강의의 시간 정보를 가져옴
//        List<CourseTime> openingCourseTimeInfo = openingCourseInfo.getCourseTimes();
//
//        // 기존 관심등록한 강의 목록을 리스트에 담음
//        List<CourseTime> existingCourseTimeInfo = new ArrayList<>();
//
//        for (Enrollment enrollment : existEnrollments) {
//            existingCourseTimeInfo.addAll(enrollment.getCourseOpening().getCourseTimes());
//        }
//
//        // 성능 향상을 위해 관심등록 되어있는 데이터와 등록할 데이터를 하나의 List에 병합
//        List<CourseTime> allCourseTimesForMatching = new ArrayList<>();
//        allCourseTimesForMatching.addAll(openingCourseTimeInfo);
//        allCourseTimesForMatching.addAll(existingCourseTimeInfo);
//
//        // 시간정보 정렬(요일과 시작 시간 기준)
//        allCourseTimesForMatching.sort(Comparator.comparing(CourseTime::getDayOfWeek)
//                .thenComparing(CourseTime::getStartTime));
//
//        // 겹치는 시간 체크
//        for (int i = 1; i < allCourseTimesForMatching.size(); i++) {
//            CourseTime previous = allCourseTimesForMatching.get(i - 1);
//            CourseTime current = allCourseTimesForMatching.get(i);
//
//            // 요일이 같고 시간 겹침 체크
//            if (previous.getDayOfWeek().equals(current.getDayOfWeek()) &&
//                    isTimeOverlap(previous.getStartTime(), previous.getEndTime(), current.getStartTime(), current.getEndTime())) {
//                throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다: " + current.getCourseOpening().getCourse().getCourseName());
//            }
//        }
//    }
//
//    // 시간 겹침 체크 메서드
//    private boolean isTimeOverlap(LocalTime newStartTime, LocalTime newEndTime, LocalTime existingStartTime, LocalTime existingEndTime) {
//        return (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime));
//    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public List<CourseOpeningDto> getAllClasses() {
//
////        List<CourseOpening> tmpList = courseOpeningRepository.findAll();
////
////        List<UUID> courseOpeningIds = new ArrayList<>();
////
////        for (CourseOpening id : tmpList) {
////            courseOpeningIds.add(id.getOpeningId());
////        }
////
////        List<CourseOpening> courseList = courseOpeningRepository.findById();
//
//
////        List<CourseOpeningDto> allClassesList = new ArrayList<>();
////
////        for (CourseOpening c : tmpList) {
////
////            List<CourseTimeDto> courseTimeDtos = c.getCourseTimes().stream()
////                    .map(ctd -> new CourseTimeDto(
////                            ctd.getTimeId(),
////                            ctd.getCourseOpening().getOpeningId(),
////                            ctd.getDayOfWeek(),
////                            ctd.getStartTime(),
////                            ctd.getEndTime(),
////                            ctd.getClassroom()))
////                    .collect(Collectors.toList());
////
////            SyllabusDto syllabusDto = new SyllabusDto(
////                    c.getSyllabus().getSyllabusId(),
////                    c.getSyllabus().getLearningObjectives(),
////                    c.getSyllabus().getWeeklyPlan(),
////                    c.getSyllabus().getEvaluationMethod(),
////                    c.getSyllabus().getTextbooks()
////            );
////
////            allClassesList.add(new CourseOpeningDto(
////                    c.getOpeningId(),
////                    c.getCourse().getCourseId(),
////                    c.getProfessorId(),
////                    c.getSemester(),
////                    c.getYear(),
////                    c.getMaxStudents(),
////                    c.getCurrentStudents(),
////                    c.getStatus(),
////                    courseTimeDtos,
////                    syllabusDto
////            ));
////        }
//
//        // 강의에 대한 정보들을 담기 위해 각 정보가 필요한 list 생성
//
//
//        //return allClassesList;
//        return null;
//    }

    public List<Object> getAllClasses() {
        List<CourseOpening> tmpList = courseOpeningRepository.findAll();

        List<Object> allCoursesList = new ArrayList<>();

        for (CourseOpening c : tmpList) {

            List<Object> courseInfo = new ArrayList<>();

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

            courseInfo.add(courseCode);
            courseInfo.add(courseName);
            courseInfo.add(credit);
            courseInfo.add(day);
            courseInfo.add(startTime);
            courseInfo.add(endTime);

            allCoursesList.add(courseInfo);
        }

        return allCoursesList;
    }


    //public List<EnrollmentDto> getEnrolledClassById (UserAuthentication studentId){
//    public List<EnrollmentDto> getEnrolledClassById (UUID studentId){
//        return convertToDto(studentId);
//    }


    public EnrollmentDto[][] getTimeTableById (UUID studentId){
        List<EnrollmentDto> courseListById = convertToDto(studentId);
        return scheduleChecker.timeTableMaker(courseListById);
    }


    public void deleteClassEnrollmentById ( long enrollmentId){
        // 수업 등록 정보 조회
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 등록 정보가 존재하지 않습니다: " + enrollmentId));
        CourseOpening courseInfo = enrollment.getCourseOpening(); // 삭제할 강의 정보
        List<CourseTime> times = courseInfo.getCourseTimes();

        int startTime = 0; // 시작 시간
        int endTime = 0;   // 종료 시간
        DayOfWeek day = null;       // 요일

        for (CourseTime c : times) {
            startTime = c.getStartTime().getHour();
            endTime = c.getEndTime().getHour();
            day = c.getDayOfWeek();
        }


        // 시간과 요일 인덱스 계산
        Integer startPeriodIndex = scheduleChecker.periods.get(startTime);
        Integer dayIndex = scheduleChecker.days.get(day.toString());
        int period = endTime - startTime;

        EnrollmentDto[][] deleteTargetTable = getTimeTableById(enrollment.getStudent().getStudentId());

        // 강의 삭제
        for (int i = 0; i < (period); i++) {
            if (deleteTargetTable[startPeriodIndex + i][dayIndex] != null) {
                deleteTargetTable[startPeriodIndex + i][dayIndex] = null; // 강의 삭제
            }
        }

        // DB에서 삭제
        enrollmentRepository.deleteById(enrollmentId);
    }
}


