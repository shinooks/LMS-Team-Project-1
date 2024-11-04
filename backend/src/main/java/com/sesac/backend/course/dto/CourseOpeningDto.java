package com.sesac.backend.course.dto;

import com.sesac.backend.course.dto.CourseTimeDto;
import com.sesac.backend.course.dto.SyllaBusDto;
import com.sesac.backend.course.entity.CourseOpening;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseOpeningDto {
    private String openingId;
    private String courseId;
    private String professorId;
    private String semester;
    private Integer year;
    private Integer maxStudents;
    private Integer currentStudents;
    private String status;
    private List<CourseTimeDto> courseTimes;
    private SyllaBusDto syllabus;

    public static CourseOpeningDto from(CourseOpening courseOpening) {
        CourseOpeningDto dto = new CourseOpeningDto();
        dto.setOpeningId(courseOpening.getOpeningId());
        dto.setCourseId(courseOpening.getCourseId());
        dto.setProfessorId(courseOpening.getProfessorId());
        dto.setSemester(courseOpening.getSemester());
        dto.setYear(courseOpening.getYear());
        dto.setMaxStudents(courseOpening.getMaxStudents());
        dto.setCurrentStudents(courseOpening.getCurrentStudents());
        dto.setStatus(courseOpening.getStatus());

        if (courseOpening.getCourseTimes() != null) {
            dto.setCourseTimes(courseOpening.getCourseTimes().stream()
                    .map(CourseTimeDto::from)
                    .collect(Collectors.toList()));
        }

        if (courseOpening.getSyllabus() != null) {
            dto.setSyllabus(SyllaBusDto.from(courseOpening.getSyllabus()));
        }

        return dto;
    }

    public CourseOpening toEntity() {
        CourseOpening courseOpening = new CourseOpening();
        courseOpening.setOpeningId(this.openingId);
        courseOpening.setCourseId(this.courseId);
        courseOpening.setProfessorId(this.professorId);
        courseOpening.setSemester(this.semester);
        courseOpening.setYear(this.year);
        courseOpening.setMaxStudents(this.maxStudents);
        courseOpening.setCurrentStudents(this.currentStudents);
        courseOpening.setStatus(this.status);
        return courseOpening;
    }
}