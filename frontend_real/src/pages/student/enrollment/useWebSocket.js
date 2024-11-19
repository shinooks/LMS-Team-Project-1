// useWebSocket.js
import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { enrollmentAPI } from '../../../api/services';

const useWebSocket = (studentId, courses, onEnrollmentResult, onCourseStatusUpdate) => {
    const [stompClient, setStompClient] = useState(null);
    const [currentEnrollments, setCurrentEnrollments] = useState({})

    useEffect(() => {
        const client = new Client({
            webSocketFactory: () => new WebSocket('ws://localhost:8081/ws-enrollment'),
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            debug: function (str) {
                console.log('STOMP Debug', str);
            },
            onConnect: () => {
                console.log("WebSocket Connected Successfully!");
                setStompClient(client);

                // 수강신청 결과 구독
                client.subscribe(`/user/${studentId}/topic/enrollment-result`, message => {
                    const result = JSON.parse(message.body);
                    console.log("수강신청 결과: ", result);
                    alert(result.message);

                    if (result.sucess) {
                        Promise.all([
                            enrollmentAPI.getAllCourses()
                        ])

                    }
                });

                // 강의별 상태 업데이트 구독
                if (courses.length > 0) {
                    courses.forEach(course => {
                        client.subscribe(`/topic/course-status/${course.openingId}`, message => {
                            const update = JSON.parse(message.body);
                            console.log("강의 상태 업데이트: ", update);
                            setCurrentEnrollments(prev => ({
                                ...prev,
                                [update.openingId]: update.currentEnrollment
                            }))
                        });
                    });
                }
            },
            onStompError: (frame) => {
                console.error('STOMP Error: ', frame);
            },
            onWebSocketError: (event) => {
                console.error('WebSocket Error: ', event);
            }
        });

        client.activate();

        return () => {
            if (client.active) {
                client.deactivate();
            }
        };
    }, [studentId, courses]);

    return currentEnrollments;
};

export default useWebSocket;