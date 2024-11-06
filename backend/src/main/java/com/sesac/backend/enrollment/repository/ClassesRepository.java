package com.sesac.backend.enrollment.repository;

import com.sesac.backend.enrollment.domain.tempClasses.Classes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassesRepository extends JpaRepository<Classes, Long> {
    Optional<Classes> findByClassName(String className);
}
