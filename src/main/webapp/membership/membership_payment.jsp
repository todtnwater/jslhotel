<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>멤버십 결제하기</title>
    <link rel="icon" href="https://static.toss.im/icons/png/4x/icon-toss-logo.png" />
    <link rel="stylesheet" href="style/payment.css">
    <script src="https://js.tosspayments.com/v2/standard"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="payment-container">
        <div class="payment-header">
            <h1>🎖️ 멤버십 결제하기</h1>
            <p>안전한 결제를 위해 토스페이먼츠를 이용합니다</p>
        </div>
        
        <div class="payment-body">
            <div class="order-info">
                <h3>📋 멤버십 정보</h3>
                <div class="info-row">
                    <span class="info-label">주문번호</span>
                    <span class="info-value" id="orderIdDisplay">-</span>
                </div>
                <div class="info-row">
                    <span class="info-label">멤버십</span>
                    <span class="info-value" id="membershipInfo">-</span>
                </div>
                <div class="info-row">
                    <span class="info-label">유효기간</span>
                    <span class="info-value" id="periodInfo">-</span>
                </div>
                <div class="info-row">
                    <span class="info-label">결제금액</span>
                    <span class="info-value" id="amountDisplay">₩0</span>
                </div>
            </div>
            
            <div class="timer">
                ⏰ 결제 제한 시간: <span class="timer-value" id="timer">15:00</span>
            </div>
            
            <div id="payment-method-container">
                <h3 style="margin-bottom: 20px; color: #333;">💳 결제 수단 선택</h3>
                <div id="payment-method-buttons">
                    <button type="button" class="payment-method-btn" data-method="CARD">
                        <span class="method-icon">💳</span>
                        <span class="method-name">카드</span>
                    </button>
                </div>
            </div>
            
            <div class="button-group">
                <button type="button" class="btn btn-cancel" onclick="cancelPayment()">취소</button>
                <button type="button" class="btn btn-pay" id="payment-button" disabled>결제 수단을 선택하세요</button>
            </div>
        </div>
    </div>

    <script>
        const urlParams = new URLSearchParams(window.location.search);
        const orderId = urlParams.get('orderId');
        
        console.log('orderId from URL:', orderId);
        
        if (!orderId) {
            alert('잘못된 접근입니다. 주문번호가 없습니다.');
            goHome();
        }
        
        const clientKey = 'test_ck_XXXXXXXXXXXXXXXXXX';
        const customerKey = 'CUSTOMER_' + Date.now();
        
        let tossPayments;
        let payment;
        let amount = 0;
        let timeLeft = 900;
        let timerInterval;
        let membershipData = null;
        let selectedPaymentMethod = null;
        
        $(document).ready(function() {
            console.log('페이지 로드 완료, 멤버십 정보 조회 시작');
            loadMembershipInfo();
            startTimer();
            initPaymentMethodButtons();
        });
        
        function initPaymentMethodButtons() {
            const buttons = document.querySelectorAll('.payment-method-btn');
            buttons.forEach(button => {
                button.addEventListener('click', function() {
                    buttons.forEach(btn => btn.classList.remove('selected'));
                    this.classList.add('selected');
                    selectedPaymentMethod = this.getAttribute('data-method');
                    
                    const payBtn = document.getElementById('payment-button');
                    payBtn.disabled = false;
                    payBtn.textContent = '결제하기';
                    
                    console.log('선택된 결제 수단:', selectedPaymentMethod);
                });
            });
        }
        
        function loadMembershipInfo() {
            console.log('멤버십 정보 조회 요청 - orderId:', orderId);
            
            $.ajax({
                url: 'Payment',
                type: 'GET',
                data: { 
                    t_gubun: 'getTempMembership',
                    orderId: orderId 
                },
                dataType: 'json',
                success: function(response) {
                    console.log('멤버십 정보 조회 성공:', response);
                    
                    if (response.success) {
                        membershipData = response.data;
                        
                        $('#orderIdDisplay').text(orderId);
                        $('#membershipInfo').text(membershipData.membershipName);
                        $('#periodInfo').text(membershipData.validPeriod);
                        $('#amountDisplay').text('₩' + membershipData.totalAmount.toLocaleString());
                        
                        amount = membershipData.totalAmount;
                        
                        console.log('결제 금액:', amount);
                        initTossPayments();
                    } else {
                        console.error('멤버십 정보 조회 실패:', response.message);
                        alert('멤버십 정보를 불러올 수 없습니다: ' + response.message);
                        goHome();
                    }
                },
                error: function(xhr, status, error) {
                    console.error('AJAX Error:', status, error);
                    console.error('Response:', xhr.responseText);
                    alert('멤버십 정보 조회 중 오류가 발생했습니다.');
                    goHome();
                }
            });
        }
        
        async function initTossPayments() {
            try {
                console.log('토스 결제 SDK 초기화 시작');
                tossPayments = TossPayments(clientKey);
                payment = tossPayments.payment({ customerKey: customerKey });
                console.log('토스 결제 SDK 초기화 완료');
                
                document.getElementById('payment-button').addEventListener('click', requestPayment);
                
            } catch (error) {
                console.error('토스 결제 SDK 초기화 실패:', error);
                alert('결제 시스템 초기화에 실패했습니다.');
            }
        }
        
        async function requestPayment() {
            if (!selectedPaymentMethod) {
                alert('결제 수단을 선택해주세요.');
                return;
            }
            
            try {
                console.log('결제 요청 시작 - 결제 수단:', selectedPaymentMethod);
                
                const currentUrl = window.location.origin + window.location.pathname;
                const baseUrl = currentUrl.substring(0, currentUrl.lastIndexOf('/'));
                
                const paymentConfig = {
                    method: selectedPaymentMethod,
                    amount: {
                        currency: "KRW",
                        value: amount
                    },
                    orderId: orderId,
                    orderName: 'JSL 호텔 멤버십 - ' + membershipData.membershipName,
                    customerName: membershipData.memberId || '고객',
                    customerEmail: membershipData.memberId + '@jslhotel.com',
                    successUrl: baseUrl + '/Payment?t_gubun=membershipPaymentSuccess',
                    failUrl: baseUrl + '/Payment?t_gubun=membershipPaymentFail'
                };
                
                if (selectedPaymentMethod === 'CARD') {
                    paymentConfig.card = {
                        useEscrow: false,
                        flowMode: "DEFAULT",
                        useCardPoint: false,
                        useAppCardOnly: false
                    };
                }
                
                console.log('결제 설정:', paymentConfig);
                
                await payment.requestPayment(paymentConfig);
                
            } catch (error) {
                console.error('Payment Error:', error);
                if (error.code === 'USER_CANCEL') {
                    console.log('사용자가 결제를 취소했습니다.');
                    clearSessionAndGoHome();
                } else if (error.code === 'INVALID_CARD_COMPANY') {
                    alert('유효하지 않은 카드사입니다.');
                } else {
                    alert('결제 중 오류가 발생했습니다: ' + error.message);
                }
            }
        }
        
        function startTimer() {
            timerInterval = setInterval(function() {
                timeLeft--;
                
                const minutes = Math.floor(timeLeft / 60);
                const seconds = timeLeft % 60;
                
                $('#timer').text(
                    String(minutes).padStart(2, '0') + ':' + 
                    String(seconds).padStart(2, '0')
                );
                
                if (timeLeft <= 0) {
                    clearInterval(timerInterval);
                    alert('결제 시간이 만료되었습니다. 멤버십 페이지로 돌아갑니다.');
                    clearSessionAndGoHome();
                }
            }, 1000);
        }
        
        function cancelPayment() {
            if (confirm('결제를 취소하시겠습니까?')) {
                clearInterval(timerInterval);
                clearSessionAndGoHome();
            }
        }
        
        function clearSessionAndGoHome() {
            console.log('🧹 세션 정리 및 멤버십 페이지로 이동');
            goHome();
        }
        
        function goHome() {
            location.href = 'Membership?t_gubun=membershipList';
        }
        
        window.addEventListener('beforeunload', function() {
            clearInterval(timerInterval);
        });
    </script>
</body>
</html>
