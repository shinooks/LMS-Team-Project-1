import React from 'react';
import './NotificationItem.css';

function NotificationItem({ notification }) {
  const getNotificationIcon = (type) => {
    switch (type) {
      case 'GRADE':
        return '📊';
      case 'GRADE_APPEAL':
        return '✋';
      case 'ASSIGNMENT':
        return '📝';
      case 'ANNOUNCEMENT':
        return '📢';
      default:
        return '🔔';
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('ko-KR');
  };

  return (
    <div className={`notification-item ${notification.isRead ? 'read' : 'unread'}`}>
      <div className="notification-icon">
        {getNotificationIcon(notification.type)}
      </div>
      <div className="notification-content">
        <h4 className="notification-title">{notification.title}</h4>
        <p className="notification-message">{notification.content}</p>
        <span className="notification-time">
          {formatDate(notification.createdAt)}
        </span>
      </div>
    </div>
  );
}

export default NotificationItem;