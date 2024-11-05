package com.sesac.backend.course.service;

import com.sesac.backend.course.dto.DepartmentDto;
import com.sesac.backend.course.repository.DepartmentRepository;
import com.sesac.backend.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

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
}