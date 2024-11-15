package com.sesac.backend.enrollment.domain;

import com.sesac.backend.course.repository.CourseTimeRepository;
import com.sesac.backend.enrollment.dto.InterestTimeTableDto;
import com.sesac.backend.enrollment.dto.TimeTableCellDto;
import com.sesac.backend.entity.CourseTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.*;

@Component
public class InterestScheduleChecker {

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


    public TimeTableCellDto[][] timeTableMaker(List<TimeTableCellDto> list) {

        TimeTableCellDto[][] timeTable = new TimeTableCellDto[9][5];


        for (TimeTableCellDto info : list) {

            UUID openingId = info.getOpeningId();

            List<CourseTime> tmpList = courseTimeRepository.findByCourseOpeningOpeningId(openingId);

            LocalTime tmpStartTime = null;
            LocalTime tmpEndTime = null;
            String day = null;

            for (CourseTime times : tmpList) {
                tmpStartTime = times.getStartTime();
                tmpEndTime = times.getEndTime();
                day = times.getDayOfWeek().getDescription();
            }

            int startHour = 0;
            int endHour = 0;
            Integer dayIndex = 0;

            if (tmpStartTime != null && tmpEndTime != null && day != null) {
                startHour = tmpStartTime.getHour();
                endHour = tmpEndTime.getHour();
                dayIndex = days.get(day);
            } else {
                System.out.println("시간 혹은 요일 정보가 없습니다.");
            }

            Integer startPeriodIndex = periods.get(startHour);
            int period = endHour - startHour;

            for (int i = 0; i < period; i++) {
                if (timeTable[startPeriodIndex][dayIndex] == null) {
                    timeTable[startPeriodIndex][dayIndex] = info;
                    startPeriodIndex++;
                }
            }
        }
        return timeTable;
    }


    public InterestTimeTableDto[][] interestTimeTableMaker(List<InterestTimeTableDto> list) {

        InterestTimeTableDto[][] timeTable = new InterestTimeTableDto[9][5];


        for (InterestTimeTableDto info : list) {

            UUID openingId = info.getOpeningId();

            List<CourseTime> tmpList = courseTimeRepository.findByCourseOpeningOpeningId(openingId);

            LocalTime tmpStartTime = null;
            LocalTime tmpEndTime = null;
            String day = null;

            for (CourseTime times : tmpList) {
                tmpStartTime = times.getStartTime();
                tmpEndTime = times.getEndTime();
                day = times.getDayOfWeek().getDescription();
            }

            int startHour = 0;
            int endHour = 0;
            Integer dayIndex = 0;

            if (tmpStartTime != null && tmpEndTime != null && day != null) {
                startHour = tmpStartTime.getHour();
                endHour = tmpEndTime.getHour();
                dayIndex = days.get(day);
            } else {
                System.out.println("시간 혹은 요일 정보가 없습니다.");
            }

            Integer startPeriodIndex = periods.get(startHour);
            int period = endHour - startHour;

            for (int i = 0; i < period; i++) {
                if (timeTable[startPeriodIndex][dayIndex] == null) {
                    timeTable[startPeriodIndex][dayIndex] = info;
                    startPeriodIndex++;
                }
            }
        }
        return timeTable;
    }
}
