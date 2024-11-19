import axios from "axios";

export const enrollmentAPI = {
  // 2024/11/16 Gnuke
  // 다른 분들이 getAllCourses를 어떻게 쓰실 지
  // 몰라서 모든 강의에 대한 정보를 가져오는 API를 새로 생성했습니다.

  // 모든 강의 정보를 가져오는 API
  getAllCourses: async () => {
    try {
      const response = await axios.get('http://localhost:8081/allcourses');
      if (response.status === 200) {
        // axios를 사용할 때는 json()이 아닌 data를 반환해야 한다고 합니다
        return response.data.allCourses;
      }
    } catch (error) {
      console.log("강의 목록 조회 실패 : ", error);
      throw error;
      // 백에서 던질 exception에 따라 다른 내용을 출력하도록 바꾸면 좋을지도..?
    }
  },

  // 수강신청 실행 API
  // 2024/11/16 gnuke
  enrollCourse: async (studentId, course) => {
    try {
      // 각 강의에 대해 수강신청 요청 보내기
      // 이렇게 courses를 배열화 하면 여러 강의에 대한 수강신청을
      // 한 번에 실행할 수 있는 기능을 구현할 수도 있을 거 같습니다.
      await axios.post('http://localhost:8081/enrollment', {
        studentId: studentId,
        openingId: course.openingId
      });
      console.log("수강신청 요청 전송됨");
      // 모든 수강신청 요청이 완료될 때까지 기다림
      // await Promise.all(enrollList);
    } catch (error) {
      console.error("수강신청 요청 실패 : ", error);
      alert("수강시청 요청 중 오류가 발생했습니다")
    }
  },

  getEnrollments: async () => {
    const response = await fetch('/api/enrollments');
    if (!response.ok) throw new Error('Failed to fetch enrollments');
    return response.json();
  },

  // 수강 취소
  deleteCourse: async (enrollmentId) => {

  },

  // 학생의 수강 목록 조회
  getStudentEnrollments: async (studentId) => {
    const response = await fetch(`/api/students/${studentId}/enrollments`);
    if (!response.ok) throw new Error('Failed to fetch student enrollments');
    return response.json();
  },

  // 수강신청 가능 여부 확인
  checkEnrollmentEligibility: async (studentId, courseId) => {
    const response = await fetch(`/api/enrollments/check?studentId=${studentId}&courseId=${courseId}`);
    if (!response.ok) throw new Error('Failed to check enrollment eligibility');
    return response.json();
  },

  // 선수과목 확인
  checkPrerequisites: async (studentId, courseId) => {
    const response = await fetch(`/api/enrollments/prerequisites?studentId=${studentId}&courseId=${courseId}`);
    if (!response.ok) throw new Error('Failed to check prerequisites');
    return response.json();
  },

  // 시간표 충돌 확인
  checkTimeConflict: async (studentId, courseId) => {
    const response = await fetch(`/api/enrollments/timeconflict?studentId=${studentId}&courseId=${courseId}`);
    if (!response.ok) throw new Error('Failed to check time conflict');
    return response.json();
  },

  // 학생 상태 조회
  getStatus: async () => {
    // 예시: mock 데이터 반환
    return {
      user: {
        id: 1,
        semester: '2024-1', // 예시 데이터
      },
    };
  },

  // 학생의 수강 내역 조회
  getHistory: async (semester, studentId) => {
    const response = await fetch(`/api/students/history?semester=${semester}&studentId=${studentId}`);
    if (!response.ok) throw new Error('Failed to fetch enrollment history');
    return response.json();
  }
};

export default enrollmentAPI;