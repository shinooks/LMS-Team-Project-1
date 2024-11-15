import { useEffect, useState } from 'react';
import { useNotification } from '../../hooks/useNotification';

function NotificationList({ studentId }) {
  const [grades, setGrades] = useState([]);
  const { notifications } = useNotification(studentId);

  // 성적 데이터 가져오기
  const fetchGrades = async () => {
    try {
      const response = await fetch(`http://localhost:8081/grades/student/${studentId}`);
      if (response.ok) {
        const data = await response.json();
        setGrades(data);
      }
    } catch (error) {
      console.error('성적 조회 실패:', error);
    }
  };

  useEffect(() => {
    fetchGrades();
    
    // gradeUpdated 이벤트 리스너 등록
    const handleGradeUpdate = () => {
      fetchGrades(); // 성적 데이터 새로고침
    };
    
    window.addEventListener('gradeUpdated', handleGradeUpdate);
    
    return () => {
      window.removeEventListener('gradeUpdated', handleGradeUpdate);
    };
  }, [studentId]);

  return (
    <div>
      <div className="notifications">
        <h2>알림 목록</h2>
        {notifications.map((notification, index) => (
          <div key={index} className="notification-item">
            <h3>{notification.title}</h3>
            <p>{notification.content}</p>
          </div>
        ))}
      </div>
      
      <div className="grades">
        <h2>성적 목록</h2>
        {grades.map((grade, index) => (
          <div key={index} className="grade-item">
            <p>{grade.courseName}: {grade.totalScore}점</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default NotificationList;