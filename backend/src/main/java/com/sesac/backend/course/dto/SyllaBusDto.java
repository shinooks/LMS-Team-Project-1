package com.sesac.backend.course.dto;

import com.sesac.backend.course.entity.SyllaBus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyllaBusDto {
    private String syllabusId;
    private String openingId;
    private String learningObjectives;
    private String weeklyPlan;
    private String evaluationMethod;
    private String textbook;

    public static SyllaBusDto from(SyllaBus syllabus) {
        return new SyllaBusDto(
                syllabus.getSyllabusId(),
                syllabus.getOpeningId(),
                syllabus.getLearningObjectives(),
                syllabus.getWeeklyPlan(),
                syllabus.getEvaluationMethod(),
                syllabus.getTextbook()
        );
    }

    public SyllaBus toEntity() {
        SyllaBus syllabus = new SyllaBus();
        syllabus.setSyllabusId(this.syllabusId);
        syllabus.setOpeningId(this.openingId);
        syllabus.setLearningObjectives(this.learningObjectives);
        syllabus.setWeeklyPlan(this.weeklyPlan);
        syllabus.setEvaluationMethod(this.evaluationMethod);
        syllabus.setTextbook(this.textbook);
        return syllabus;
    }
}