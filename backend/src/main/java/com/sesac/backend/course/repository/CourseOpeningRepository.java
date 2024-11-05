package com.sesac.backend.course.repository;

import com.sesac.backend.entity.CourseOpening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseOpeningRepository extends JpaRepository<CourseOpening, UUID> {
    // 강의 개설 관련 추가 쿼리 메서드들
}
