-- --------------------------------------------------------
-- 게시판 관련 데이터 생성
-- 생성 날짜: 2024-03-11
-- --------------------------------------------------------

-- 게시판 데이터
INSERT INTO 게시판 (게시판ID, 게시판이름, 게시판유형, 익명허용, 댓글허용, 수정허용, 삭제허용) VALUES
                                                                  ('BOARD001', '공지사항', '공지사항', false, true, false, false),
                                                                  ('BOARD002', '학사공지', '공지사항', false, true, false, false),
                                                                  ('BOARD003', '자유게시판', '일반', true, true, true, true),
                                                                  ('BOARD004', '학과별공지', '공지사항', false, true, false, false),
                                                                  ('BOARD005', '취업정보', '일반', false, true, true, true),
                                                                  ('BOARD006', '질문과답변', 'Q&A', true, true, true, true),
                                                                  ('BOARD007', '동아리모집', '일반', false, true, true, true);

-- 게시글 데이터
INSERT INTO 게시글 (게시글ID, 게시판ID, 작성자ID, 제목, 내용, 익명여부, 조회수, 생성일시) VALUES
-- 공지사항
('POST001', 'BOARD001', 'USER010', '2024학년도 1학기 개강 안내', '3월 2일부터 개강입니다. 수업 시간표를 반드시 확인하시기 바랍니다.', false, 352, '2024-02-25 09:00:00'),
('POST002', 'BOARD001', 'USER011', '코로나19 예방 수칙 안내', '교내 마스크 착용 규정 안내드립니다.', false, 245, '2024-02-26 10:30:00'),

-- 학사공지
('POST003', 'BOARD002', 'USER010', '수강신청 일정 안내', '2024학년도 1학기 수강신청 일정을 안내드립니다.', false, 521, '2024-02-15 11:00:00'),
('POST004', 'BOARD002', 'USER011', '졸업사정 결과 발표', '2024년 2월 졸업예정자 졸업사정 결과를 안내합니다.', false, 423, '2024-02-20 14:00:00'),

-- 자유게시판
('POST005', 'BOARD003', 'USER001', '학식 맛있는 메뉴 추천', '오늘 학식 돈까스 최고였어요!', true, 89, '2024-03-11 12:30:00'),
('POST006', 'BOARD003', 'USER004', 'Study Group Looking for Members', 'Looking for members to join our English study group', false, 45, '2024-03-11 15:20:00'),

-- Q&A
('POST007', 'BOARD006', 'USER002', '수강신청 에러 문의', '수강신청 페이지에서 오류가 발생합니다.', true, 67, '2024-03-10 16:45:00'),
('POST008', 'BOARD006', 'USER005', 'Question about Dormitory', 'How can I apply for the dormitory?', false, 34, '2024-03-11 09:15:00');

-- 댓글 데이터
INSERT INTO 댓글 (댓글ID, 게시글ID, 작성자ID, 내용, 익명여부, 생성일시) VALUES
-- 공지사항 댓글
('COMMENT001', 'POST001', 'USER001', '확인했습니다.', false, '2024-02-25 09:30:00'),
('COMMENT002', 'POST001', 'USER002', '감사합니다!', false, '2024-02-25 10:00:00'),

-- 자유게시판 댓글
('COMMENT003', 'POST005', 'USER003', '오늘 먹어봐야겠네요!', true, '2024-03-11 12:45:00'),
('COMMENT004', 'POST005', 'USER004', '저도 동의합니다 ㅎㅎ', true, '2024-03-11 13:00:00'),

-- 영어 게시글 댓글
('COMMENT005', 'POST006', 'USER005', 'I''m interested! Please provide more details.', false, '2024-03-11 15:30:00'),
('COMMENT006', 'POST006', 'USER006', 'What time do you usually meet?', false, '2024-03-11 15:45:00'),

-- Q&A 댓글
('COMMENT007', 'POST007', 'USER010', '해당 내용 확인 후 조치하도록 하겠습니다.', false, '2024-03-10 17:00:00'),
('COMMENT008', 'POST008', 'USER011', 'Please check the dormitory application guide on the website.', false, '2024-03-11 09:30:00');