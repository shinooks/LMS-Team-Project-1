package com.sesac.backend.course.service;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.DepartmentRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.course.dto.CourseDto;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
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
    private final DepartmentRepository departmentRepository;
    private final CourseOpeningRepository courseOpeningRepository;

    // 강의 등록
    public CourseDto createCourse(CourseDto courseDto) {
        Department department = departmentRepository.findById(courseDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("학과를 찾을 수 없습니다."));

        Course course = Course.builder()
                .courseCode(courseDto.getCourseCode())
                .courseName(courseDto.getCourseName())
                .department(department)
                .credits(courseDto.getCredits())  // Enum이 자동으로 Integer로 변환됨
                .description(courseDto.getDescription())
                .build();

        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    // 강의 조회
    @Transactional(readOnly = true)
    public CourseDto getCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));
        return convertToDto(course);
    }

    // 전체 강의 목록 조회
    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 강의 수정
    public CourseDto updateCourse(UUID courseId, CourseDto courseDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));

        Department department = departmentRepository.findById(courseDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("학과를 찾을 수 없습니다."));

        course.setCourseCode(courseDto.getCourseCode());
        course.setCourseName(courseDto.getCourseName());
        course.setDepartment(department);
        course.setCredits(courseDto.getCredits());
        course.setDescription(courseDto.getDescription());

        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    // 강의 삭제
    public void deleteCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));

        // 강의 개설 정보가 있는지 확인
        List<CourseOpening> courseOpenings = courseOpeningRepository.findByCourse(course);
        if (!courseOpenings.isEmpty()) {
            throw new RuntimeException(
                    "이 강의의 개설 정보가 존재하여 삭제할 수 없습니다. " +
                            "먼저 강의 개설 정보를 삭제해주세요."
            );
        }

        courseRepository.delete(course);
    }

    // Entity -> DTO 변환
    private CourseDto convertToDto(Course course) {
        return CourseDto.builder()
                .courseId(course.getCourseId())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .departmentId(course.getDepartment().getDepartmentId())
                .credits(course.getCreditsEnum())  // Integer를 Enum으로 변환
                .description(course.getDescription())
                .build();
    }
}