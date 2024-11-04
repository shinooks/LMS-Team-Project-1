package com.sesac.backend.course.dto;

import com.sesac.backend.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자를 자동으로 생성
public class CourseDto {
        private String courseId;
        private String courseCode;
        private String courseName;
        private String departmentId;
        private Integer credit;
        private String description;

        public static CourseDto from(Course course) {
                return new CourseDto(
                        course.getCourseId(),
                        course.getCourseCode(),
                        course.getCourseName(),
                        course.getDepartmentId(),
                        course.getCredit(),
                        course.getDescription()
                );
        }

        public Course toEntity() {
                Course course = new Course();
                course.setCourseId(this.courseId);
                course.setCourseCode(this.courseCode);
                course.setCourseName(this.courseName);
                course.setDepartmentId(this.departmentId);
                course.setCredit(this.credit);
                course.setDescription(this.description);
                return course;
        }
}