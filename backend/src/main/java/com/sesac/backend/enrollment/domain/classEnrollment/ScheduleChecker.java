package com.sesac.backend.enrollment.domain.classEnrollment;

import com.sesac.backend.enrollment.dto.ClassEnrollmentDto;
import com.sesac.backend.enrollment.dto.ClassesDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ScheduleChecker {


    public Map<Integer, Integer> periods = new HashMap<>();
    public Map<String, Integer> days = new HashMap<>();

    private final int firstPeriod = 9;
    private final int secondPeriod = 10;
    private final int thirdPeriod = 11;
    private final int fourthPeriod = 12;
    private final int fifthPeriod = 13;
    private final int sixthPeriod = 14;
    private final int seventhPeriod = 15;
    private final int eighthPeriod = 16;
    private final int ninthPeriod = 17;

    private final String monday = "월요일";
    private final String tuesday = "화요일";
    private final String wednesday = "수요일";
    private final String thursday = "목요일";
    private final String friday = "금요일";
    {
        periods.put(firstPeriod, 0);
        periods.put(secondPeriod, 1);
        periods.put(thirdPeriod, 2);
        periods.put(fourthPeriod, 3);
        periods.put(fifthPeriod, 4);
        periods.put(sixthPeriod, 5);
        periods.put(seventhPeriod, 6);
        periods.put(eighthPeriod, 7);
        periods.put(ninthPeriod, 8);

        days.put(monday, 0);
        days.put(tuesday, 1);
        days.put(wednesday, 2);
        days.put(thursday, 3);
        days.put(friday, 4);
    }



    public ClassEnrollmentDto[][] timeTableMaker(List<ClassEnrollmentDto> list) {

        ClassEnrollmentDto[][] timeTable = new ClassEnrollmentDto[9][5];

        for (ClassEnrollmentDto dto : list) {
            String tmpStartTime = dto.getClasses().getStartTime();
            String tmpEndTime = dto.getClasses().getEndTime();

            int startHour = Integer.parseInt(tmpStartTime.split(":")[0]);
            int endHour = Integer.parseInt(tmpEndTime.split(":")[0]);
            int totalHour = endHour - startHour;

            Integer startPeriodIndex = periods.get(startHour);
            Integer dayIndex = days.get(dto.getClasses().getDay());

            for (int i = 0; i < totalHour; i++) {
                if (timeTable[startPeriodIndex][dayIndex] == null) {
                    timeTable[startPeriodIndex][dayIndex] = dto;
                    startPeriodIndex++;
                }
//                else {
//                    throw new TimeOverlapException(
//                            "시간 또는 요일이 겹치는 강의가 있습니다: "
//                                    + timeTable[startPeriodIndex][dayIndex].getClassName());
//                }
            }
        }

        return timeTable;
    }
}
