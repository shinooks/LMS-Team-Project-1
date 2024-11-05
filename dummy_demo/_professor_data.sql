-- --------------------------------------------------------
-- 교수 관련 데이터 생성
-- 생성 날짜: 2024-03-11
-- --------------------------------------------------------

-- 교수 기본 데이터
INSERT INTO 교수 (교수ID, 사용자ID, 교번, 이름, 학과ID) VALUES
-- 한국 교수
('PROF001', 'USER007', 'P202401', '김도현', 'DEPT001'),
('PROF002', 'USER008', 'P202402', '박성준', 'DEPT002'),
('PROF003', 'USER016', 'P202403', '이미란', 'DEPT003'),
('PROF004', 'USER017', 'P202404', '정태우', 'DEPT005'),
-- 외국인 교수
('PROF005', 'USER009', 'P202405', 'Michael Anderson', 'DEPT004'),
('PROF006', 'USER018', 'P202406', 'Sarah Williams', 'DEPT007'),
('PROF007', 'USER019', 'P202407', 'David Chen', 'DEPT006');

-- 교수 상세정보
INSERT INTO 교수상세정보 (교수ID, 연구실위치, 상담시간, 연구분야, 프로필이미지URL) VALUES
                                                            ('PROF001', '공학관 401호', '월,수 13:00-15:00', '인공지능, 빅데이터', '/profiles/prof001.jpg'),
                                                            ('PROF002', '경영관 308호', '화,목 14:00-16:00', '재무관리, 투자론', '/profiles/prof002.jpg'),
                                                            ('PROF003', '공학관 502호', '월,화 10:00-12:00', '반도체설계, IoT', '/profiles/prof003.jpg'),
                                                            ('PROF004', '공학관 601호', '수,금 15:00-17:00', '로봇공학, 자동제어', '/profiles/prof004.jpg'),
                                                            ('PROF005', '인문관 208호', '화,목 14:00-16:00', 'English Literature, Cultural Studies', '/profiles/prof005.jpg'),
                                                            ('PROF006', '국제관 305호', '월,수 11:00-13:00', 'International Trade, Economics', '/profiles/prof006.jpg'),
                                                            ('PROF007', '공학관 701호', '화,금 13:00-15:00', 'Chemical Process, Green Technology', '/profiles/prof007.jpg');

-- 교수 논문
INSERT INTO 교수논문 (논문ID, 교수ID, 제목, 저자목록, 출판일, 학술지, DOI) VALUES
                                                           ('PAPER001', 'PROF001', '딥러닝 기반 학습자 행동 분석 연구', '김도현, 박연구', '2024-01-15', '한국정보과학회논문지', 'DOI001'),
                                                           ('PAPER002', 'PROF001', 'AI in Education: A Comprehensive Study', '김도현, John Smith, Sarah Park', '2023-11-20', 'International Journal of AI', 'DOI002'),
                                                           ('PAPER003', 'PROF005', 'Modern Literature in Digital Age', 'Michael Anderson, Emma White', '2024-02-10', 'Journal of English Literature', 'DOI003'),
                                                           ('PAPER004', 'PROF002', '디지털 경제시대의 투자전략 연구', '박성준, 김경제', '2024-03-01', '한국경영학회지', 'DOI004'),
                                                           ('PAPER005', 'PROF006', 'Global Trade Patterns in Post-Pandemic Era', 'Sarah Williams, David Lee', '2024-01-25', 'International Economics Review', 'DOI005'),
                                                           ('PAPER006', 'PROF003', '차세대 반도체 설계 방법론', '이미란, 정기술', '2024-02-15', '대한전자공학회논문지', 'DOI006'),
                                                           ('PAPER007', 'PROF007', 'Green Chemistry Solutions', 'David Chen, Lisa Park', '2024-03-05', 'Chemical Engineering Journal', 'DOI007');

-- 교수 연구프로젝트
INSERT INTO 교수연구프로젝트 (프로젝트ID, 교수ID, 프로젝트명, 시작일, 종료일, 연구비, 설명) VALUES
                                                                  ('PROJ001', 'PROF001', '차세대 AI 교육 플랫폼 개발', '2024-03-01', '2025-02-28', 300000000, 'AI 기술을 활용한 맞춤형 교육 시스템 개발'),
                                                                  ('PROJ002', 'PROF005', 'Digital Humanities Research', '2024-01-01', '2024-12-31', 150000000, 'Investigating the impact of digital technology on literature'),
                                                                  ('PROJ003', 'PROF002', '핀테크 보안 시스템 연구', '2024-04-01', '2025-03-31', 250000000, '블록체인 기반 금융 보안 시스템 개발'),
                                                                  ('PROJ004', 'PROF003', '차세대 반도체 설계', '2024-03-01', '2025-08-31', 500000000, '저전력 고성능 반도체 설계 기술 개발'),
                                                                  ('PROJ005', 'PROF006', 'Global Supply Chain Analysis', '2024-05-01', '2025-04-30', 200000000, 'Post-pandemic supply chain resilience study'),
                                                                  ('PROJ006', 'PROF004', '자율주행 로봇 제어 시스템', '2024-06-01', '2025-05-31', 400000000, 'AI 기반 로봇 제어 시스템 개발'),
                                                                  ('PROJ007', 'PROF007', '친환경 화학공정 개발', '2024-04-01', '2025-09-30', 350000000, '탄소중립을 위한 그린 케미스트리 연구');