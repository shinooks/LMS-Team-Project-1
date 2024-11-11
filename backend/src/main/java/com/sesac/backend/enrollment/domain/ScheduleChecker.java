//package com.sesac.backend.enrollment.domain;
//
//import com.sesac.backend.course.constant.DayOfWeek;
//import com.sesac.backend.enrollment.dto.EnrollmentDto;
//import com.sesac.backend.entity.CourseTime;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalTime;
//import java.util.*;
//
//@Component
//public class ScheduleChecker {
//
//    public Map<Integer, Integer> periods = new HashMap<>();
//    public Map<String, Integer> days = new HashMap<>();
//
//    private final int FIRST_PERIOD = 9;
//    private final int SECOND_PERIOD = 10;
//    private final int THIRD_PERIOD = 11;
//    private final int FOURTH_PERIOD = 12;
//    private final int FIFTH_PERIOD = 13;
//    private final int SIXTH_PERIOD = 14;
//    private final int SEVENTH_PERIOD = 15;
//    private final int EIGHTH_PERIOD = 16;
//    private final int NINTH_PERIOD = 17;
//
//    private final String monday = "월";
//    private final String tuesday = "화";
//    private final String wednesday = "수";
//    private final String thursday = "목";
//    private final String friday = "금";
//
//    {
//        periods.put(FIRST_PERIOD, 0);
//        periods.put(SECOND_PERIOD, 1);
//        periods.put(THIRD_PERIOD, 2);
//        periods.put(FOURTH_PERIOD, 3);
//        periods.put(FIFTH_PERIOD, 4);
//        periods.put(SIXTH_PERIOD, 5);
//        periods.put(SEVENTH_PERIOD, 6);
//        periods.put(EIGHTH_PERIOD, 7);
//        periods.put(NINTH_PERIOD, 8);
//
//        days.put(monday, 0);
//        days.put(tuesday, 1);
//        days.put(wednesday, 2);
//        days.put(thursday, 3);
//        days.put(friday, 4);
//    }
//
//
//    public EnrollmentDto[][] timeTableMaker(List<EnrollmentDto> list) {
//
//        EnrollmentDto[][] timeTable = new EnrollmentDto[9][5];
//
//        for (EnrollmentDto dto : list) {
//
//            List<CourseTime> tmpList = dto.getCourseOpening().getCourseTimes();
//
//            LocalTime tmpStartTime = null;
//            LocalTime tmpEndTime = null;
//            DayOfWeek day = null;
//
//            for (CourseTime times : tmpList) {
//                tmpStartTime = times.getStartTime();
//                tmpEndTime = times.getEndTime();
//                day = times.getDayOfWeek();
//            }
//
//            int startHour = 0;
//            int endHour = 0;
//            Integer dayIndex = 0;
//
//            if (tmpStartTime != null && tmpEndTime != null && day != null) {
//                startHour = tmpStartTime.getHour();
//                endHour = tmpEndTime.getHour();
//                dayIndex = days.get(day.toString());
//            } else {
//                System.out.println("시간 혹은 요일 정보가 없습니다.");
//            }
//
//            Integer startPeriodIndex = periods.get(startHour);
//            int period = endHour - startHour;
//
//            for (int i = 0; i < period; i++) {
//                if (timeTable[startPeriodIndex][dayIndex] == null) {
//                    timeTable[startPeriodIndex][dayIndex] = dto;
//                    startPeriodIndex++;
//                }
//            }
//        }
//
//        return timeTable;
//    }
//}
