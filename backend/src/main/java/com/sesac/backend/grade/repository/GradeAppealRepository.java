package com.sesac.backend.grade.repository;

import com.sesac.backend.entity.GradeAppeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GradeAppealRepository extends JpaRepository<GradeAppeal, UUID> {
    List<GradeAppeal> findByGrade_GradeId(UUID gradeId);
    List<GradeAppeal> findByGrade_CourseOpening_OpeningId(UUID openingId);
}
