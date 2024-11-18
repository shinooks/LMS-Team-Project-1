package com.sesac.backend.course.repository;
import com.sesac.backend.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, UUID> {
    // 특정 강의 개설의 강의계획서 조회
    Optional<Syllabus> findByCourseOpeningOpeningId(UUID openingId);

    // 특정 강의 개설의 강의계획서 존재 여부 확인
    boolean existsByCourseOpeningOpeningId(UUID openingId);
}