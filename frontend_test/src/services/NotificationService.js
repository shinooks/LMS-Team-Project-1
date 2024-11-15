class NotificationService {
  constructor(studentId) {
    this.studentId = studentId;
    this.eventSource = null;
  }

  connect() {
    if (this.eventSource) {
      this.disconnect();
    }

    this.eventSource = new EventSource(`http://localhost:8081/notifications/subscribe/${this.studentId}`);

    this.eventSource.onmessage = (event) => {
      try {
        const notification = JSON.parse(event.data);
        this.handleNotification(notification);
        if (notification.type === 'GRADE') {
          window.dispatchEvent(new CustomEvent('gradeUpdated'));
        }
      } catch (error) {
        console.error('알림 처리 중 오류 발생:', error);
      }
    };

    this.eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error);
      this.disconnect();
      setTimeout(() => this.connect(), 5000);
    };
  }

  disconnect() {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }

  handleNotification(notification) {
    const event = new CustomEvent('notification', { detail: notification });
    window.dispatchEvent(event);
  }

  async getNotifications() {
    try {
      const response = await fetch(`http://localhost:8081/notifications/student/${this.studentId}`);
      console.log("알림")
      if (!response.ok) {
        console.log("알림 성공")
        throw new Error('알림 목록 조회 실패');
      }
      return await response.json();
    } catch (error) {
      console.error('알림 목록 조회 중 오류 발생:', error);
      return [];
    }
  }
}

export default NotificationService;