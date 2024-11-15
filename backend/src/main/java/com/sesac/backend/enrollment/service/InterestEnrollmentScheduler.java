package com.sesac.backend.enrollment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InterestEnrollmentScheduler {
    @Autowired InterestEnrollmentService interestEnrollmentService;

    private LocalDateTime interestStart; // 관심강의 등록 시작시간
    private LocalDateTime interestEnd; // 관심강의 등록 종료시간
    private boolean isInterestActive = false;

    public void setInterestPeriod(LocalDateTime interestStart, LocalDateTime interestEnd) {
        if (interestStart.isAfter(interestEnd)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }
        this.interestStart = interestStart;
        this.interestEnd = interestEnd;
    }

    // isInterestActive 필드에 대한 getter 메서드
    public boolean isInterestActive() {
        return isInterestActive;
    }

    // 관심과목 등록 기간을 체크해서 서비스를 종료시키는 메서드
    @Scheduled(fixedRate = 5000)
    public void checkInterestPeriod(){
        if(interestStart == null || interestEnd == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        if(now.isEqual(interestStart) || now.isAfter(interestStart) && !isInterestActive) {
            startInterest();
            //interestEnrollmentService.initializeInterestEnrollment(); // 관심 과목 등록 초기화
        } else if (now.isEqual(interestEnd) || now.isAfter(interestEnd) && !isInterestActive) {
            stopInterest();
        }
    }

    // 관심과목 등록을 시작하는 메서드
    public synchronized void startInterest(){
        isInterestActive = true;
        System.out.println("관심과목 등록이 시작되었습니다.");

        // 모든 학생의 관심 과목 정보 초기화
        interestEnrollmentService.initializeInterestEnrollment();
    }

    // 관심과목 등록을 종료하는 메서드
    public synchronized void stopInterest(){
        isInterestActive = false;
        System.out.println("관심과목 등록이 종료되었습니다.");

        // 모든 학생의 관심 과목 정보 초기화
        interestEnrollmentService.initializeInterestEnrollment();
    }
}
