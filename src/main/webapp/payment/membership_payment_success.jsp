<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>멤버십 결제 완료</title>
    <link rel="stylesheet" href="style/payment.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .success-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 40px;
            background: white;
            border-radius: 20px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .success-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }
        
        .success-title {
            font-size: 28px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }
        
        .success-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
        }
        
        .membership-info {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
            text-align: left;
        }
        
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #666;
        }
        
        .info-value {
            font-weight: 600;
            color: #333;
        }
        
        .button-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }
        
        .btn {
            flex: 1;
            padding: 15px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #e0e0e0;
            color: #333;
        }
        
        .btn-secondary:hover {
            background: #d0d0d0;
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon">🎉</div>
        <h1 class="success-title">멤버십 구독 완료!</h1>
        <p class="success-message">멤버십 결제가 성공적으로 완료되었습니다.</p>
        
        <div class="membership-info" id="membershipInfo">
            <div class="info-row">
                <span class="info-label">멤버십</span>
                <span class="info-value" id="membershipName">-</span>
            </div>
            <div class="info-row">
                <span class="info-label">결제 금액</span>
                <span class="info-value" id="paymentAmount">₩0</span>
            </div>
            <div class="info-row">
                <span class="info-label">유효 기간</span>
                <span class="info-value" id="validPeriod">-</span>
            </div>
            <div class="info-row">
                <span class="info-label">결제 번호</span>
                <span class="info-value" id="paymentNo">-</span>
            </div>
        </div>
        
        <div class="button-group">
            <button class="btn btn-secondary" onclick="goMembershipList()">멤버십 목록</button>
            <button class="btn btn-primary" onclick="goHome()">홈으로</button>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            console.log('=== 멤버십 결제 성공 페이지 로드 ===');
            
            const urlParams = new URLSearchParams(window.location.search);
            const paymentKey = urlParams.get('paymentKey');
            const orderId = urlParams.get('orderId');
            const amount = urlParams.get('amount');
            
            console.log('paymentKey:', paymentKey);
            console.log('orderId:', orderId);
            console.log('amount:', amount);
            
            if (!paymentKey || !orderId || !amount) {
                alert('잘못된 접근입니다.');
                goHome();
                return;
            }
            
            confirmPayment(paymentKey, orderId, parseInt(amount));
        });
        
        function confirmPayment(paymentKey, orderId, amount) {
            console.log('=== 토스 결제 승인 요청 ===');
            
            $.ajax({
                url: 'Payment',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    paymentKey: paymentKey,
                    orderId: orderId,
                    amount: amount
                }),
                success: function(response) {
                    console.log('토스 결제 승인 응답:', response);
                    
                    if (response.success) {
                        finalizeMembership(paymentKey, orderId, amount, response.method, response.status);
                    } else {
                        alert('결제 승인 실패: ' + response.message);
                        goHome();
                    }
                },
                error: function(xhr, status, error) {
                    console.error('결제 승인 오류:', error);
                    alert('결제 승인 중 오류가 발생했습니다.');
                    goHome();
                }
            });
        }
        
        function finalizeMembership(paymentKey, orderId, amount, paymentMethod, paymentStatus) {
            console.log('=== 멤버십 최종 확정 요청 ===');
            
            $.ajax({
                url: 'Payment',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    orderId: orderId,
                    paymentKey: paymentKey,
                    paymentMethod: paymentMethod,
                    paymentStatus: paymentStatus,
                    amount: amount
                }),
                success: function(response) {
                    console.log('멤버십 확정 응답:', response);
                    
                    if (response.success) {
                        displayMembershipInfo(response.data);
                    } else {
                        alert('멤버십 확정 실패: ' + response.message);
                        goHome();
                    }
                },
                error: function(xhr, status, error) {
                    console.error('멤버십 확정 오류:', error);
                    alert('멤버십 확정 중 오류가 발생했습니다.');
                    goHome();
                }
            });
        }
        
        function displayMembershipInfo(data) {
            console.log('=== 멤버십 정보 표시 ===', data);
            
            $('#membershipName').text(data.membershipName);
            $('#paymentAmount').text('₩' + data.totalAmount.toLocaleString());
            $('#validPeriod').text(data.validPeriod);
            $('#paymentNo').text(data.paymentNo);
        }
        
        function goMembershipList() {
            location.href = 'Membership?t_gubun=membershipList';
        }
        
        function goHome() {
            location.href = 'Main';
        }
    </script>
</body>
</html>
