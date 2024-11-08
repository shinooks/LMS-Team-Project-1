package com.sesac.backend.course.service;

import com.sesac.backend.course.dto.SyllabusDto;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.SyllabusRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Syllabus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SyllabusService {

    private final SyllabusRepository syllabusRepository;
    private final CourseOpeningRepository courseOpeningRepository;

    // 전체 강의계획서 조회
    @Transactional(readOnly = true)
    public List<SyllabusDto> getAllSyllabi() {
        return syllabusRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 강의 계획서를 등록
    public SyllabusDto createSyllabus(UUID openingId, SyllabusDto syllabusDto) {
        // 강의 개설 정보 조회
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("강의 개설 정보를 찾을 수 없습니다."));

        // 이미 강의 계획서가 있는지 확인
        if (existsSyllabusByOpeningId(openingId)) {
            throw new RuntimeException("이미 강의 계획서가 존재합니다.");
        }

        // Syllabus 엔티티 생성 및 저장
        Syllabus syllabus = Syllabus.builder()
                .courseOpening(courseOpening)
                .learningObjectives(syllabusDto.getLearningObjectives())
                .weeklyPlan(syllabusDto.getWeeklyPlan())
                .evaluationMethod(syllabusDto.getEvaluationMethod())
                .textbooks(syllabusDto.getTextbooks())
                .build();

        syllabus.setCourseOpening(courseOpening);
        log.info("Creating syllabus for opening id: {}", openingId);
        return convertToDto(syllabusRepository.save(syllabus));
    }

    // 강의 개설 id로 조회
    @Transactional(readOnly = true)
    public SyllabusDto getSyllabusByOpeningId(UUID openingId) {
        return syllabusRepository.findByCourseOpeningOpeningId(openingId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("강의 계획서를 찾을 수 없습니다."));
    }

    // 특정 강의 계획서 조회
    @Transactional(readOnly = true)
    public SyllabusDto getSyllabus(UUID syllabusId) {
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new RuntimeException("강의 계획서를 찾을 수 없습니다."));
        return convertToDto(syllabus);
    }

    // 강의 계획서 수정
    public SyllabusDto updateSyllabus(UUID syllabusId, SyllabusDto syllabusDto) {
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new RuntimeException("강의 계획서를 찾을 수 없습니다."));

        // 내용 업데이트
        syllabus.setLearningObjectives(syllabusDto.getLearningObjectives());
        syllabus.setWeeklyPlan(syllabusDto.getWeeklyPlan());
        syllabus.setEvaluationMethod(syllabusDto.getEvaluationMethod());
        syllabus.setTextbooks(syllabusDto.getTextbooks());

        log.info("Updating syllabus: {}", syllabusId);
        return convertToDto(syllabusRepository.save(syllabus));
    }

    // 강의 계획서 삭제
    public void deleteSyllabus(UUID syllabusId) {
        syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new RuntimeException("강의 계획서를 찾을 수 없습니다."));
        syllabusRepository.deleteById(syllabusId);
        log.info("Deleted syllabus: {}", syllabusId);
    }

    // 강의 계획서 존재 여부 확인
    @Transactional(readOnly = true)
    public boolean existsSyllabusByOpeningId(UUID openingId) {
        return syllabusRepository.existsByCourseOpeningOpeningId(openingId);
    }

    // Entity를 DTO로 변환
    private SyllabusDto convertToDto(Syllabus syllabus) {
        return SyllabusDto.builder()
                .syllabusId(syllabus.getSyllabusId())
                .learningObjectives(syllabus.getLearningObjectives())
                .weeklyPlan(syllabus.getWeeklyPlan())
                .evaluationMethod(syllabus.getEvaluationMethod())
                .textbooks(syllabus.getTextbooks())
                .build();
    }
}