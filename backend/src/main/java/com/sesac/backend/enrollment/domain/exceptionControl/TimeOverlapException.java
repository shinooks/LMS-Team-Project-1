package com.sesac.backend.enrollment.domain.exceptionControl;

// 시간 겹침에 대한 exception을 던져줄 클래스
public class TimeOverlapException extends RuntimeException {
    public TimeOverlapException(String message) {
        super(message);
    }
}
