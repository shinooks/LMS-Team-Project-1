# 서비스 소개

<aside>
💡 이 프로젝트는 Spring Boot를 기반으로 가상의 대학교인 🌱새싹대학교🌱에 대한 학사지원 시스템의 주요 기능을 개발하였습니다.
</aside>
<hr>

## 🕗 프로젝트 기간
24.10.14 - 24.11.15 (4주)

## 👨‍💻 개발 인원 및 역할
| 이름 | 담당 |
| --- | --- |
| 신동진(리더) | 시험출제, 응시, 채점, CI/CD |
| 김경환 | 출석체크, 게시판, 프론트엔드 |
| 박은화 | 강의개설, UI/UX |
| 성기범 | 수강신청 |
| 정진욱 | 수강신청 |
| 홍인표 | 성적 조회, 수정, 확정 |
| 황신욱 | 권한인증, 인프라 구축 |
<hr>

## 🛠 활용 기술

![image](https://github.com/user-attachments/assets/1d8e7d5f-380d-4a82-9bdc-186bc242d4b5)
<hr>

## 핵심 기능
- 오픈소스 Redis 및 Apache Kafka을 활용한 실시간 수강신청 인원 제한
- Spring Security, Spring LDAP을 활용한 사용자 인증 및 토큰 발급
- Spring oAuth2 활용 사용자 토큰 관리
- 로그인 사용자에 따라 다른 대시보드 출력
- 로그인 사용자에 따라 API 요청 권한 부여
- 학생/교수/관리자 기능 분리
<hr>

## 인프라 아키텍처
![image](https://github.com/user-attachments/assets/f0622c7e-97d2-45ce-bfd9-86e83f049f3d)

## 배포 프로세스 및 Git 전략
![image](https://github.com/user-attachments/assets/9ff1a935-73df-4dc5-8d41-1219a0a351a9)



[화면API기술명세서.pdf](https://www.notion.so/API-PDF-14e6137c2d3f809ba5b6ddefc98f24a7)
