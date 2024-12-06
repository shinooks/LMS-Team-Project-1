package com.sesac.backend.course.service;


import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.dto.ProfessorDto;
import com.sesac.backend.course.repository.DepartmentRepository;
import com.sesac.backend.course.repository.ProfessorRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Department;
import com.sesac.backend.entity.Professor;
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
public class ProfessorService {


    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;

    // 특정 교수 조회
    @Transactional(readOnly = true)
    public ProfessorDto getProfessor(UUID professorId) {
        return convertToDto(findProfessorById(professorId));
    }

    // 전체 교수 목록 조회
    @Transactional(readOnly = true)
    public List<ProfessorDto> getAllProfessors() {
        return professorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 교수 번호로 교수 조회
    @Transactional(readOnly = true)
    public ProfessorDto getProfessorByNumber(String professorNumber) {
        Professor professor = professorRepository.findByProfessorNumber(professorNumber)
                .orElseThrow(() -> new RuntimeException("해당 교수 번호로 등록된 교수를 찾을 수 없습니다."));
        return convertToDto(professor);
    }

    // 학과별 교수 목록 조회
    @Transactional(readOnly = true)
    public List<ProfessorDto> getProfessorsByDepartment(UUID departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("학과를 찾을 수 없습니다."));
        return professorRepository.findByDepartment(department).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 이름으로 교수 검색
    @Transactional(readOnly = true)
    public List<ProfessorDto> getProfessorsByName(String name) {
        return professorRepository.findByName(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 특정 교수의 전체 강의 목록 조회
    @Transactional(readOnly = true)
    public List<CourseOpeningDto> getAllProfessorCourses(UUID professorId) {
        Professor professor = findProfessorById(professorId);
        return professor.getCourseOpenings().stream()
                .map(this::convertToCourseOpeningDto)
                .collect(Collectors.toList());
    }

    // 특정 교수의 특정 학기 강의 목록 조회
    @Transactional(readOnly = true)
    public List<CourseOpeningDto> getProfessorCoursesBySemester(
            UUID professorId, Integer year, String semester) {
        Professor professor = findProfessorById(professorId);
        return professor.getCourseOpenings().stream()
                .filter(co -> co.getYear().equals(year) && co.getSemester().equals(semester))
                .map(this::convertToCourseOpeningDto)
                .collect(Collectors.toList());
    }

    private Professor findProfessorById(UUID professorId) {
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("교수를 찾을 수 없습니다."));
    }

    private ProfessorDto convertToDto(Professor professor) {
        return ProfessorDto.builder()
                .professorId(professor.getProfessorId())
                .name(professor.getName())
                .professorNumber(professor.getProfessorNumber())
                .departmentId(professor.getDepartment().getDepartmentId())
                .userId(professor.getUser().getUserId())
                .build();
    }

    private CourseOpeningDto convertToCourseOpeningDto(CourseOpening courseOpening) {
        return CourseOpeningDto.builder()
                .openingId(courseOpening.getOpeningId())
                .courseId(courseOpening.getCourse().getCourseId())
                .professor(convertToDto(courseOpening.getProfessor()))
                .semester(courseOpening.getSemester())
                .year(courseOpening.getYear())
                .maxStudents(courseOpening.getMaxStudents())
                .currentStudents(courseOpening.getCurrentStudents())
                .status(courseOpening.getStatus())
                .build();
    }
}

