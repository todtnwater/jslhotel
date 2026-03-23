<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 결제 실패 시 세션 정리
    String orderId = request.getParameter("orderId");
    if (orderId != null && !orderId.isEmpty()) {
        HttpSession userSession = request.getSession(false);
        if (userSession != null) {
            // 임시 예약 관련 세션 모두 삭제
            userSession.removeAttribute("tempReservation_" + orderId);
            userSession.removeAttribute("customerName_" + orderId);
            userSession.removeAttribute("customerEmail_" + orderId);
            userSession.removeAttribute("customerMobilePhone_" + orderId);
            userSession.removeAttribute("breakfastOption_" + orderId);
            userSession.removeAttribute("usePoints_" + orderId);
            userSession.removeAttribute("memberRank_" + orderId);
            userSession.removeAttribute("isLoggedIn_" + orderId);
            userSession.removeAttribute("memberId_" + orderId);
            userSession.removeAttribute("orderId");
            
            System.out.println("✅ 결제 실패 - 세션 정리 완료 (orderId: " + orderId + ")");
        }
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제 실패</title>
    <link rel="stylesheet" href="style/payment.css">
</head>
<body>
    <div class="container">
        <!-- 에러 아이콘 -->
        <div class="error-icon">❌</div>
        
        <!-- 제목 -->
        <h1>결제에 실패했습니다</h1>
        <p class="subtitle">결제 처리 중 문제가 발생했습니다.</p>
        
        <!-- 에러 메시지 박스 -->
        <div class="error-message">
            <h3>🚫 오류 정보</h3>
            <p id="errorMsg">결제가 정상적으로 처리되지 않았습니다.</p>
            
            <!-- 상세 에러 정보 (있을 경우에만 표시) -->
            <div class="error-detail" id="errorDetail" style="display:none;">
                <strong>상세 내용:</strong><br>
                <span id="errorDetailText"></span>
            </div>
        </div>
        
        <!-- 에러 코드 표시 (있을 경우에만 표시) -->
        <div class="error-code-box" id="errorCodeBox" style="display:none;">
            <h4>📌 오류 코드</h4>
            <div class="error-code" id="errorCodeText"></div>
        </div>
        
        <!-- 도움말 섹션 -->
        <div class="help-text">
            <h4>💡 다음 사항을 확인해주세요</h4>
            <ul>
                <li>카드 정보(카드번호, 유효기간, CVC)가 정확한지 확인해주세요</li>
                <li>카드 한도가 충분한지 확인해주세요</li>
                <li>결제 비밀번호를 정확히 입력했는지 확인해주세요</li>
                <li>해외결제 차단 설정을 확인해주세요</li>
                <li>인터넷 연결 상태가 안정적인지 확인해주세요</li>
                <li>다른 결제 수단(계좌이체, 가상계좌 등)을 시도해보세요</li>
            </ul>
        </div>
        
        <!-- 버튼 그룹 -->
        <div class="button-group">
            <a href="javascript:goToReservation();" class="btn btn-primary">예약 페이지로 돌아가기</a>
        </div>
        
        <!-- 고객센터 안내 -->
        <p style="margin-top: 30px; color: #999; font-size: 14px;">
            문제가 계속되면 고객센터(1234-5678)로 문의해주세요.
        </p>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        /**
         * 페이지 로드 시 실행
         */
        (function() {
            console.log('=================================================');
            console.log('결제 실패 페이지 진입');
            
            // URL 파라미터에서 에러 정보 추출
            const urlParams = new URLSearchParams(window.location.search);
            const errorCode = urlParams.get('code');
            const errorMsg = urlParams.get('message');
            const orderId = urlParams.get('orderId');
            
            console.log('에러 코드:', errorCode);
            console.log('에러 메시지:', errorMsg);
            console.log('주문 ID:', orderId);
            console.log('=================================================');
            
            // 에러 메시지 표시
            if (errorMsg) {
                const decodedMsg = decodeURIComponent(errorMsg);
                document.getElementById('errorMsg').textContent = decodedMsg;
                console.log('디코딩된 메시지:', decodedMsg);
            }
            
            // 에러 코드 표시
            if (errorCode) {
                document.getElementById('errorCodeBox').style.display = 'block';
                document.getElementById('errorCodeText').textContent = errorCode;
                
                const errorCodeGuide = getErrorCodeGuide(errorCode);
                if (errorCodeGuide) {
                    document.getElementById('errorDetail').style.display = 'block';
                    document.getElementById('errorDetailText').textContent = errorCodeGuide;
                }
            }
        })();
        
        /**
         * 예약 페이지로 돌아가기 (새로 시작)
         */
        function goToReservation() {
            console.log('📤 예약 페이지로 이동 (새로 시작)');
            location.href = 'Index';
        }
        
        /**
         * 에러 코드별 안내 메시지 반환
         */
        function getErrorCodeGuide(errorCode) {
            const errorGuides = {
                'PAY_PROCESS_CANCELED': '사용자가 결제를 취소했습니다.',
                'PAY_PROCESS_ABORTED': '결제 진행 중 오류가 발생했습니다.',
                'REJECT_CARD_COMPANY': '카드사에서 승인을 거부했습니다. 카드사로 문의해주세요.',
                'INVALID_CARD_EXPIRATION': '카드 유효기간이 만료되었거나 잘못 입력되었습니다.',
                'INVALID_STOPPED_CARD': '정지된 카드입니다.',
                'EXCEED_MAX_CARD_QUOTA': '한도를 초과했습니다.',
                'INVALID_CARD_INSTALLMENT_PLAN': '할부 개월 수가 잘못되었습니다.',
                'NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT': '할부가 지원되지 않는 카드 또는 가맹점입니다.',
                'INVALID_CARD_LOST_OR_STOLEN': '분실/도난 신고된 카드입니다.',
                'RESTRICTED_TRANSFER_ACCOUNT': '계좌이체가 제한된 계좌입니다.',
                'INVALID_ACCOUNT_INFO': '계좌 정보가 올바르지 않습니다.',
                'FAILED_INTERNAL_SYSTEM_PROCESSING': '내부 시스템 처리 오류입니다. 잠시 후 다시 시도해주세요.',
                'FAILED_METHOD_HANDLING': '결제 수단 처리에 실패했습니다.',
                'FAILED_PASSWORD_AUTH': '비밀번호가 올바르지 않습니다.',
                'EXCEED_MAX_AUTH_COUNT': '인증 시도 횟수를 초과했습니다.',
                'EXCEED_MAX_ONE_DAY_AMOUNT': '1일 결제 한도를 초과했습니다.',
                'NOT_AVAILABLE_PAYMENT': '결제가 불가능한 시간입니다.'
            };
            
            return errorGuides[errorCode] || null;
        }
    </script>
</body>
</html>
