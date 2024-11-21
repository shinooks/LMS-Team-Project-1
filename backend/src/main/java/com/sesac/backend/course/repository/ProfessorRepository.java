package com.sesac.backend.course.repository;

import com.sesac.backend.entity.Department;
import com.sesac.backend.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {


    // 특정 학과의 교수 목록 조회
    List<Professor> findByDepartment(Department department);

    // 교수 이름으로 교수 목록 조회 (동명이인 가능성)
    List<Professor> findByName(String name);

    // 교수 번호로 교수 목록 조회 (단일 결과)
    Optional<Professor> findByProfessorNumber(String professorNumber);

}
