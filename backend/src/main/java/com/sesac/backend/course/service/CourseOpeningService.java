package com.sesac.backend.course.service;

import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.dto.CourseTimeDto;
import com.sesac.backend.course.dto.SyllabusDto;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 강의 개설 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseOpeningService {

    private final CourseOpeningRepository courseOpeningRepository;
    private final CourseRepository courseRepository;

    /**
     * 새로운 강의 개설을 생성하는 메서드
     * @param courseOpeningDto 강의 개설 정보를 담은 DTO
     * @return 생성된 강의 개설 정보
     * @throws RuntimeException 강의를 찾을 수 없는 경우 발생
     */
    public CourseOpeningDto createCourseOpening(CourseOpeningDto courseOpeningDto) {
        // 강의 조회 (없으면 예외 발생)
        Course course = courseRepository.findById(courseOpeningDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));

        // CourseOpening 엔티티 생성
        CourseOpening courseOpening = CourseOpening.builder()
                .course(course)                           // 강의 정보
                .professorId(courseOpeningDto.getProfessorId())  // 담당 교수
                .semester(courseOpeningDto.getSemester())        // 학기
                .year(courseOpeningDto.getYear())               // 연도
                .maxStudents(courseOpeningDto.getMaxStudents()) // 최대 수강 인원
                .status(courseOpeningDto.getStatus())           // 강의 상태
                .build();

        // 데이터베이스에 저장
        CourseOpening savedCourseOpening = courseOpeningRepository.save(courseOpening);
        log.info("Created course opening: {}", savedCourseOpening);

        // DTO로 변환하여 반환
        return convertToDto(savedCourseOpening);
    }

    /**
     * 특정 ID의 강의 개설을 조회하는 메서드
     * @param openingId 조회할 강의 개설 ID
     * @return 해당 ID의 강의 개설 정보
     * @throws RuntimeException 강의 개설을 찾을 수 없는 경우 발생
     */
    @Transactional(readOnly = true)
    public CourseOpeningDto getCourseOpening(UUID openingId) {
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("개설된 강의를 찾을 수 없습니다."));
        return convertToDto(courseOpening);
    }

    /**
     * 모든 강의 개설 목록을 조회하는 메서드
     * @return 전체 강의 개설 목록
     */
    @Transactional(readOnly = true)
    public List<CourseOpeningDto> getAllCourseOpenings() {
        return courseOpeningRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * CourseOpening 엔티티를 DTO로 변환하는 메서드
     * @param courseOpening 변환할 CourseOpening 엔티티
     * @return 변환된 CourseOpeningDto
     */
    private CourseOpeningDto convertToDto(CourseOpening courseOpening) {
        return CourseOpeningDto.builder()
                .openingId(courseOpening.getOpeningId())         // 개설 ID
                .courseId(courseOpening.getCourse().getCourseId()) // 강의 ID
                .professorId(courseOpening.getProfessorId())     // 교수 ID
                .semester(courseOpening.getSemester())           // 학기
                .year(courseOpening.getYear())                   // 연도
                .maxStudents(courseOpening.getMaxStudents())     // 최대 수강 인원
                .currentStudents(courseOpening.getCurrentStudents()) // 현재 수강 인원
                .status(courseOpening.getStatus())               // 강의 상태
                .courseTimes(courseOpening.getCourseTimes().stream()  // 강의 시간 정보 추가
                        .map(time -> CourseTimeDto.builder()
                                .timeId(time.getTimeId())
                                .openingId(time.getCourseOpening().getOpeningId())
                                .dayOfWeek(time.getDayOfWeek())
                                .startTime(time.getStartTime())
                                .endTime(time.getEndTime())
                                .classroom(time.getClassroom())
                                .build())
                        .collect(Collectors.toList()))
                .syllabus(courseOpening.getSyllabus() != null ?  // 강의 계획서 정보
                        SyllabusDto.builder()
                                .syllabusId(courseOpening.getSyllabus().getSyllabusId())
                                .learningObjectives(courseOpening.getSyllabus().getLearningObjectives())
                                .weeklyPlan(courseOpening.getSyllabus().getWeeklyPlan())
                                .evaluationMethod(courseOpening.getSyllabus().getEvaluationMethod())
                                .textbooks(courseOpening.getSyllabus().getTextbooks())
                                .build()
                        : null)
                .build();
    }
}