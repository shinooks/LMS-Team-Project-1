import { useState, useEffect } from 'react';
import NotificationService from '../services/NotificationService';

export function useNotification(studentId) {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const notificationService = new NotificationService(studentId);

    const handleNotification = (event) => {
      const newNotification = event.detail;
      setNotifications(prevNotifications => {
        const isDuplicate = prevNotifications.some(
          notification => notification.content === newNotification.content
        );
        if (isDuplicate) return prevNotifications;
        return [newNotification, ...prevNotifications];
      });
    };

    window.addEventListener('notification', handleNotification);
    notificationService.connect();

    // 초기 알림 목록 로드
    notificationService.getNotifications()
      .then(notifications => setNotifications(notifications));

    return () => {
      window.removeEventListener('notification', handleNotification);
      notificationService.disconnect();
    };
  }, [studentId]);

  return { notifications };
}