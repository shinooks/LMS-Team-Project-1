package com.sesac.backend.course.service;

import com.sesac.backend.course.dto.DepartmentDto;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.course.repository.DepartmentRepository;
import com.sesac.backend.entity.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    // 학과 등록
    @Transactional  // 쓰기 작업을 위해 추가
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        // DTO -> Entity
        Department department = Department.builder()
                .departmentName(departmentDto.getDepartmentName())
                .build();

        // 저장
        Department savedDepartment = departmentRepository.save(department);

        // Entity -> DTO 변환 후 반환
        return DepartmentDto.builder()
                .departmentId(savedDepartment.getDepartmentId())
                .departmentName(savedDepartment.getDepartmentName())
                .build();
    }

    // 특정 ID로 학과 조회
    public DepartmentDto getDepartmentById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("학과를 찾을 수 없습니다."));

        return DepartmentDto.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .build();
    }

    // 전체 학과 목록 조회
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> DepartmentDto.builder()
                        .departmentId(department.getDepartmentId())
                        .departmentName(department.getDepartmentName())
                        .build())
                .collect(Collectors.toList());
    }

    // 학과명으로 학과 조회
    public DepartmentDto getDepartmentByName(String departmentName) {
        Department department = departmentRepository.findByDepartmentName(departmentName)
                .orElseThrow(() -> new RuntimeException("해당 이름의 학과를 찾을 수 없습니다."));

        return DepartmentDto.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .build();
    }

    // 학과명 중복 확인
    public boolean isDepartmentNameExists(String departmentName) {
        return departmentRepository.existsByDepartmentName(departmentName);
    }

    // 학과 정보 수정
    @Transactional
    public DepartmentDto updateDepartment(UUID departmentId, DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("학과를 찾을 수 없습니다."));

        department.setDepartmentName(departmentDto.getDepartmentName());

        Department updatedDepartment = departmentRepository.save(department);
        log.info("Updated department: {}", updatedDepartment);

        return DepartmentDto.builder()  // convertToDto 대신 직접 변환
                .departmentId(updatedDepartment.getDepartmentId())
                .departmentName(updatedDepartment.getDepartmentName())
                .build();
    }

    //학과 정보 삭제
    @Transactional
    public void deleteDepartment(UUID departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("학과를 찾을 수 없습니다."));

        // 연관된 강의가 있는지 확인
        long courseCount = courseRepository.countByDepartment(department);
        if (courseCount > 0) {
            throw new RuntimeException("이 학과에 연결된 강의가 있어 삭제할 수 없습니다.");
        }

        departmentRepository.delete(department);
        log.info("Deleted department: {}", department);
    }

}