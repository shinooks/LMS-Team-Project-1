import React from 'react';
import { scheduleDetails } from '../../../../api/mock/data/scheduleDetails'; // 상세 시간표 데이터
import { scheduleStrings } from '../../../../api/mock/data/schedules'; // 간단한 시간표 문자열
import { enrollmentAPI } from '../../../../api/services/enrollmentAPI';

const CourseItem = ({ course, onAddToCart, isInCart, isEnrolled, studentId, currentEnrollments }) => {

    const enrollCourse = async (studentId, course) => {
        await enrollmentAPI.enrollCourse(studentId, course);
    }

    // scheduleDetails에서 해당 courseId에 대한 시간표 가져오기
    const formatSchedule = (courseId) => {
        const schedule = scheduleDetails[courseId];
        if (!Array.isArray(schedule)) {
            console.error('Schedule is not an array:', schedule);
            return '일정 없음'; // 기본값 반환
        }
        return schedule.map(s => `${s.day} ${s.startTime}-${s.endTime} (${s.room})`).join(', ');
    };

    return (
        <tr key={course.openingId}>
            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {course.courseCode}
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {course.courseName}
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {course.professorName}
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {course.credit}
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span className={course.currentStudents >= course.maxStudents ? 'text-red-600' : ''}>
                    {/*{currentStudents}/{maxStudents}*/}
                    {
                        currentEnrollments[course.openingId] !== undefined
                            ? `${currentEnrollments[course.openingId]}/${course.maxStudents}`
                            : `${course.currentStudents}/${course.maxStudents}`
                    }
                </span>
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {course.day} {course.startTime} - {course.endTime}
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button onClick={() => enrollCourse(studentId, course)}>
                    신청
                </button>
            </td>
            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button>
                    담기
                </button>
            </td>
        </tr>
    );
};

export default CourseItem;