package com.sesac.backend.enrollment.service;
ss
import com.sesac.backend.enrollment.domain.classEnrollment.ClassEnrollment;
import com.sesac.backend.enrollment.domain.classEnrollment.ScheduleChecker;
import com.sesac.backend.enrollment.domain.classEnrollment.TimeOverlapException;
import com.sesac.backend.enrollment.domain.tempClasses.Classes;
import com.sesac.backend.enrollment.dto.ClassEnrollmentDto;
import com.sesac.backend.enrollment.dto.ClassesDto;
import com.sesac.backend.enrollment.repository.ClassesEnrollmentRepository;
import com.sesac.backend.enrollment.repository.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    private List<ClassEnrollmentDto> convertToDto(String id) {
        Set<ClassEnrollment> tmpList = classesEnrollmentRepository.findByStudentId(id);
        List<ClassEnrollmentDto> classListById = new ArrayList<>();

        for (ClassEnrollment c : tmpList) {
            Classes classes = c.getClasses();
            ClassesDto classesDto = new ClassesDto(
                    classes.getClassId(),
                    classes.getClassName(),
                    classes.getMaxEnrollments(),
                    classes.getCurrentEnrollments(),
                    classes.getDay(),
                    classes.getStartTime(),
                    classes.getEndTime(),
                    classes.getCredit()
            );
            classListById.add(new ClassEnrollmentDto(c.getEnrollmentId(), c.getStudentId(), classesDto, c.getClassName()));
        }
        return classListById;
    }

    ////////////////////////////// save기능 set ////////////////////////////////////
    public void saveClassEnrollment(ClassEnrollmentDto classEnrollmentDto) {
        Classes classInfo = classesRepository.findByClassName(classEnrollmentDto.getClassName()).orElse(null);

        // 클래스 정보가 있을 때 중복 검사 수행 후 save
        if(classInfo != null){
            checkMultiClass(classInfo, classEnrollmentDto);

            ClassEnrollment entity = new ClassEnrollment(classEnrollmentDto.getEnrollmentId(), classEnrollmentDto.getStudentId(),
                    classInfo, classEnrollmentDto.getClassName());
            classesEnrollmentRepository.save(entity);
        }
    }

    // enrollment 생성 시 중복 검사 서비스
    public void checkMultiClass(Classes classInfo, ClassEnrollmentDto classEnrollmentDto) {
        // 학생 id로 이미 있는 enrollment data 목록을 가져와서 existEnrollments에 배당
        String enrollStudentId = classEnrollmentDto.getStudentId();
        Set<ClassEnrollment> existEnrollments = classesEnrollmentRepository.findByStudentId(enrollStudentId);

        // 대소비교를 쉽게 하기 위해서 localtime으로 새로운 정보를 변환합니다. 
        LocalTime newStartLocalTime = LocalTime.parse(classInfo.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime newEndLocalTime = LocalTime.parse(classInfo.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
        String newDay = classInfo.getDay();

        for (ClassEnrollment enrollment : existEnrollments) {
            Classes existingClass = enrollment.getClasses();
            LocalTime existingStartLocalTime = LocalTime.parse(existingClass.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime existingEndLocalTime = LocalTime.parse(existingClass.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
            String existingDay = existingClass.getDay();

            // 요일이 같을 때만 시간 겹침 체크
            if (newDay.equals(existingDay) && isTimeOverlap(newStartLocalTime, newEndLocalTime, existingStartLocalTime, existingEndLocalTime)) {
                throw new TimeOverlapException("시간이 겹치는 강의가 이미 등록되어 있습니다: " + existingClass.getClassName());
            }
        }
    }

    // 시간 겹침 체크 메서드
    private boolean isTimeOverlap(LocalTime newStartTime, LocalTime newEndTime, LocalTime existingStartTime, LocalTime existingEndTime) {
        return (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<ClassesDto> getAllClasses() {
        List<Classes> tmpList = classesRepository.findAll();
        List<ClassesDto> allClassesList = new ArrayList<>();

        for (Classes c : tmpList) {
            allClassesList.add(new ClassesDto(
                    c.getClassId(),
                    c.getClassName(),
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

    public List<ClassEnrollmentDto> getEnrolledClassById(String id) {
        return convertToDto(id);
    }

    public ClassEnrollmentDto[][] getTimeTableById(String id) {
        List<ClassEnrollmentDto> classListById = convertToDto(id);
        return scheduleChecker.timeTableMaker(classListById);
    }

    public void deleteClassEnrollmentById(long enrollmentId) {
        // 수업 등록 정보 조회
        ClassEnrollment enrollment = classesEnrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 등록 정보가 존재하지 않습니다: " + enrollmentId));
        Classes classes = enrollment.getClasses(); // 삭제할 강의 정보
        String startTime = classes.getStartTime(); // 시작 시간
        String endTime = classes.getEndTime();     // 종료 시간
        String day = classes.getDay();             // 요일

        // 시간과 요일 인덱스 계산
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);
        Integer startPeriodIndex = scheduleChecker.periods.get(startHour);
        Integer dayIndex = scheduleChecker.days.get(day);

        ClassEnrollmentDto[][] deleteTargetTable = getTimeTableById(enrollment.getStudentId());

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

