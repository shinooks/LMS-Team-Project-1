package com.sesac.backend.enrollment.repository;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.entity.Enrollment;
import com.sesac.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByStudent_StudentId(UUID studentId);

    List<Enrollment> findAllByCourseOpeningOpeningId(UUID openingId);

    //@Query("SELECT co FROM CourseOpening co JOIN co.courseTimes cct JOIN CourseTime ct ON ct.")

    // : 기호는 JPQL (Java Persistence Query Language) 또는 JPA (Java Persistence API)에서 Named Parameters를 나타내는 데 사용됩니다.
    // Named Parameters는 쿼리에서 특정 값을 동적으로 바인딩할 수 있도록 해 주며, 코드 가독성을 높이고 SQL 인젝션 공격을 방지하는 데 도움을 줍니다.
    // 특정 학생이 시간 겹침이 있는 강의를 이미 등록했는지 확인
    @Query("SELECT e FROM Enrollment e JOIN e.courseOpening co JOIN CourseTime ct ON ct.courseOpening = co "
            + "WHERE e.student = :student "
            + "AND ct.dayOfWeek = :day "
            + "AND ( " + " (ct.startTime < :endTime AND ct.startTime > :startTime) OR "
            + " (ct.endTime > :startTime AND ct.endTime < :endTime) OR "
            + " (ct.startTime <= :startTime AND ct.endTime >= :endTime) " + ")")
    List<Enrollment> findConflictingEnrollments(@Param("student") Student student,
                                                @Param("day") DayOfWeek day,
                                                @Param("startTime") LocalTime startTime,
                                                @Param("endTime") LocalTime endTime);

    boolean existsByStudent_StudentIdAndCourseOpening_OpeningId(UUID studentId, UUID openingId);




}
