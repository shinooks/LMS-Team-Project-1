package com.sesac.backend.course.service;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.dto.CourseTimeDto;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseTimeRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.CourseTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseTimeService {

    private final CourseTimeRepository courseTimeRepository;
    private final CourseOpeningRepository courseOpeningRepository;

    // 전체 강의 시간 조회
    @Transactional(readOnly = true)
    public List<CourseTimeDto> getAllCourseTimes() {
        return courseTimeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 강의 시간 등록
    public CourseTimeDto createCourseTime(UUID openingId, CourseTimeDto courseTimeDto) {
        // 시간 유효성 검사
        validateTimeRange(courseTimeDto);
        // 시간 중복 검사
        validateTimeConflicts(courseTimeDto);

        // 강의 개설 정보 조회
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("강의 개설 정보를 찾을 수 없습니다."));

        // CourseTime 엔티티 생성 및 저장
        CourseTime courseTime = CourseTime.builder()
                .courseOpening(courseOpening)
                .dayOfWeek(courseTimeDto.getDayOfWeek())
                .startTime(courseTimeDto.getStartTime())
                .endTime(courseTimeDto.getEndTime())
                .classroom(courseTimeDto.getClassroom())
                .build();

        log.info("Creating course time for opening id: {}", openingId);
        return convertToDto(courseTimeRepository.save(courseTime));
    }

    // 강의 개설 id로 조회
    @Transactional(readOnly = true)
    public List<CourseTimeDto> getCourseTimesByOpeningId(UUID openingId) {
        List<CourseTime> courseTimes = courseTimeRepository.findByCourseOpeningOpeningId(openingId);
        courseTimes.forEach(ct -> ct.getCourseOpening().getOpeningId());  // LAZY 로딩 처리
        return courseTimes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 특정 강의 시간 조회
    @Transactional(readOnly = true)
    public CourseTimeDto getCourseTime(UUID timeId) {
        CourseTime courseTime = courseTimeRepository.findById(timeId)
                .orElseThrow(() -> new RuntimeException("강의 시간을 찾을 수 없습니다."));
        return convertToDto(courseTime);
    }

    // 특정 요일의 모든 강의 시간 조회
    @Transactional(readOnly = true)
    public List<CourseTimeDto> getCourseTimesByDay(DayOfWeek dayOfWeek) {
        return courseTimeRepository.findByDayOfWeek(dayOfWeek).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 특정 강의실의 시간표 조회
    @Transactional(readOnly = true)
    public List<CourseTimeDto> getCourseTimesByClassroom(String classroom, DayOfWeek dayOfWeek) {
        if (dayOfWeek != null) {
            return courseTimeRepository.findByClassroomAndDayOfWeek(classroom, dayOfWeek).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        return courseTimeRepository.findByClassroom(classroom).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 강의 시간 수정
    public CourseTimeDto updateCourseTime(UUID timeId, CourseTimeDto courseTimeDto) {
        // 시간 유효성 검사
        validateTimeRange(courseTimeDto);
        // 시간 중복 검사 (자기 자신 제외)
        validateTimeConflicts(courseTimeDto, timeId);

        CourseTime courseTime = courseTimeRepository.findById(timeId)
                .orElseThrow(() -> new RuntimeException("강의 시간을 찾을 수 없습니다."));

        // 내용 업데이트
        courseTime.setDayOfWeek(courseTimeDto.getDayOfWeek());
        courseTime.setStartTime(courseTimeDto.getStartTime());
        courseTime.setEndTime(courseTimeDto.getEndTime());
        courseTime.setClassroom(courseTimeDto.getClassroom());

        log.info("Updating course time: {}", timeId);
        return convertToDto(courseTimeRepository.save(courseTime));
    }

    // 강의 시간 삭제
    public void deleteCourseTime(UUID timeId) {
        courseTimeRepository.findById(timeId)
                .orElseThrow(() -> new RuntimeException("강의 시간을 찾을 수 없습니다."));
        courseTimeRepository.deleteById(timeId);
        log.info("Deleted course time: {}", timeId);
    }

    // 시간 범위 유효성 검사
    private void validateTimeRange(CourseTimeDto dto) {
        if (dto.getStartTime() != null && dto.getEndTime() != null &&
                !dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 늦어야 합니다.");
        }
    }

    // 시간 중복 검사 (신규 등록용)
    private void validateTimeConflicts(CourseTimeDto newTime) {
        validateTimeConflicts(newTime, null);
    }

    // 시간 중복 검사
    private void validateTimeConflicts(CourseTimeDto newTime, UUID excludeTimeId) {
        validateClassroomTimeConflicts(newTime, excludeTimeId);
        validateProfessorTimeConflicts(newTime, excludeTimeId);
    }

    // 강의실 시간 중복 검사
    private void validateClassroomTimeConflicts(CourseTimeDto newTime, UUID excludeTimeId) {
        List<CourseTime> classroomTimes = courseTimeRepository
                .findByClassroomAndDayOfWeek(newTime.getClassroom(), newTime.getDayOfWeek());

        for (CourseTime existingTime : classroomTimes) {
            if (shouldSkipTimeCheck(existingTime, excludeTimeId)) continue;

            if (isTimeOverlapping(newTime.getStartTime(), newTime.getEndTime(),
                    existingTime.getStartTime(), existingTime.getEndTime())) {
                throw new IllegalStateException(String.format(
                        "강의실 %s는 %s요일 %s~%s에 이미 다른 강의가 있습니다.",
                        newTime.getClassroom(),
                        newTime.getDayOfWeek().getDescription(),
                        existingTime.getStartTime(),
                        existingTime.getEndTime()));
            }
        }
    }

    // 교수 시간 중복 검사
    private void validateProfessorTimeConflicts(CourseTimeDto newTime, UUID excludeTimeId) {
        CourseOpening courseOpening = courseOpeningRepository.findById(newTime.getOpeningId())
                .orElseThrow(() -> new RuntimeException("강의 개설 정보를 찾을 수 없습니다."));

        List<CourseTime> professorTimes = courseTimeRepository.findAll().stream()
                .filter(time -> time.getCourseOpening().getProfessor().getProfessorId().equals(courseOpening.getProfessor().getProfessorId()))
                .filter(time -> time.getDayOfWeek() == newTime.getDayOfWeek())
                .collect(Collectors.toList());

        for (CourseTime existingTime : professorTimes) {
            if (shouldSkipTimeCheck(existingTime, excludeTimeId)) continue;

            if (isTimeOverlapping(newTime.getStartTime(), newTime.getEndTime(),
                    existingTime.getStartTime(), existingTime.getEndTime())) {
                throw new IllegalStateException(String.format(
                        "해당 교수는 %s요일 %s~%s에 이미 다른 강의가 있습니다.",
                        newTime.getDayOfWeek().getDescription(),
                        existingTime.getStartTime(),
                        existingTime.getEndTime()));
            }
        }
    }

    // 중복 검사 제외 여부 확인
    private boolean shouldSkipTimeCheck(CourseTime existingTime, UUID excludeTimeId) {
        return excludeTimeId != null && existingTime.getTimeId().equals(excludeTimeId);
    }

    // 시간 중복 확인
    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !end1.isBefore(start2) && !end2.isBefore(start1);
    }

    // Entity를 DTO로 변환
    private CourseTimeDto convertToDto(CourseTime courseTime) {
        return CourseTimeDto.builder()
                .timeId(courseTime.getTimeId())
                .openingId(courseTime.getCourseOpening().getOpeningId())
                .dayOfWeek(courseTime.getDayOfWeek())
                .startTime(courseTime.getStartTime())
                .endTime(courseTime.getEndTime())
                .classroom(courseTime.getClassroom())
                .build();
    }

}