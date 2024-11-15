import { useEffect, useState } from 'react';
import './App.css';
import NotificationList from './components/Notification/NotificationList';

function App() {
  const [studentId, setStudentId] = useState("eeeeeeee-1111-1111-1111-111111111111");

  // studentId를 변경하는 함수
  const changeStudentId = (newId) => {
    setStudentId(newId);
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>알림 테스트</h1>

      {/* studentId 선택 버튼 */}
      <div style={{ marginBottom: '20px' }}>
        <button onClick={() => changeStudentId("eeeeeeee-1111-1111-1111-111111111111")}>
          학생 1
        </button>
        <button onClick={() => changeStudentId("eeeeeeee-2222-2222-2222-222222222222")}>
          학생 2
        </button>
      </div>

      {/* 알림 목록 */}
      <NotificationList studentId={studentId} />
    </div>
  );
}

export default App;