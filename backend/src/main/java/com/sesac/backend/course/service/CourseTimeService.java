package com.sesac.backend.course.service;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.dto.CompleteUpdateRequestDto;
import com.sesac.backend.course.dto.CourseOpeningDto;
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

    @Transactional
    public void updateCourseOpeningAndTimes(UUID openingId, CompleteUpdateRequestDto requestDto) {
        // 1. 강의 개설 정보 업데이트
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("강의 개설 정보를 찾을 수 없습니다."));

        // 강의 개설 정보 업데이트
        CourseOpeningDto openingDto = requestDto.getCourseOpening();
        courseOpening.updateFromDto(openingDto);
        courseOpeningRepository.save(courseOpening);

        // 2. 새로운 강의 시간들의 중복 검사
        List<CourseTimeDto> timesDtos = requestDto.getCourseTimes();
        validateTimesWithinOpening(timesDtos);

        // 3. 기존 강의 시간 정보 모두 삭제
        courseTimeRepository.deleteAllByCourseOpeningOpeningId(openingId);

        // 4. 새로운 강의 시간 정보 추가
        for (CourseTimeDto timeDto : timesDtos) {
            CourseTime newTime = CourseTime.builder()
                    .courseOpening(courseOpening)
                    .dayOfWeek(timeDto.getDayOfWeek())
                    .startTime(timeDto.getStartTime())
                    .endTime(timeDto.getEndTime())
                    .classroom(timeDto.getClassroom())
                    .build();
            courseTimeRepository.save(newTime);
        }
    }

    // 같은 강의 개설 내의 시간 중복 검사를 위한 새로운 메소드
    private void validateTimesWithinOpening(List<CourseTimeDto> times) {
        for (int i = 0; i < times.size(); i++) {
            CourseTimeDto time1 = times.get(i);
            for (int j = i + 1; j < times.size(); j++) {
                CourseTimeDto time2 = times.get(j);

                // 같은 요일인 경우에만 검사
                if (time1.getDayOfWeek() == time2.getDayOfWeek()) {
                    // 같은 강의실이고 시간이 겹치는 경우
                    if (time1.getClassroom().equals(time2.getClassroom()) &&
                            isTimeOverlapping(time1.getStartTime(), time1.getEndTime(),
                                    time2.getStartTime(), time2.getEndTime())) {
                        throw new IllegalStateException(String.format(
                                "%s요일 %s~%s에 강의실 %s가 중복됩니다.",
                                time1.getDayOfWeek().getDescription(),
                                time1.getStartTime(),
                                time1.getEndTime(),
                                time1.getClassroom()
                        ));
                    }

                    // 같은 시간대 중복 검사
                    if (isTimeOverlapping(time1.getStartTime(), time1.getEndTime(),
                            time2.getStartTime(), time2.getEndTime())) {
                        throw new IllegalStateException(String.format(
                                "%s요일 %s~%s 시간이 중복됩니다.",
                                time1.getDayOfWeek().getDescription(),
                                time1.getStartTime(),
                                time1.getEndTime()
                        ));
                    }
                }
            }
        }
    }


    // 전체 강의 시간 조회 (기존 코드 유지)
    @Transactional(readOnly = true)
    public List<CourseTimeDto> getAllCourseTimes() {
        return courseTimeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 강의 시간 등록 (기존 코드 유지)
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


    @Transactional(readOnly = true)
    public List<CourseTimeDto> getCourseTimesByOpeningId(UUID openingId) {
        List<CourseTime> courseTimes = courseTimeRepository.findByCourseOpeningOpeningId(openingId);
        courseTimes.forEach(ct -> ct.getCourseOpening().getOpeningId());  // LAZY 로딩 처리
        return courseTimes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseTimeDto getCourseTime(UUID timeId) {
        CourseTime courseTime = courseTimeRepository.findById(timeId)
                .orElseThrow(() -> new RuntimeException("강의 시간을 찾을 수 없습니다."));
        return convertToDto(courseTime);
    }

    @Transactional(readOnly = true)
    public List<CourseTimeDto> getCourseTimesByDay(DayOfWeek dayOfWeek) {
        return courseTimeRepository.findByDayOfWeek(dayOfWeek).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

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

    // 강의 시간 수정 (기존 코드 유지)
    public CourseTimeDto updateCourseTime(UUID timeId, CourseTimeDto courseTimeDto) {
        validateTimeRange(courseTimeDto);
        validateTimeConflicts(courseTimeDto, timeId);

        CourseTime courseTime = courseTimeRepository.findById(timeId)
                .orElseThrow(() -> new RuntimeException("강의 시간을 찾을 수 없습니다."));

        courseTime.setDayOfWeek(courseTimeDto.getDayOfWeek());
        courseTime.setStartTime(courseTimeDto.getStartTime());
        courseTime.setEndTime(courseTimeDto.getEndTime());
        courseTime.setClassroom(courseTimeDto.getClassroom());

        log.info("Updating course time: {}", timeId);
        return convertToDto(courseTimeRepository.save(courseTime));
    }

    // 강의 시간 삭제 (기존 코드 유지)
    public void deleteCourseTime(UUID timeId) {
        courseTimeRepository.findById(timeId)
                .orElseThrow(() -> new RuntimeException("강의 시간을 찾을 수 없습니다."));
        courseTimeRepository.deleteById(timeId);
        log.info("Deleted course time: {}", timeId);
    }

    // 시간 범위 유효성 검사 (기존 코드 유지)
    private void validateTimeRange(CourseTimeDto dto) {
        if (dto.getStartTime() != null && dto.getEndTime() != null &&
                !dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 늦어야 합니다.");
        }
    }

    // 시간 중복 검사 (신규 등록용) (기존 코드 유지)
    private void validateTimeConflicts(CourseTimeDto newTime) {
        validateTimeConflicts(newTime, null);
    }

    // 시간 중복 검사 (수정된 부분)
    private void validateTimeConflicts(CourseTimeDto newTime, UUID excludeTimeId) {
        CourseOpening newOpening = courseOpeningRepository.findById(newTime.getOpeningId())
                .orElseThrow(() -> new RuntimeException("강의 개설 정보를 찾을 수 없습니다."));

        validateClassroomTimeConflicts(newTime, excludeTimeId, newOpening);
        validateProfessorTimeConflicts(newTime, excludeTimeId, newOpening);
    }

    // 강의실 시간 중복 검사 (수정된 부분)
    private void validateClassroomTimeConflicts(CourseTimeDto newTime, UUID excludeTimeId, CourseOpening newOpening) {
        List<CourseTime> classroomTimes = courseTimeRepository
                .findByClassroomAndDayOfWeek(newTime.getClassroom(), newTime.getDayOfWeek());

        for (CourseTime existingTime : classroomTimes) {
            if (shouldSkipTimeCheck(existingTime, excludeTimeId)) continue;

            // 같은 학기인 경우에만 검사
            if (!isSameTerm(existingTime.getCourseOpening(), newOpening)) continue;

            if (isTimeOverlapping(newTime.getStartTime(), newTime.getEndTime(),
                    existingTime.getStartTime(), existingTime.getEndTime())) {
                throw new IllegalStateException(String.format(
                        "%d년 %s학기 강의실 %s는 %s요일 %s~%s에 이미 다른 강의가 있습니다.",
                        newOpening.getYear(),
                        newOpening.getSemester(),
                        newTime.getClassroom(),
                        newTime.getDayOfWeek().getDescription(),
                        existingTime.getStartTime(),
                        existingTime.getEndTime()));
            }
        }
    }

    // 교수 시간 중복 검사 (수정된 부분)
    private void validateProfessorTimeConflicts(CourseTimeDto newTime, UUID excludeTimeId, CourseOpening newOpening) {
        List<CourseTime> professorTimes = courseTimeRepository.findAll().stream()
                .filter(time -> time.getCourseOpening().getProfessor().equals(newOpening.getProfessor()))
                .filter(time -> time.getDayOfWeek() == newTime.getDayOfWeek())
                .collect(Collectors.toList());

        for (CourseTime existingTime : professorTimes) {
            if (shouldSkipTimeCheck(existingTime, excludeTimeId)) continue;

            // 같은 학기인 경우에만 검사
            if (!isSameTerm(existingTime.getCourseOpening(), newOpening)) continue;

            if (isTimeOverlapping(newTime.getStartTime(), newTime.getEndTime(),
                    existingTime.getStartTime(), existingTime.getEndTime())) {
                throw new IllegalStateException(String.format(
                        "%d년 %s학기 %s요일 %s~%s에 해당 교수는 이미 다른 강의가 있습니다.",
                        newOpening.getYear(),
                        newOpening.getSemester(),
                        newTime.getDayOfWeek().getDescription(),
                        existingTime.getStartTime(),
                        existingTime.getEndTime()));
            }
        }
    }

    // 중복 검사 제외 여부 확인 (기존 코드 유지)
    private boolean shouldSkipTimeCheck(CourseTime existingTime, UUID excludeTimeId) {
        return excludeTimeId != null && existingTime.getTimeId().equals(excludeTimeId);
    }

    // 시간 중복 확인 (기존 코드 유지)
    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !end1.isBefore(start2) && !end2.isBefore(start1);
    }

    // 같은 학기 여부 확인 (새로 추가된 메서드)
    private boolean isSameTerm(CourseOpening opening1, CourseOpening opening2) {
        return opening1.getYear().equals(opening2.getYear()) &&
                opening1.getSemester().equals(opening2.getSemester());
    }

    // Entity를 DTO로 변환 (기존 코드 유지)
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