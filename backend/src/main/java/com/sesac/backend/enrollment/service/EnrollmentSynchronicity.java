package com.sesac.backend.enrollment.service;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

//학생 등록 요청 Class
@Getter
class RegistrationRequest implements Comparable<RegistrationRequest> {
    // test를 위한 임시 변수와 생성자--------------------------------------
    private final String studentId; // 학생 id
    private final long requestTime; // 요청 시간 밀리초로 저장
    private final int priority; // 요청의 우선순위

    // 등록 요청 생성 메서드
    public RegistrationRequest(String studentId) {
        this.studentId = studentId;
        this.requestTime = System.currentTimeMillis(); // 신청 시간
        this.priority = new Random().nextInt(); // 난수값을 통한 우선순위
    }
    //-------------------------------------------------------------------


    // Comparable 인터페이스를 구현하는 클래스에서는 compareTo 메서드와 함께 equals 메서드를 적절히 오버라이드하여 일관성을 유지
    // Comparable 인터페이스의 메서드
    @Override
    // 현재 요청과 다른 요청을 비교, 요청 시간이 같을 경우 차우선순위로 비교
    public int compareTo(RegistrationRequest other) {
        if (this.requestTime == other.requestTime) {
            return Integer.compare(this.priority, other.priority);
        }
        return Long.compare(this.requestTime, other.requestTime);
    }

    @Override
    // 두 요청의 동등성 검사
    public boolean equals(Object obj) {
        if (this == obj) return true; // 참조 비교
        if (obj == null || getClass() != obj.getClass()) return false; // null 체크 및 클래스 비교
        RegistrationRequest that = (RegistrationRequest) obj;
        return requestTime == that.requestTime && priority == that.priority && Objects.equals(studentId, that.studentId);
    }

    @Override
    // 객체의 해쉬코드 반환
    public int hashCode() {
        return Objects.hash(studentId, requestTime, priority);
    }
    //------------------------------------------------------------------------
}



//Class
// 학생의 등록 및 해지 관리 클래스
class CourseRegistration {
    // 현재 등록된 학생 수
    int currentCount = 49;
    // 동시성 문제를 방지하기 위해 사용되는 락
    private final ReentrantLock lock = new ReentrantLock();
    // 대기 중인 등록 요청을 저장하는 리스트
    private final List<RegistrationRequest> waitingList = new ArrayList<>();

    public boolean registerStudent(String studentId) { // 학생 등록 메서드
//        로직
//        RegistrationRequest 객체를 생성하고, 대기 리스트에 추가합니다.
//        현재 등록된 학생 수가 정원보다 적으면 등록을 진행하고 대기 리스트에서 제거합니다.
//        정원이 초과된 경우 대기 리스트를 정렬하고 우선순위가 가장 높은 요청과 비교하여 현재 요청이 우선순위가 높으면 등록합니다.
//        등록 성공 시 true를 반환하고, 실패 시 false를 반환합니다.

        final int capacity = 50;
        RegistrationRequest request = new RegistrationRequest(studentId);
        lock.lock();
        try {
            waitingList.add(request);

            if (currentCount < capacity) {
                currentCount++;
                waitingList.remove(request); // 대기 리스트에서 제거
                return true; // 신청 성공
            }

            Collections.sort(waitingList);
            RegistrationRequest highestPriorityRequest = waitingList.get(0);

            if (request.compareTo(highestPriorityRequest) > 0) {
                waitingList.remove(highestPriorityRequest); // 대기 중인 요청 제거
                currentCount++;
                return true; // 신청 성공
            } else {
                return false; // 신청 실패
            }
        } finally {
            lock.unlock();
        }
    }

    // 등록 해지 메서드
    public void deregisterStudent(String studentId) {
        lock.lock();

        // 로직
        //현재 등록된 학생 수가 0보다 큰 경우에만 해지를 진행합니다.
        //등록된 학생 수를 감소시키고, 대기 리스트에서 해당 학생의 요청을 제거합니다.
        try {
            if (currentCount > 0) {
                currentCount--;
                waitingList.removeIf(request -> request.getStudentId().equals(studentId));
            }
        } finally {
            lock.unlock();
        }
    }
}


// 테스트 수행 클래스
public class EnrollmentSynchronicity {
    private final CourseRegistration registration = new CourseRegistration();

    public void runConcurrentTests() {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 스레드 풀 생성

        // 등록 요청을 위한 스레드 생성
        for (int i = 0; i < 10; i++) {
            final String studentId = "Student" + i;
            executor.submit(() -> {
                boolean result = registration.registerStudent(studentId);
                System.out.println(studentId + " registration: " + result);
            });
        }

        // 해지 요청을 위한 스레드 생성
        for (int i = 0; i < 5; i++) {
            final String studentId = "Student" + i;
            executor.submit(() -> {
                registration.deregisterStudent(studentId);
                System.out.println(studentId + " deregistered.");
            });
        }

        executor.shutdown(); // 모든 작업이 완료되면 스레드 풀 종료

        // 모든 스레드가 종료될 때까지 대기
        while (!executor.isTerminated()) {
            // 대기
        }

        // 현재 등록된 학생 수 출력
        System.out.println("Current registered count: " + registration.currentCount);
    }

    public static void main(String[] args) {
        EnrollmentSynchronicity synchronicity = new EnrollmentSynchronicity();
        synchronicity.runConcurrentTests(); // 동시성 테스트 메서드 호출
    }
}

