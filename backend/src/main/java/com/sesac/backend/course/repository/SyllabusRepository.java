package com.sesac.backend.course.repository;

import com.sesac.backend.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SyllabusRepository  extends JpaRepository<Syllabus, UUID> {
    // 강의 계획서 관련 추가 쿼리 메서드들
}
