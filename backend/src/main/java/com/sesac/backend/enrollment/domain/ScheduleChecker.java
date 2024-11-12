package com.sesac.backend.enrollment.domain;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseTimeRepository;
import com.sesac.backend.enrollment.dto.EnrollmentDetailDto;
import com.sesac.backend.enrollment.dto.EnrollmentDto;
import com.sesac.backend.entity.CourseTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.*;

@Component
public class ScheduleChecker {

    @Autowired
    private CourseTimeRepository courseTimeRepository;

    public Map<Integer, Integer> periods = new HashMap<>();
    public Map<String, Integer> days = new HashMap<>();

    private final int FIRST_PERIOD = 9;
    private final int SECOND_PERIOD = 10;
    private final int THIRD_PERIOD = 11;
    private final int FOURTH_PERIOD = 12;
    private final int FIFTH_PERIOD = 13;
    private final int SIXTH_PERIOD = 14;
    private final int SEVENTH_PERIOD = 15;
    private final int EIGHTH_PERIOD = 16;
    private final int NINTH_PERIOD = 17;

    private final String MONDAY = "월";
    private final String TUESDAY = "화";
    private final String WEDNESDAY = "수";
    private final String THURSDAY = "목";
    private final String FRIDAY = "금";
    private final String SATURDAY = "토";
    private final String SUNDAY = "일";

    @Autowired
    private CourseOpeningRepository courseOpeningRepository;

    {
        periods.put(FIRST_PERIOD, 0);
        periods.put(SECOND_PERIOD, 1);
        periods.put(THIRD_PERIOD, 2);
        periods.put(FOURTH_PERIOD, 3);
        periods.put(FIFTH_PERIOD, 4);
        periods.put(SIXTH_PERIOD, 5);
        periods.put(SEVENTH_PERIOD, 6);
        periods.put(EIGHTH_PERIOD, 7);
        periods.put(NINTH_PERIOD, 8);

        days.put(MONDAY, 0);
        days.put(TUESDAY, 1);
        days.put(WEDNESDAY, 2);
        days.put(THURSDAY, 3);
        days.put(FRIDAY, 4);
        days.put(SATURDAY, 5);
        days.put(SUNDAY, 6);
    }


    public EnrollmentDetailDto[][] timeTableMaker(List<EnrollmentDetailDto> courseListById) {

        //System.out.println("코리바 : " + courseListById);
        EnrollmentDetailDto[][] timeTable = new EnrollmentDetailDto[9][5];

        for(EnrollmentDetailDto enrollmentDetailDto : courseListById) {
            LocalTime startTime = enrollmentDetailDto.getStartTime();
            LocalTime endTime = enrollmentDetailDto.getEndTime();
            String day = enrollmentDetailDto.getDayOfWeek().getDescription();

            if (startTime != null && endTime != null && day != null){
                int startHour = startTime.getHour();
                int endHour = endTime.getHour();
                Integer dayIndex = days.get(day); // 요일 인덱스

                Integer startPeriodIndex = periods.get(startHour); // 시작 교시 인덱스
                int period = endHour - startHour; // 수업 기간

                for( int i = 0; i < period; i++ ){
                    if (timeTable[startPeriodIndex][dayIndex] == null) {
                        timeTable[startPeriodIndex][dayIndex] = enrollmentDetailDto;
                        startPeriodIndex++; // 다음교시로 이동
                    }
                }
            }
        }

        return timeTable;
    }
}
