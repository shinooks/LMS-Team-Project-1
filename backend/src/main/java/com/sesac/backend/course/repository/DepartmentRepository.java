package com.sesac.backend.course.repository;  // 패키지 경로 확인

import com.sesac.backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    // 학과명으로 학과 정보 조회
    Optional<Department> findByDepartmentName(String departmentName);

    // 학과명 중복 확인
    boolean existsByDepartmentName(String departmentName);
}