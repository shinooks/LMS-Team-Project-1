import { useEffect, useState } from 'react'
import './App.css'
import axios from "axios";

function App() {
  const [classes, setClasses] = useState([]);
  // mySqlDB
  // 수강 강의 목록
  const [enrolledClasses, setEnrolledClasses] = useState([]);
  // 관심강의 목록 상태 redisDb
  const [interestCourses, setinterestCourses] = useState([]);
  const [interestTimeTable, setInterestTimeTable] = useState(Array(9).fill().map(() => Array(5).fill(null)));
  const [studentId, setStudentId] = useState("dcd7ef04-84f2-44d1-8dbf-48ba37da9230");
  // 2번째 학생 : 02195b9b-6654-4037-9e78-f60f90f9356b
    // 관심강의로 검색하시면 관심강의 등록에 대한 함수들을 찾으실 수 있습니다.

  // 관심강의 목록의 상태
  useEffect(() => {
    //requestData();
    requestInterestData();
    requestInterestTimeTable();
  }, [studentId]);

  // 전체강의 목록의 상태
  useEffect(() => {
    getAllClasses();
  }, []);

  // studentId를 변경하는 함수
  const changeStudentId = (newId) => {
    setStudentId(newId); // 새로운 studentId로 업데이트
  }

  // 전체 강의 목록 가져오는 함수
  const getAllClasses = () => {
    try {
      axios.get(`http://localhost:8081/allclasses`).then(function (res) {
        if (res.status === 200) {
          setClasses(res.data.allClasses);
        }
      })
    } catch (error) {
      console.error("Error fetching all classes:", error);
    }
  };

  // 전체 강의 목록에서 강의등록 함수
  const enroll = (classes) => {
    axios.post("http://localhost:8081/enrollment", {
      studentId: studentId,
      // 전체 강의 배열에 담겨져있는 courseCode를 백으로 보냄
      openingId: classes.openingId
    })
        .then(function (res) {
          if (res.status === 200) {
            alert("수강신청 성공");
            //requestData();
          } else {
            console.log("비정상응답");
          }
        })
        .catch(function (error) {
          // 오류가 발생했을 때
          if (error.response) {
            // 서버가 응답했지만 상태 코드가 2xx가 아닌 경우
            if (error.response.status === 409) {
              alert("시간이 겹치는 강의가 이미 등록되어 있습니다.");
            } else {
              alert("오류 발생: " + error.response.data.message); // 다른 오류 메시지 처리
            }
          } else if (error.request) {
            // 요청이 이루어졌지만 응답을 받지 못한 경우
            alert("서버 응답이 없습니다.");
          } else {
            // 오류를 발생시킨 요청 설정 중에 문제가 발생한 경우
            alert("오류: " + error.message);
          }
        });
  }

  //관심강의 관련 함수-------------------------------------------------------------------------------------------------
  // 관심강의 목록에 강의 등록
  const saveInterest = (classes) => {
    axios.post("http://localhost:8081/saveStudentInterest", {
      studentId: studentId,
      openingId: classes.openingId
    })
        .then((res) => {
          // 성공적으로 등록된 경우
          if (res.status === 201) {
            alert("관심강의 등록 성공");
            requestInterestData();
            requestInterestTimeTable();
          }
        })
        .catch((error) => {
          // 에러 발생 시 상태 코드 확인
          if (error.response) {
            // 서버가 응답을 반환했지만 상태 코드가 2xx가 아닌 경우
            if (error.response.status === 409) {
              alert("이미 관심강의에 등록된 강의입니다.");
            } else {
              alert("관심강의 등록 실패");
            }
          } else {
            // 요청이 이루어지지 않았거나 다른 오류
            alert("서버와 연결할 수 없습니다.");
          }
        });
  }

  // redisDB에서 관심강의 목록을 가져오는 함수
  const requestInterestData = () => {
    axios.get(`http://localhost:8081/interestList/${studentId}`).then(function (res) {
      if (res.status === 200){
        setinterestCourses(res.data);
      }
    })
  }
  
  // 관심강의 데이터 시간표에 세팅하는 함수
  const requestInterestTimeTable = () =>{
    axios.get(`http://localhost:8081/interestTimeTable/${studentId}`)
        .then(function (res) {
          if(res.status === 200){
            const formattedTimeTable = res.data.interestTimeTable.map(row => row.map(cell => {
                  if (cell) {
                    return {
                      classId: cell.courseCode,
                      className: cell.courseName
                    };
                  }
                  return null;
                })
            );
            setInterestTimeTable(formattedTimeTable);
          }
        })
  }

  // 관심강의 리스트에서 삭제
  const deleteInterest = (interestCourses) => {
    axios.delete(`http://localhost:8081/deleteStudentInterest/${studentId}/${interestCourses.openingId}`)
        .then((res) => {
          if (res.status === 200) {
            alert("관심과목 삭제 성공")
            requestInterestData();
            requestInterestTimeTable(); // 시간표도 업데이트
          }
        })
  }

  // 관심강의 목록에서 수강신청
  const enrollInInterest = (interestCourses) => {
    axios.post("http://localhost:8081/enrollment", {
      studentId: studentId,
      // 전체 강의 배열에 담겨져있는 courseCode를 백으로 보냄
      openingId: interestCourses.openingId
    })
        .then(function (res) {
          if (res.status === 200) {
            alert("수강신청 성공");
            //requestData();
          } else {
            console.log("비정상응답");
          }
        })
        .catch(function (error) {
          // 오류가 발생했을 때
          if (error.response) {
            // 서버가 응답했지만 상태 코드가 2xx가 아닌 경우
            if (error.response.status === 409) {
              alert("시간이 겹치는 강의가 이미 등록되어 있습니다.");
            } else {
              alert("오류 발생: " + error.response.data.message); // 다른 오류 메시지 처리
            }
          } else if (error.request) {
            // 요청이 이루어졌지만 응답을 받지 못한 경우
            alert("서버 응답이 없습니다.");
          } else {
            // 오류를 발생시킨 요청 설정 중에 문제가 발생한 경우
            alert("오류: " + error.message);
          }
        });
  }
//-------------------------------------------------------------------------------------------------------------
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
          classes.map((classes, index) => (
              <div key={index} style={{margin: '10px 0'}}>
                강의코드: {classes.courseCode}&nbsp;&nbsp;&nbsp;
                강의명: {classes.courseName}&nbsp;&nbsp;&nbsp;
                학점: {classes.credit}&nbsp;&nbsp;&nbsp;
                요일: {classes.day}&nbsp;&nbsp;&nbsp;
                시작시간: {classes.startTime} ~
                끝시간: {classes.endTime}&nbsp;&nbsp;&nbsp;
                최대 수강인원: {classes.maxStudents}&nbsp;&nbsp;&nbsp;
                현재 수강인원: {classes.currentStudents}&nbsp;&nbsp;&nbsp;
                <button onClick={() => saveInterest(classes)}>관심강의 등록</button>
                <button onClick={() => enroll(classes)}>신청</button>
              </div>
          ))
        )}
      </div>

      <div style={{flex: 1, marginRight: '20px' }}>
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
                    backgroundColor: interestTimeTable[row][col] ? "#e3f2fd" : "white",
                    fontsize: "0.9em"
                  }}>
                    {interestTimeTable[row][col] && (
                      <>
                        <div>강의번호: {interestTimeTable[row][col].classId}</div>
                        <div>{interestTimeTable[row][col].className}</div>
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
        <h2 style={{ borderBottom: '2px solid blue' }}>관심강의 목록</h2>
        {interestCourses.length === 0 ? (
          <div>관심등록한 강의가 없습니다.</div>
        ) : (
          interestCourses.map((interestCourses, index) => (
              <div key={index} style={{margin: '10px 0'}}>
                강의번호: {interestCourses.courseCode}&nbsp;&nbsp;&nbsp;
                강의명: {interestCourses.courseName}&nbsp;&nbsp;&nbsp;
                학점: {interestCourses.credits}&nbsp;&nbsp;&nbsp;
                요일: {interestCourses.timeInfo.day}&nbsp;&nbsp;&nbsp;
                시작시간: {interestCourses.timeInfo.startTime} ~
                끝시간: {interestCourses.timeInfo.endTime}&nbsp;&nbsp;&nbsp;
                <button onClick={() => deleteInterest(interestCourses)}>관심강의 삭제</button>
                <button onClick={() => enrollInInterest(interestCourses)}>신청</button>
              </div>
          ))
        )}
      </div>
    </div>
  );

}


export default App;