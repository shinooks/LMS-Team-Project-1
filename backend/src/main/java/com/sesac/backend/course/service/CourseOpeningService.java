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

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseOpeningService {

    private final CourseOpeningRepository courseOpeningRepository;
    private final CourseRepository courseRepository;

    // 강의 개설 생성
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

        return convertToDto(savedCourseOpening);
    }

    // 특정 강의 개설 조회
    @Transactional(readOnly = true)
    public CourseOpeningDto getCourseOpening(UUID openingId) {
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("개설된 강의를 찾을 수 없습니다."));
        return convertToDto(courseOpening);
    }

    // 전체 강의 개설 목록 조회
    @Transactional(readOnly = true)
    public List<CourseOpeningDto> getAllCourseOpenings() {
        return courseOpeningRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Entity를 DTO로 변환
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

    // 강의 개설 수정
    public CourseOpeningDto updateCourseOpening(UUID openingId, CourseOpeningDto courseOpeningDto) {
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("개설된 강의를 찾을 수 없습니다."));

        Course course = courseRepository.findById(courseOpeningDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));

        courseOpening.setCourse(course);
        courseOpening.setProfessorId(courseOpeningDto.getProfessorId());
        courseOpening.setSemester(courseOpeningDto.getSemester());
        courseOpening.setYear(courseOpeningDto.getYear());
        courseOpening.setMaxStudents(courseOpeningDto.getMaxStudents());
        courseOpening.setStatus(courseOpeningDto.getStatus());

        CourseOpening updatedOpening = courseOpeningRepository.save(courseOpening);
        return convertToDto(updatedOpening);
    }

    // 강의 개설 삭제 (연관된 강의시간, 강의계획서도 자동 삭제)
    public void deleteCourseOpening(UUID openingId) {
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("개설된 강의를 찾을 수 없습니다."));

        courseOpeningRepository.delete(courseOpening);
    }
}