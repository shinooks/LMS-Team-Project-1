import { useEffect, useState } from 'react'
import './App.css'
import axios from "axios";
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function App() {


  const [stompClient, setStompClient] = useState(null);
  // STOMP: Simple Text Oriented Messaging Protocol 
  // 메시지 전송을 위한 간단한 텍스트 기반 프로토콜
  // WebSocket 위에서 동작하는 메시징 프로토콜
  const [currentEnrollments, setCurrentEnrollments] = useState({})
  const [classes, setClasses] = useState([]);
  const [enrolledClasses, setEnrolledClasses] = useState([]);
  const [myTimeTable, setMyTimeTable] = useState(Array(9).fill().map(() => Array(5).fill(null)));
  const [studentId, setStudentId] = useState("dcd7ef04-84f2-44d1-8dbf-48ba37da9230");
  // 2번째 학생 : 02195b9b-6654-4037-9e78-f60f90f9356b

  useEffect(() => {
    requestData();
    getAllClasses();
  }, [studentId]);

  // WebSocket 연결 설정
  useEffect(() => {
    console.log("Attempting WebSocket connection...");

    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8081/ws-enrollment'),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    client.onConnect = () => {
      console.log("WebSocket Connected Successfully!");

      setStompClient(client);

      // 1. 수강신청 결과 구독
      client.subscribe(`/user/topic/enrollment-result`, message => {
        const result = JSON.parse(message.body);
        console.log("수강신청 결과: ", result);

        alert(result.message);

        if (result.success) {
          Promise.all([
            requestData(),
            getAllClasses()
          ]);
        }

      });

      // 2. 강의별 상태 업데이트 구독
      if (classes.length > 0) {
        classes.forEach(course => {
          client.subscribe(
            `/topic/course-status/${course.openingId}`,
            message => {
              const update = JSON.parse(message.body);
              console.log("강의 상태 업데이트: ", update);
              setCurrentEnrollments(prev => ({
                ...prev,
                [update.openingId]: update.currentEnrollment
              }));
            }
          );
        });
      }
    };

    client.activate();

    return () => {
      if (client.active) {
        client.deactivate();
      }
    };
  }, [studentId, classes]);

  const requestData = async () => {
    try {
      const res = await axios.get(`http://localhost:8081/myclasslist/${studentId}`);
      if (res.status === 200) {
        setEnrolledClasses(res.data.myClassList);

        setMyTimeTable(res.data.myTimeTable.map(row =>
          row.map(cell => cell ? {
            classId: cell.courseCode,
            className: cell.courseName
          } : null)
        ));
      }
    } catch (error) {
      console.log("데이터 갱신 중 오류 발생:", error)
    }
  }

  const getAllClasses = async () => {
    try {
      const res = await axios.get(`http://localhost:8081/allclasses`);
      if (res.status === 200) {
        setClasses(res.data.allClasses);
      }
    } catch (error) {
      console.error("강의 목록 조회 실패:", error);
    }
  };

  // 관심강의 등록 함수
  const enroll = async (classes) => {
    try {
      await axios.post("http://localhost:8081/enrollment", {
        studentId: studentId,
        // 전체 강의 배열에 담겨져있는 courseCode를 백으로 보냄
        openingId: classes.openingId
      });
      console.log("수강신청 요청 전송됨");
      // 실제 성공/실패는 WebSocket으로 받을 예정이므로 여기서는 데이터 갱신하지 않음

    } catch (error) {
      console.error("수강신청 요청 실패: ", error);
      alert("수강시청 요청 중 오류가 발생했습니다")
    }
  };

  // 관심강의 삭제 함수
  const deleteClass = async (enrollmentId) => {
    try {
      await axios.delete(`http://localhost:8081/myclasslist/delete/${enrollmentId}`);
      await Promise.all([requestData(), getAllClasses()]);

    } catch (error) {
      console.error("삭제 중 오류 발생:", error);
      alert("삭제 중 오류가 발생했습니다.")
    }
  };

  // studentId를 변경하는 함수
  const changeStudentId = async (newId) => {
    setStudentId(newId); // 새로운 studentId로 업데이트
  }

  return (

    <div style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>

      {/* studentId 선택 버튼 */}
      <div>
        <button onClick={() => changeStudentId("dcd7ef04-84f2-44d1-8dbf-48ba37da9230")}>학생 ID: aaa</button>
        <button onClick={() => changeStudentId("02195b9b-6654-4037-9e78-f60f90f9356b")}>학생 ID: bbb</button>
      </div>


      <div>
        <h2 style={{ borderBottom: '2px solid blue' }}>강의 리스트</h2>
        {classes.length === 0 ? (
          <div>강의가 없습니다.</div>
        ) : (
          // 강의코드 기준으로 정렬
          [...classes].sort((a, b) => a.courseCode.localeCompare(b.courseCode)).map((classes, index) => (
            <div key={classes.openingId} style={{ margin: '10px 0' }}> {/* key를 index에서 openingId로 변경 */}
              강의코드: {classes.courseCode}&nbsp;&nbsp;&nbsp;
              강의명: {classes.courseName}&nbsp;&nbsp;&nbsp;
              학점: {classes.credit}&nbsp;&nbsp;&nbsp;
              요일: {classes.day}&nbsp;&nbsp;&nbsp;
              시작시간: {classes.startTime} ~
              끝시간: {classes.endTime}&nbsp;&nbsp;&nbsp;
              최대 수강인원: {classes.maxStudents}&nbsp;&nbsp;&nbsp;
              현재 수강인원: {
                currentEnrollments[classes.openingId] !== undefined
                  ? currentEnrollments[classes.openingId]
                  : classes.currentStudents}&nbsp;&nbsp;&nbsp;
              <button onClick={() => enroll(classes)}>신청</button>
            </div>
          ))
        )}
      </div>

      <div style={{ flex: 1, marginRight: '20px' }}>
        <h2 style={{ borderBottom: '2px solid blue' }}>시간표</h2>
        <table style={{ width: "600px", textAlign: "center", borderCollapse: "collapse" }}>
          <thead>
            <tr>
              <th style={{ border: "1px solid black", padding: "8px" }}>교시</th>
              {['월', '화', '수', '목', '금'].map((day, index) => (
                <th key={index} style={{ border: "1px solid black", padding: "8px" }}>{day}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {Array.from({ length: 9 }).map((_, row) => (
              <tr key={row}>
                <td style={{ border: "1px solid black", padding: "8px" }}>{row + 1}교시</td>
                {Array.from({ length: 5 }).map((_, col) => (
                  <td key={col} style={{
                    border: "1px solid black",
                    padding: "8px",
                    height: "50px",
                    backgroundColor: myTimeTable[row][col] ? "#e3f2fd" : "white",
                    fontsize: "0.9em"
                  }}>
                    {myTimeTable[row][col] && (
                      <>
                        <div>강의번호: {myTimeTable[row][col].classId}</div>
                        <div>{myTimeTable[row][col].className}</div>
                      </>
                    )}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div style={{ flex: 1 }}>
        <h2 style={{ borderBottom: '2px solid blue' }}>신청 목록</h2>
        {enrolledClasses.length === 0 ? (
          <div>신청한 강의가 없습니다.</div>
        ) : (
          enrolledClasses.map((enrolledClass, index) => (
            <div key={index} style={{ margin: '10px 0' }}>
              강의번호: {enrolledClass.courseCode}&nbsp;&nbsp;&nbsp;
              강의명: {enrolledClass.courseName}&nbsp;&nbsp;&nbsp;
              학점: {enrolledClass.credit}&nbsp;&nbsp;&nbsp;
              요일: {enrolledClass.day}&nbsp;&nbsp;&nbsp;
              시작시간: {enrolledClass.startTime} ~
              끝시간: {enrolledClass.endTime}&nbsp;&nbsp;&nbsp;
              최대인원: {enrolledClass.maxStudents}&nbsp;&nbsp;&nbsp;
              <button onClick={() => deleteClass(enrolledClass.enrollmentId)}>삭제</button>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default App;