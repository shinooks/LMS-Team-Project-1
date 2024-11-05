package com.sesac.backend.course.controller;

import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> findAllByDepartmentAndTerm(String departmentId, String term) {
        return courseRepository.findAllByCourseNameAndTerm(courseName, term);
    }
}
