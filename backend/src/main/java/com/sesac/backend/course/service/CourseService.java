package com.sesac.backend.course.service;

import com.sesac.backend.course.repository.DepartmentRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.course.dto.CourseDto;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;  // 학과 레포지토리 추가

    // 강의 등록
    public CourseDto createCourse(CourseDto courseDto) {
        // 학과 조회 (없으면 예외 발생)
        Department department = departmentRepository.findById(courseDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("학과를 찾을 수 없습니다."));

        // Course 엔티티 생성
        Course course = Course.builder()
                .courseCode(courseDto.getCourseCode())    // 강의코드
                .courseName(courseDto.getCourseName())    // 강의명
                .department(department)                    // 학과정보
                .credits(courseDto.getCredits())          // 학점
                .description(courseDto.getDescription())   // 설명
                .build();

        // 데이터베이스에 저장
        Course savedCourse = courseRepository.save(course);
        // DTO로 변환하여 반환
        return convertToDto(savedCourse);
    }

    // 강의 조회
    @Transactional(readOnly = true)
    public CourseDto getCourse(UUID courseId) {
        // ID로 강의 조회 (없으면 예외 발생)
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));
        // DTO로 변환하여 반환
        return convertToDto(course);
    }
    // 전체 강의 목록 조회 메서드 추가
    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Entity -> DTO 변환 메서드
    private CourseDto convertToDto(Course course) {
        return CourseDto.builder()
                .courseId(course.getCourseId())           // 강의ID
                .courseCode(course.getCourseCode())       // 강의코드
                .courseName(course.getCourseName())       // 강의명
                .departmentId(course.getDepartment().getDepartmentId())  // 학과ID
                .credits(course.getCredits())            // 학점
                .description(course.getDescription())     // 설명
                .build();
    }
}