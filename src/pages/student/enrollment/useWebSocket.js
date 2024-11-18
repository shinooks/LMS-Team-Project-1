// useWebSocket.js
import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';

const useWebSocket = (studentId, classes, onEnrollmentResult, onCourseStatusUpdate) => {
    const [stompClient, setStompClient] = useState(null);

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

                    if (result.success) {
                        onEnrollmentResult();
                    }
                });

                // 강의별 상태 업데이트 구독
                if (classes.length > 0) {
                    classes.forEach(course => {
                        client.subscribe(`/topic/course-status/${course.openingId}`, message => {
                            const update = JSON.parse(message.body);
                            console.log("강의 상태 업데이트: ", update);
                            onCourseStatusUpdate(update);
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
    }, [studentId, classes]);

    return stompClient;
};

export default useWebSocket;