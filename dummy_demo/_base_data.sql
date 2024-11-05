-- --------------------------------------------------------
-- 기본 데이터 생성
-- 생성 날짜: 2024-03-11
-- --------------------------------------------------------

-- 학과 데이터
INSERT INTO 학과 (학과ID, 학과명) VALUES
('DEPT001', '컴퓨터공학과'),
('DEPT002', '경영학과'),
('DEPT003', '전자공학과'),
('DEPT004', '영어영문학과'),
('DEPT005', '기계공학과'),
('DEPT006', '화학공학과'),
('DEPT007', '국제통상학과'),
('DEPT008', '건축공학과'),
('DEPT009', '산업디자인학과'),
('DEPT010', '생명공학과');

-- 사용자인증 데이터
INSERT INTO 사용자인증 (사용자ID, 이메일, 비밀번호해시, 사용자유형, 소셜로그인제공자, 활성화여부, 마지막로그인) VALUES
-- 한국 학생
('USER001', 'kimmin@univ.ac.kr', 'hash1234', '학생', '일반', true, '2024-03-10 10:00:00'),
('USER002', 'parkjh@univ.ac.kr', 'hash1234', '학생', '일반', true, '2024-03-10 11:00:00'),
('USER003', 'leesy@univ.ac.kr', 'hash1234', '학생', '카카오', true, '2024-03-10 12:00:00'),
-- 외국인 학생
('USER004', 'john.smith@univ.ac.kr', 'hash1234', '학생', '구글', true, '2024-03-10 09:00:00'),
('USER005', 'emma.wilson@univ.ac.kr', 'hash1234', '학생', '일반', true, '2024-03-10 14:00:00'),
('USER006', 'liu.wei@univ.ac.kr', 'hash1234', '학생', '일반', true, '2024-03-10 15:00:00'),
-- 한국 교수
('USER007', 'profkim@univ.ac.kr', 'hash1234', '교수', '일반', true, '2024-03-10 08:00:00'),
('USER008', 'profpark@univ.ac.kr', 'hash1234', '교수', '일반', true, '2024-03-10 08:30:00'),
-- 외국인 교수
('USER009', 'prof.anderson@univ.ac.kr', 'hash1234', '교수', '일반', true, '2024-03-10 13:00:00'),
-- 교직원
('USER010', 'staff.kim@univ.ac.kr', 'hash1234', '교직원', '일반', true, '2024-03-10 07:00:00'),
('USER011', 'staff.lee@univ.ac.kr', 'hash1234', '교직원', '일반', true, '2024-03-10 07:30:00');

-- 다국어 번역 데이터
INSERT INTO 다국어번역 (번역ID, 언어코드, 번역키, 번역값) VALUES
('TRANS001', 'ko', 'MENU_HOME', '홈'),
('TRANS002', 'en', 'MENU_HOME', 'Home'),
('TRANS003', 'ko', 'MENU_COURSE', '강의'),
('TRANS004', 'en', 'MENU_COURSE', 'Courses'),
('TRANS005', 'ko', 'MENU_SCHEDULE', '시간표'),
('TRANS006', 'en', 'MENU_SCHEDULE', 'Schedule');
