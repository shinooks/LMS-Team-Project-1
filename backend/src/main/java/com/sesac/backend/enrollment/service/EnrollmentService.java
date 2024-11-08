package com.sesac.backend.enrollment.service;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.dto.CourseTimeDto;
import com.sesac.backend.course.dto.SyllabusDto;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseRepository;
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
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ScheduleChecker scheduleChecker;

    @Autowired
    private CourseOpeningRepository courseOpeningRepository;

    @Autowired
    private EnrollStudentRepository enrollStudentRepository;
    @Autowired
    private CourseRepository courseRepository;


    private List<EnrollmentDto> convertToDto(UserAuthentication studentId) {
        Student student = enrollStudentRepository.findByUser(studentId).orElse(null);
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
    public void saveClassEnrollment(UserAuthentication studentId, UUID openingId) {
        // className으로 CourseOpening의 정보를 조회
        CourseOpening openingCourseInfo = courseOpeningRepository.findById(openingId).orElse(null);

        Student student = enrollStudentRepository.findByUser(studentId).orElse(null);

        // 클래스 정보가 있을 때 중복 검사 수행 후 save
        if (openingCourseInfo != null) {
            // 정진욱 : courseEnrollmentDto를 보내는 이유 : dto.studentId로 관심과목에 등록한 과목들을 조회하기 위해서
            checkMultiClass(openingCourseInfo, student);

            Enrollment entity = new Enrollment(student, openingCourseInfo,
                    openingCourseInfo.getCourse().getCourseName());
            enrollmentRepository.save(entity);
        }
    }

    // enrollment 생성 시 중복 검사 서비스
    public void checkMultiClass(CourseOpening openingCourseInfo, Student student) {
        // 관심강의를 등록하려는 학생 id(dto.studentId)로 이미 있는
        // 동일 학생의 enrollment data 목록을 가져와서 existEnrollments에 배당
        Set<Enrollment> existEnrollments = enrollmentRepository.findByStudent(student);

        // 등록할 강의의 시간 정보를 가져옴
        List<CourseTime> openingCourseTimeInfo = openingCourseInfo.getCourseTimes();

        // 기존 관심등록한 강의 목록을 리스트에 담음
        List<CourseTime> existingCourseTimeInfo = new ArrayList<>();

        for (Enrollment enrollment : existEnrollments) {
            existingCourseTimeInfo.addAll(enrollment.getCourseOpening().getCourseTimes());
        }

        // 성능 향상을 위해 관심등록 되어있는 데이터와 등록할 데이터를 하나의 List에 병합
        List<CourseTime> allCourseTimesForMatching = new ArrayList<>();
        allCourseTimesForMatching.addAll(openingCourseTimeInfo);
        allCourseTimesForMatching.addAll(existingCourseTimeInfo);

        // 시간정보 정렬(요일과 시작 시간 기준)
        allCourseTimesForMatching.sort(Comparator.comparing(CourseTime::getDayOfWeek)
                .thenComparing(CourseTime::getStartTime));

        // 겹치는 시간 체크
        for (int i = 1; i < allCourseTimesForMatching.size(); i++) {
            CourseTime previous = allCourseTimesForMatching.get(i - 1);
            CourseTime current = allCourseTimesForMatching.get(i);

            // 요일이 같고 시간 겹침 체크
            if (previous.getDayOfWeek().equals(current.getDayOfWeek()) &&
                    isTimeOverlap(previous.getStartTime(), previous.getEndTime(), current.getStartTime(), current.getEndTime())) {
                throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다: " + current.getCourseOpening().getCourse().getCourseName());
            }
        }
    }

    // 시간 겹침 체크 메서드
    private boolean isTimeOverlap(LocalTime newStartTime, LocalTime newEndTime, LocalTime existingStartTime, LocalTime existingEndTime) {
        return (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<CourseOpeningDto> getAllClasses() {
        List<CourseOpening> tmpList = courseOpeningRepository.findAll();


        List<CourseOpeningDto> allClassesList = new ArrayList<>();

        for (CourseOpening c : tmpList) {

            List<CourseTimeDto> courseTimeDtos = c.getCourseTimes().stream()
                    .map(ctd -> new CourseTimeDto(
                            ctd.getTimeId(),
                            ctd.getCourseOpening().getOpeningId(),
                            ctd.getDayOfWeek(),
                            ctd.getStartTime(),
                            ctd.getEndTime(),
                            ctd.getClassroom()))
                    .collect(Collectors.toList());

            SyllabusDto syllabusDto = new SyllabusDto(
                    c.getSyllabus().getSyllabusId(),
                    c.getSyllabus().getLearningObjectives(),
                    c.getSyllabus().getWeeklyPlan(),
                    c.getSyllabus().getEvaluationMethod(),
                    c.getSyllabus().getTextbooks()
            );

            allClassesList.add(new CourseOpeningDto(
                    c.getOpeningId(),
                    c.getCourse().getCourseId(),
                    c.getProfessorId(),
                    c.getSemester(),
                    c.getYear(),
                    c.getMaxStudents(),
                    c.getCurrentStudents(),
                    c.getStatus(),
                    courseTimeDtos,
                    syllabusDto
            ));
        }
        return allClassesList;

    }

    public List<EnrollmentDto> getEnrolledClassById(UserAuthentication studentId) {
        return convertToDto(studentId);
    }

    public EnrollmentDto[][] getTimeTableById(UserAuthentication studentId) {
        List<EnrollmentDto> courseListById = convertToDto(studentId);
        return scheduleChecker.timeTableMaker(courseListById);
    }

    public void deleteClassEnrollmentById(long enrollmentId) {
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

        EnrollmentDto[][] deleteTargetTable = getTimeTableById(enrollment.getStudent().getUser());

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

