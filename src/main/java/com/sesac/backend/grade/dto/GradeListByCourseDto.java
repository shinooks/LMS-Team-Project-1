package com.sesac.backend.grade.dto;

import com.sesac.backend.entity.CourseOpening;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GradeListByCourseDto {

    private String courseName;      // 강의명
    private String courseCode;      // 강의 코드
    private String semester;        // 학기
    private List<GradeListResponseDto> grades; // 학생들의 성적 리스트


    public static GradeListByCourseDto from(CourseOpening courseOpening, List<GradeListResponseDto> grades) {
        GradeListByCourseDto dto = new GradeListByCourseDto();
        dto.courseName = courseOpening.getCourse().getCourseName();
        dto.courseCode = courseOpening.getCourse().getCourseCode();
        dto.semester = courseOpening.getSemester();
        dto.grades = grades;
        return dto;
    }

}
