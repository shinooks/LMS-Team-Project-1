package com.sesac.backend.course.repository;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.entity.CourseTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseTimeRepository extends JpaRepository<CourseTime, UUID> {
    // 특정 강의 개설의 모든 강의 시간 조회
    List<CourseTime> findByCourseOpeningOpeningId(UUID openingId);

    // 특정 요일의 모든 강의 시간 조회
    List<CourseTime> findByDayOfWeek(DayOfWeek dayOfWeek);

    // 특정 강의실의 모든 강의 시간 조회
    List<CourseTime> findByClassroom(String classroom);

    // 특정 강의실의 특정 요일 강의 시간 조회
    List<CourseTime> findByClassroomAndDayOfWeek(String classroom, DayOfWeek dayOfWeek);

    // 같은 학기의 강의 시간 조회를 위한 메서드 추가
    @Query("SELECT ct FROM CourseTime ct WHERE ct.courseOpening.year = :year " +
            "AND ct.courseOpening.semester = :semester " +
            "AND ct.classroom = :classroom " +
            "AND ct.dayOfWeek = :dayOfWeek")
    List<CourseTime> findByYearAndSemesterAndClassroomAndDayOfWeek(
            Integer year, String semester, String classroom, DayOfWeek dayOfWeek);


    // 특정 강의 개설의 모든 강의 시간 삭제 메서드 추가
    @Modifying
    @Transactional
    @Query("DELETE FROM CourseTime ct WHERE ct.courseOpening.openingId = :openingId")
    void deleteAllByCourseOpeningOpeningId(UUID openingId);

}

