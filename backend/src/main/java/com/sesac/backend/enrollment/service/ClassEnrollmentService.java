package com.sesac.backend.enrollment.service;
import com.sesac.backend.enrollment.domain.classEnrollment.CourseEnrollment;
import com.sesac.backend.enrollment.domain.classEnrollment.ScheduleChecker;
import com.sesac.backend.enrollment.domain.classEnrollment.TimeOverlapException;
import com.sesac.backend.enrollment.domain.tempClasses.Course;
import com.sesac.backend.enrollment.dto.CourseEnrollmentDto;
import com.sesac.backend.enrollment.dto.CourseDto;
import com.sesac.backend.enrollment.repository.ClassesEnrollmentRepository;
import com.sesac.backend.enrollment.repository.ClassesRepository;
import com.sesac.backend.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ClassEnrollmentService {

    @Autowired
    private ClassesEnrollmentRepository classesEnrollmentRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ScheduleChecker scheduleChecker;

    private List<CourseEnrollmentDto> convertToDto(UUID studentId) {
        Set<CourseEnrollment> tmpList = classesEnrollmentRepository.findByStudentId(studentId);
        List<CourseEnrollmentDto> classListById = new ArrayList<>();

        for (CourseEnrollment c : tmpList) {
            Course course = c.getCourse();
            CourseDto classesDto = new CourseDto(
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getMaxEnrollments(),
                    course.getCurrentEnrollments(),
                    course.getDay(),
                    course.getStartTime(),
                    course.getEndTime(),
                    course.getCredit()
            );
            classListById.add(new CourseEnrollmentDto(c.getEnrollmentId(), c.getStudent(), classesDto, c.getCourseName(), c.getEnrollmentDate()));
        }
        return classListById;
    }

    ////////////////////////////// save기능 set ////////////////////////////////////
    public void saveClassEnrollment(CourseEnrollmentDto courseEnrollmentDto) {
        Course courseInfo = classesRepository.findByClassName(courseEnrollmentDto.getCourseName()).orElse(null);

        // 클래스 정보가 있을 때 중복 검사 수행 후 save
        if(courseInfo != null){
            checkMultiClass(courseInfo, courseEnrollmentDto);

            CourseEnrollment entity = new CourseEnrollment(courseEnrollmentDto.getEnrollmentId(), courseEnrollmentDto.getStudent(),
                    courseInfo, courseEnrollmentDto.getCourseName(), courseEnrollmentDto.getEnrollmentDate());
            classesEnrollmentRepository.save(entity);
        }
    }

    // enrollment 생성 시 중복 검사 서비스
    public void checkMultiClass(Course classInfo, CourseEnrollmentDto courseEnrollmentDto) {
        // 학생 id로 이미 있는 enrollment data 목록을 가져와서 existEnrollments에 배당
        UUID enrollStudentId = courseEnrollmentDto.getStudent().getStudentId();
        Set<CourseEnrollment> existEnrollments = classesEnrollmentRepository.findByStudentId(enrollStudentId);

        // 대소비교를 쉽게 하기 위해서 localtime으로 새로운 정보를 변환합니다. 
        LocalTime newStartLocalTime = LocalTime.parse(classInfo.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime newEndLocalTime = LocalTime.parse(classInfo.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
        String newDay = classInfo.getDay();

        for (CourseEnrollment enrollment : existEnrollments) {
            Course existingClass = enrollment.getCourse();
            LocalTime existingStartLocalTime = LocalTime.parse(existingClass.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime existingEndLocalTime = LocalTime.parse(existingClass.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
            String existingDay = existingClass.getDay();

            // 요일이 같을 때만 시간 겹침 체크
            if (newDay.equals(existingDay) && isTimeOverlap(newStartLocalTime, newEndLocalTime, existingStartLocalTime, existingEndLocalTime)) {
                throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다: " + existingClass.getCourseName());
            }
        }
    }

    // 시간 겹침 체크 메서드
    private boolean isTimeOverlap(LocalTime newStartTime, LocalTime newEndTime, LocalTime existingStartTime, LocalTime existingEndTime) {
        return (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<CourseDto> getAllClasses() {
        List<Course> tmpList = classesRepository.findAll();
        List<CourseDto> allClassesList = new ArrayList<>();

        for (Course c : tmpList) {
            allClassesList.add(new CourseDto(
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getMaxEnrollments(),
                    c.getCurrentEnrollments(),
                    c.getDay(),
                    c.getStartTime(),
                    c.getEndTime(),
                    c.getCredit()
            ));
        }
        return allClassesList;
    }

    public List<CourseEnrollmentDto> getEnrolledClassById(UUID studentId) {
        return convertToDto(studentId);
    }

    public CourseEnrollmentDto[][] getTimeTableById(UUID studentId) {
        List<CourseEnrollmentDto> classListById = convertToDto(studentId);
        return scheduleChecker.timeTableMaker(classListById);
    }

    public void deleteClassEnrollmentById(long enrollmentId) {
        // 수업 등록 정보 조회
        CourseEnrollment enrollment = classesEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 등록 정보가 존재하지 않습니다: " + enrollmentId));
        Course course = enrollment.getCourse(); // 삭제할 강의 정보
        String startTime = course.getStartTime(); // 시작 시간
        String endTime = course.getEndTime();     // 종료 시간
        String day = course.getDay();             // 요일

        // 시간과 요일 인덱스 계산
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);
        Integer startPeriodIndex = scheduleChecker.periods.get(startHour);
        Integer dayIndex = scheduleChecker.days.get(day);

        CourseEnrollmentDto[][] deleteTargetTable = getTimeTableById(enrollment.getStudent().getStudentId());

        // 강의 삭제
        for (int i = 0; i < (endHour - startHour); i++) {
            if (deleteTargetTable[startPeriodIndex + i][dayIndex] != null) {
                deleteTargetTable[startPeriodIndex + i][dayIndex] = null; // 강의 삭제
            }
        }
        
        // DB에서 삭제
        classesEnrollmentRepository.deleteById(enrollmentId);
    }
}

