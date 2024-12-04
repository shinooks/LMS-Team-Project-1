package com.sesac.backend.enrollment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.enrollment.domain.ScheduleChecker;
import com.sesac.backend.enrollment.dto.InterestTimeTableDto;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.CourseTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterestEnrollmentService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CourseOpeningRepository courseOpeningRepository;

    @Autowired
    private ObjectMapper objectMapper;  // JSON 변환용

    @Autowired
    private ScheduleChecker scheduleChecker;

    // 기존 관심 과목 등록 데이터 초기화
    public void initializeInterestEnrollment(){
        // 모든 학생의 관심 과목 목록을 초기화 하기 위한 패턴
        String pattern = "student:*:interest_course:*";

        // Redis에서 해당 패턴에 맞는 모든 키 가져오기
        Set<String> keys = redisTemplate.keys(pattern);

        try {
            if(keys != null && !keys.isEmpty()){
                for(String key : keys){
                    redisTemplate.delete(key); // 각 키 삭제
                }
            }
            System.out.println("모든 학생의 관심 과목 정보가 초기화 되었습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 관심강의 등록
    public void saveStudentInterest(UUID studentId, UUID openingId) throws JsonProcessingException {
        String key = "student:" + studentId + ":interest_course:" + openingId;

        // 이미 등록된 관심 강의가 있는지 확인
        if (redisTemplate.hasKey(key)) {
            throw new RuntimeException("이미 등록된 관심 강의입니다: " + openingId);
        }

        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
                .orElseThrow(() -> new RuntimeException("Course opening not found"));

        // 관심 과목에 필요한 정보 수집
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("openingId", courseOpening.getOpeningId());
        courseInfo.put("courseCode", courseOpening.getCourse().getCourseCode());
        courseInfo.put("courseName", courseOpening.getCourse().getCourseName());
        courseInfo.put("credits", courseOpening.getCourse().getCredits());
        courseInfo.put("professorName", courseOpening.getProfessor().getName());

        // 강의 시간 정보 추가
        for (CourseTime courseTime : courseOpening.getCourseTimes()) {
            Map<String, Object> timeInfo = new HashMap<>();
            timeInfo.put("day", courseTime.getDayOfWeek().getDescription());
            timeInfo.put("startTime", courseTime.getStartTime().toString());
            timeInfo.put("endTime", courseTime.getEndTime().toString());
            timeInfo.put("classroom", courseTime.getClassroom());
            courseInfo.put("timeInfo", timeInfo);  // 각 시간 정보를 저장
        }

        // JSON 변환 후 Redis에 저장
        String courseInfoJson = objectMapper.writeValueAsString(courseInfo);
        redisTemplate.opsForValue().set("student:" + studentId + ":interest_course:" + openingId, courseInfoJson);
    }

    // 관심강의 삭제
    public void deleteStudentInterest(UUID studentId, UUID openingId) {
        String key = "student:" + studentId + ":interest_course:" + openingId;

//        // 시간표에서 해당 강의 정보 삭제
//        InterestTimeTableDto[][] timeTable = getTimeTableById(studentId);
//        if (timeTable != null) {
//            for (int i = 0; i < timeTable.length; i++) {
//                for (int j = 0; j < timeTable[i].length; j++) {
//                    if (timeTable[i][j] != null &&
//                            timeTable[i][j].getOpeningId().equals(openingId)) {
//                        timeTable[i][j] = null; // 시간표에서 해당 강의 정보 삭제
//                    }
//                }
//            }
//        }

        redisTemplate.delete(key);
    }

    // 학생별 관심강의 목록 조회
    public List<Map<String, Object>> getStudentInterests(UUID studentId) {
        // 관심강의 목록을 담을 배열 생성
        List<Map<String, Object>> interests = new ArrayList<>();

        // redis에서 관심 강의 목록 가져오기
        String pattern = "student:" + studentId + ":interest_course:*";
        // 패턴에 맞는 모든 키를 가져옴
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null) {
            for (String key : keys) {
                String courseInfoJson = redisTemplate.opsForValue().get(key);
                try {
                    Map<String, Object> courseInfo = objectMapper.readValue(courseInfoJson, Map.class);
                    interests.add(courseInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("JSON 변환 중 오류 발생: " + e.getMessage());
                }
            }
        }

        return interests;
    }


//    public InterestTimeTableDto[][] getTimeTableById(UUID studentId) {
//        // Redis에서 학생의 관심 강의 목록 가져오기
//        List<Map<String, Object>> interests = getStudentInterests(studentId);
//
//        // 시간표에 필요한 데이터 구조 만들기
//        List<InterestTimeTableDto> interestsForTable = new ArrayList<>();
//
//        for (Map<String, Object> courseInfo : interests) {
//            // 시간 정보가 있는지 확인
//            Map<String, Object> timeInfos = (Map<String, Object>) courseInfo.get("timeInfo");
//            if (timeInfos != null) {
//
//                interestsForTable.add(new InterestTimeTableDto(
//                        UUID.fromString((String) courseInfo.get("openingId")), // String을 UUID로 변환
//                        (String) courseInfo.get("courseCode"),
//                        (String) courseInfo.get("courseName"),
//                        (String) timeInfos.get("day"), // 요일 정보
//                        (String) timeInfos.get("startTime"), // 시작 시간
//                        (String) timeInfos.get("endTime"), // 종료 시간
//                        (String) timeInfos.get("classroom") // 강의실 정보
//                ));
//            }
//        }
//
//        return scheduleChecker.interestTimeTableMaker(interestsForTable);
//    }
}