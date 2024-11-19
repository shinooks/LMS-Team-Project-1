import React, {useEffect, useState} from 'react';
import useEnrollmentService from "../useEnrollmentService";


const CartList = ({ studentId }) => {
  const [interestList, setInterestList] = useState([]);
  const {
    getInterestList,
  } = useEnrollmentService();

  // 관심강의 목록의 최신상태 가져오기
  useEffect(() => {
    const getInterestList = async () => {
      try{
        const result = await getInterestList();
        setInterestList(result);
      }catch(error){
        console.error("장바구니 목록을 가져오는 중 오류 발생 : ", error);
      }
    }
  })

  return (
    <div className="space-y-6">
      {/* 장바구니 요약 정보 */}
      <div className="bg-blue-50 p-4 rounded-lg">
        <div className="flex justify-between items-center">
          <div>
            <h3 className="text-lg font-medium text-blue-900">장바구니 현황</h3>
            <p className="text-sm text-blue-700">
              총 {cartItems.length}과목 / {totalCredits}학점
            </p>
          </div>
          {cartItems.length > 0 && (
            <button
              onClick={handleEnrollment}
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 
                       focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              수강신청
            </button>
          )}
        </div>
      </div>

      {/* 장바구니 목록 */}
      {cartItems.length === 0 ? (
        <div className="text-center py-8 text-gray-500">
          장바구니가 비어있습니다.
        </div>
      ) : (
        <div className="bg-white shadow overflow-hidden rounded-lg">
          <table className="min-w-full divide-y divide-gray-200">
            {/* ... 테이블 헤더는 동일 ... */}
            <tbody className="bg-white divide-y divide-gray-200">
              {cartItems.map((course) => (
                <tr key={course.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {course.code}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {course.name}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {course.professor}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {course.credits}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {course.enrolled}/{course.capacity}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {course.schedule.map(s =>
                      `${s.day} ${s.startTime}-${s.endTime}`
                    ).join(', ')}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button
                      onClick={() => onRemoveFromCart(course.id)}
                      className="text-red-600 hover:text-red-900"
                    >
                      삭제
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* 주의사항 */}
      <div className="bg-yellow-50 p-4 rounded-lg">
        <h4 className="text-sm font-medium text-yellow-800 mb-2">수강신청 주의사항</h4>
        <ul className="text-sm text-yellow-700 list-disc list-inside space-y-1">
          <li>장바구니에 담긴 과목이 수강신청 완료를 의미하지 않습니다.</li>
          <li>수강신청 버튼을 클릭하여 최종 신청을 완료해주세요.</li>
          <li>수강인원이 초과된 경우 신청이 거부될 수 있습니다.</li>
          <li>시간표 중복 여부를 반드시 확인해주세요.</li>
        </ul>
      </div>
    </div>
  );
};

export default CartList;