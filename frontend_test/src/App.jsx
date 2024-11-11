import { useEffect, useState } from 'react'
import './App.css'
import axios from "axios";

function App() {
  const [classes, setClasses] = useState([]);
  const [enrolledClasses, setEnrolledClasses] = useState([]);
  const [myTimeTable, setMyTimeTable] = useState(Array(9).fill().map(() => Array(5).fill(null)));
  const [studentId, setStudentId] = useState("dcd7ef04-84f2-44d1-8dbf-48ba37da9230");
  // 2번째 학생 : 02195b9b-6654-4037-9e78-f60f90f9356b

  useEffect(() => {
    requestData();
  }, [studentId]);

  useEffect(() => {
    getAllClasses();
  }, []);

  const requestData = () => {
<<<<<<< HEAD
    console.log(studentId === "dcd7ef04-84f2-44d1-8dbf-48ba37da9230" ? "aaa 학생" : "bbb 학생");
    
=======

>>>>>>> d54f7c357a10ea6352a75d159528718d92d75cb4
    axios.get(`http://localhost:8081/myclasslist/${studentId}`).then(function (res) {
      if (res.status === 200) {
        setEnrolledClasses(res.data.myClassList);
        console.log("myClassList : " + enrolledClasses);

        // const formattedTimeTable = res.data.myTimeTable.map(row => row.map(cell => {
        //   if (cell) {
        //     return {
        //       classId: cell.classes.classId,
        //       className: cell.classes.className
        //     };
        //   }
        //   return null;
        // })
        // );
        const formattedTimeTable = res.data.myTimeTable ? res.data.myTimeTable.map(row => row.map(cell => {
          if (cell) {
            return {
              classId: cell.classes.classId,
              className: cell.classes.className
            };
          }
          return null;
        })) : Array(9).fill().map(() => Array(5).fill(null)); // 기본값 설정


        setMyTimeTable(formattedTimeTable);
        console.log(res.data);
        //return null;
      }
    });
  }

  const getAllClasses = () => {
    try {
      axios.get(`http://localhost:8081/allclasses`).then(function (res) {
        if (res.status === 200) {
          console.log(res.data);
          setClasses(res.data.allClasses);
        }
      })
    } catch (error) {
      console.error("Error fetching all classes:", error);
    }
  };

  // 관심강의 등록 함수
  const enroll = (classes) => {
    console.log(studentId)
    console.log(classes.openingId)

    axios.post("http://localhost:8081/enrollment", {
      studentId: studentId,
      // 전체 강의 배열에 담겨져있는 courseCode를 백으로 보냄
<<<<<<< HEAD
      classId: classes[0]
=======
      openingId: classes.openingId
>>>>>>> d54f7c357a10ea6352a75d159528718d92d75cb4
    })
      .then(function (res) {
        if (res.status === 200) {
          console.log("정상응답");
          // requestData();
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
    axios.delete(`http://localhost:8081/myclasslist/delete/${enrollmentId}`, {
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
        <button onClick={() => changeStudentId("dcd7ef04-84f2-44d1-8dbf-48ba37da9230")}>학생 ID: aaa</button>
        <button onClick={() => changeStudentId("02195b9b-6654-4037-9e78-f60f90f9356b")}>학생 ID: bbb</button>
      </div>


      <div>
        <h2 style={{ borderBottom: '2px solid blue' }}>강의 리스트</h2>
        {classes.length === 0 ? (
          <div>강의가 없습니다.</div>
        ) : (
          classes.map((classes, index) => (
            <div key={index} style={{ margin: '10px 0' }}>
<<<<<<< HEAD
              강의코드: {classes[1]}&nbsp;&nbsp;&nbsp;
              강의명: {classes[2]}&nbsp;&nbsp;&nbsp;
              학점: {classes[3]}&nbsp;&nbsp;&nbsp;
              요일: {classes[4]}&nbsp;&nbsp;&nbsp;
              시작시간: {classes[5]} ~
              끝시간: {classes[6]}&nbsp;&nbsp;&nbsp;
=======
              강의코드: {classes.courseCode}&nbsp;&nbsp;&nbsp;
              강의명: {classes.courseName}&nbsp;&nbsp;&nbsp;
              학점: {classes.credit}&nbsp;&nbsp;&nbsp;
              요일: {classes.day}&nbsp;&nbsp;&nbsp;
              시작시간: {classes.startTime} ~
              끝시간: {classes.endTime}&nbsp;&nbsp;&nbsp;
>>>>>>> d54f7c357a10ea6352a75d159528718d92d75cb4
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
        {/*{enrolledClasses.length === 0 ? (*/}
        {/*  <div>신청한 강의가 없습니다.</div>*/}
        {/*) : (*/}
        {/*  enrolledClasses.map((enrolledClass, index) => (*/}
        {/*    <div key={index} style={{ margin: '10px 0' }}>*/}
        {/*      강의번호: {enrolledClass.classes.classId}&nbsp;&nbsp;&nbsp;*/}
        {/*      강의명: {enrolledClass.classes.className}&nbsp;&nbsp;&nbsp;*/}
        {/*      최대인원: {enrolledClass.classes.maxEnrollments}&nbsp;&nbsp;&nbsp;*/}
        {/*      학점: {enrolledClass.classes.credit}&nbsp;&nbsp;&nbsp;*/}
        {/*      요일: {enrolledClass.classes.day}&nbsp;&nbsp;&nbsp;*/}
        {/*      시작시간: {enrolledClass.classes.startTime} ~*/}
        {/*      끝시간: {enrolledClass.classes.endTime}&nbsp;&nbsp;&nbsp;*/}
        {/*      <button onClick={() => deleteClass(enrolledClass.enrollmentId)}>삭제</button>*/}
        {/*    </div>*/}
        {/*  ))*/}
        {/*)}*/}
      </div>
    </div>
  );

}


export default App;