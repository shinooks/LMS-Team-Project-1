import { useEffect, useState } from 'react'
import './App.css'
import axios from "axios";

function App() {
  const [classes, setClasses] = useState([]);
  const [enrolledClasses, setEnrolledClasses] = useState([]);
  const [myTimeTable, setMyTimeTable] = useState(Array(9).fill().map(() => Array(5).fill(null)));
  const [studentId, setStudentId] = useState("bbb");

  useEffect(() => {
    getAllClasses();
    requestData();
  }, [studentId]);

  const requestData = () => {

    console.log(studentId);
    axios.get(`http://localhost:8081/api/myclasslist/${studentId}`).then(function (res) {
      if (res.status === 200) {
        setEnrolledClasses(res.data.myClassList);

        const formattedTimeTable = res.data.myTimeTable.map(row => row.map(cell => {
          if (cell) {
            return {
              classId: cell.classes.classId,
              className: cell.classes.className
            };
          }
          return null;
        })
        );
        setMyTimeTable(formattedTimeTable);
        console.log(res.data);
      }
    });
  }

  const getAllClasses = () => {
    try {
      axios.get(`http://localhost:8081/api/allclasses`).then(function (res) {
        if (res.status === 200) {
          setClasses(res.data.allClasses);
        }
      })
    } catch (error) {
      console.error("Error fetching all classes:", error);
    }
  };

  // 관심강의 등록 함수
  const enroll = (classes) => {
    axios.post("http://localhost:8081/api/enrollment", {
      studentId: studentId,
      className: classes.className
    })
      .then(function (res) {
        if (res.status === 200) {
          console.log("정상응답");
          requestData();
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

  // 관심강의 삭제 함수
  const deleteClass = (enrollmentId) => {
    axios.delete(`http://localhost:8081/api/myclasslist/delete/${enrollmentId}`, {
      myTimeTable: myTimeTable
    })
      .then(function (res) {
        if (res.status === 200) {
          console.log("삭제완료");
          requestData();
        }
      })
  }


  // studentId를 변경하는 함수
  const changeStudentId = (newId) => {
    setStudentId(newId); // 새로운 studentId로 업데이트
  }

  return (

    <div style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '20px' }}>

      {/* studentId 선택 버튼 */}
      <div>
        <button onClick={() => changeStudentId("aaa")}>학생 ID: aaa</button>
        <button onClick={() => changeStudentId("bbb")}>학생 ID: bbb</button>
      </div>


      <div>
        <h2 style={{ borderBottom: '2px solid blue' }}>강의 리스트</h2>
        {classes.length === 0 ? (
          <div>강의가 없습니다.</div>
        ) : (
          classes.map((classes, index) => (
            <div key={index} style={{ margin: '10px 0' }}>
              강의번호: {classes.classId}&nbsp;&nbsp;&nbsp;
              강의명: {classes.className}&nbsp;&nbsp;&nbsp;
              학점: {classes.credit}&nbsp;&nbsp;&nbsp;
              요일: {classes.day}&nbsp;&nbsp;&nbsp;
              시작시간: {classes.startTime} ~
              끝시간: {classes.endTime}&nbsp;&nbsp;&nbsp;
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
              강의번호: {enrolledClass.classes.classId}&nbsp;&nbsp;&nbsp;
              강의명: {enrolledClass.classes.className}&nbsp;&nbsp;&nbsp;
              최대인원: {enrolledClass.classes.maxEnrollments}&nbsp;&nbsp;&nbsp;
              학점: {enrolledClass.classes.credit}&nbsp;&nbsp;&nbsp;
              요일: {enrolledClass.classes.day}&nbsp;&nbsp;&nbsp;
              시작시간: {enrolledClass.classes.startTime} ~
              끝시간: {enrolledClass.classes.endTime}&nbsp;&nbsp;&nbsp;
              <button onClick={() => deleteClass(enrolledClass.enrollmentId)}>삭제</button>
            </div>
          ))
        )}
      </div>
    </div>
  );

}


export default App;