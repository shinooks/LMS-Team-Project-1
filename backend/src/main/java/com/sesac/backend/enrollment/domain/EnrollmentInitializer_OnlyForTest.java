package com.sesac.backend.enrollment.domain;

import com.sesac.backend.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// 수강 신청 시작, 종료 기능이 아직 구현이 되지 않았으므로, 앱 시작 시 신청 인원 초기화를 위한 클래스
public class EnrollmentInitializer_OnlyForTest implements ApplicationRunner {

    private final EnrollmentService enrollmentService;

    @Override
    public void run(ApplicationArguments args) {
        enrollmentService.initializeEnrollmentCount();
    }
}
