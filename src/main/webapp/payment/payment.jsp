<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제하기</title>
    <link rel="icon" href="https://static.toss.im/icons/png/4x/icon-toss-logo.png" />
    <link rel="stylesheet" href="style/payment.css">
    <script src="https://js.tosspayments.com/v2/standard"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="payment-container">
        <div class="payment-header">
            <h1>🏨 결제하기</h1>
            <p>안전한 결제를 위해 토스페이먼츠를 이용합니다</p>
        </div>
        
        <div class="payment-body">
            <div class="order-info">
                <h3>📋 예약 정보</h3>
                <div class="info-row">
                    <span class="info-label">주문번호</span>
                    <span class="info-value" id="orderIdDisplay">-</span>
                </div>
                <div class="info-row">
                    <span class="info-label">객실</span>
                    <span class="info-value" id="roomInfo">-</span>
                </div>
                <div class="info-row">
                    <span class="info-label">투숙기간</span>
                    <span class="info-value" id="dateInfo">-</span>
                </div>
                <div class="info-row">
                    <span class="info-label">투숙인원</span>
                    <span class="info-value" id="guestInfo">-</span>
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
                    <button type="button" class="payment-method-btn" data-method="TRANSFER">
                        <span class="method-icon">🏦</span>
                        <span class="method-name">계좌이체</span>
                    </button>
                    <button type="button" class="payment-method-btn" data-method="VIRTUAL_ACCOUNT">
                        <span class="method-icon">🧾</span>
                        <span class="method-name">가상계좌</span>
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
        
        const clientKey = 'test_ck_XXXXXXXXXXXX';
        const customerKey = 'CUSTOMER_' + Date.now();
        
        let tossPayments;
        let payment;
        let amount = 0;
        let timeLeft = 900;
        let timerInterval;
        let reservationData = null;
        let selectedPaymentMethod = null;
        
        $(document).ready(function() {
            console.log('페이지 로드 완료, 예약 정보 조회 시작');
            loadReservationInfo();
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
        
        function loadReservationInfo() {
            console.log('예약 정보 조회 요청 - orderId:', orderId);
            
            $.ajax({
                url: 'Reservation',
                type: 'GET',
                data: { 
                    t_gubun: 'getTempReservation',
                    orderId: orderId 
                },
                dataType: 'json',
                success: function(response) {
                    console.log('============================================');
                    console.log('예약 정보 조회 응답:', response);
                    console.log('응답 타입:', typeof response);
                    console.log('============================================');
                    
                    if (!response) {
                        console.error('❌ 응답이 null 또는 undefined입니다');
                        alert('서버 응답이 올바르지 않습니다.');
                        goHome();
                        return;
                    }
                    
                    if (typeof response === 'string') {
                        console.warn('⚠️ 응답이 문자열입니다. JSON 파싱 시도...');
                        try {
                            response = JSON.parse(response);
                            console.log('✅ JSON 파싱 성공:', response);
                        } catch(e) {
                            console.error('❌ JSON 파싱 실패:', e);
                            console.error('원본 응답:', response);
                            alert('서버 응답 형식이 올바르지 않습니다.');
                            goHome();
                            return;
                        }
                    }
                    
                    if (!response.success) {
                        console.error('❌ 예약 정보 조회 실패:', response.message);
                        alert('예약 정보를 불러올 수 없습니다: ' + (response.message || '알 수 없는 오류'));
                        goHome();
                        return;
                    }
                    
                    if (!response.data) {
                        console.error('❌ 예약 데이터가 없습니다');
                        alert('예약 데이터를 불러올 수 없습니다.');
                        goHome();
                        return;
                    }
                    
                    reservationData = response.data;
                    console.log('✅ 예약 정보 설정 완료:', reservationData);
                    
                    $('#orderIdDisplay').text(orderId);
                    $('#roomInfo').text((reservationData.roomNo || '-') + '호 (' + (reservationData.roomType || '-') + ')');
                    $('#dateInfo').text((reservationData.checkIn || '-') + ' ~ ' + (reservationData.checkOut || '-'));
                    $('#guestInfo').text((reservationData.guestCount || '0') + '명');
                    $('#amountDisplay').text('₩' + (reservationData.totalAmount || 0).toLocaleString());
                    
                    amount = reservationData.totalAmount || 0;
                    
                    if (amount <= 0) {
                        console.error('❌ 결제 금액이 0원 이하입니다:', amount);
                        alert('결제 금액이 올바르지 않습니다.');
                        goHome();
                        return;
                    }
                    
                    console.log('✅ 결제 금액 설정 완료:', amount);
                    initTossPayments();
                },
                error: function(xhr, status, error) {
                    console.error('============================================');
                    console.error('❌ AJAX 오류 발생');
                    console.error('Status:', status);
                    console.error('Error:', error);
                    console.error('XHR Status:', xhr.status);
                    console.error('Response Text:', xhr.responseText);
                    console.error('============================================');
                    
                    let errorMessage = '예약 정보 조회 중 오류가 발생했습니다.';
                    
                    if (xhr.status === 404) {
                        errorMessage = '예약 정보를 찾을 수 없습니다.';
                    } else if (xhr.status === 500) {
                        errorMessage = '서버 오류가 발생했습니다.';
                    } else if (xhr.status === 0) {
                        errorMessage = '서버에 연결할 수 없습니다.';
                    }
                    
                    try {
                        const errorResponse = JSON.parse(xhr.responseText);
                        if (errorResponse && errorResponse.message) {
                            errorMessage = errorResponse.message;
                        }
                    } catch(e) {
                        console.error('응답을 JSON으로 파싱할 수 없습니다');
                    }
                    
                    alert(errorMessage);
                    goHome();
                }
            });
        }
        
        async function initTossPayments() {
            try {
                console.log('토스 결제 SDK 초기화 시작');
                tossPayments = TossPayments(clientKey);
                payment = tossPayments.payment({ customerKey: customerKey });
                console.log('✅ 토스 결제 SDK 초기화 완료');
                
                document.getElementById('payment-button').addEventListener('click', requestPayment);
                
            } catch (error) {
                console.error('❌ 토스 결제 SDK 초기화 실패:', error);
                alert('결제 시스템 초기화에 실패했습니다.');
            }
        }
        
        async function requestPayment() {
            if (!selectedPaymentMethod) {
                alert('결제 수단을 선택해주세요.');
                return;
            }
            
            if (!reservationData) {
                alert('예약 정보가 없습니다. 페이지를 새로고침해주세요.');
                return;
            }
            
            try {
                console.log('============================================');
                console.log('결제 요청 시작');
                console.log('결제 수단:', selectedPaymentMethod);
                console.log('예약 정보:', reservationData);
                console.log('============================================');
                
                const currentUrl = window.location.origin + window.location.pathname;
                const baseUrl = currentUrl.substring(0, currentUrl.lastIndexOf('/'));
                
                const paymentConfig = {
                    method: selectedPaymentMethod,
                    amount: {
                        currency: "KRW",
                        value: amount
                    },
                    orderId: orderId,
                    orderName: 'JSL 호텔 객실 예약 - ' + (reservationData.roomNo || '객실') + '호',
                    customerName: reservationData.customerName || '고객',
                    customerEmail: reservationData.customerEmail || 'customer@example.com',
                    successUrl: baseUrl + '/Payment?t_gubun=paymentSuccess',
                    failUrl: baseUrl + '/Payment?t_gubun=paymentFail'
                };
                
                if (selectedPaymentMethod === 'CARD') {
                    paymentConfig.card = {
                        useEscrow: false,
                        flowMode: "DEFAULT",
                        useCardPoint: false,
                        useAppCardOnly: false
                    };
                } else if (selectedPaymentMethod === 'TRANSFER') {
                    paymentConfig.transfer = {
                        cashReceipt: {
                            type: "소득공제"
                        },
                        useEscrow: false
                    };
                } else if (selectedPaymentMethod === 'VIRTUAL_ACCOUNT') {
                    paymentConfig.virtualAccount = {
                        cashReceipt: {
                            type: "소득공제"
                        },
                        useEscrow: false,
                        validHours: 24
                    };
                }
                
                console.log('결제 설정:', paymentConfig);
                console.log('결제 요청 전송...');
                
                await payment.requestPayment(paymentConfig);
                
                console.log('✅ 결제 요청 완료 (리다이렉트 중...)');
                
            } catch (error) {
                console.error('============================================');
                console.error('❌ 결제 오류 발생');
                console.error('Error Code:', error.code);
                console.error('Error Message:', error.message);
                console.error('Error Object:', error);
                console.error('============================================');
                
                if (error.code === 'USER_CANCEL') {
                    console.log('사용자가 결제를 취소했습니다.');
                    clearSessionAndGoHome();
                } else if (error.code === 'INVALID_CARD_COMPANY') {
                    alert('유효하지 않은 카드사입니다.');
                } else if (error.code === 'INVALID_PARAMETER') {
                    alert('결제 정보가 올바르지 않습니다: ' + error.message);
                } else {
                    alert('결제 중 오류가 발생했습니다: ' + (error.message || '알 수 없는 오류'));
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
                    alert('결제 시간이 만료되었습니다. 예약 페이지로 돌아갑니다.');
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
            console.log('🧹 세션 정리 및 예약 페이지로 이동');
            
            $.ajax({
                url: 'Reservation',
                type: 'POST',
                data: {
                    t_gubun: 'clearTempReservation',
                    orderId: orderId
                },
                success: function() {
                    console.log('✅ 세션 정리 완료');
                    goHome();
                },
                error: function() {
                    console.warn('⚠️ 세션 정리 실패, 그래도 페이지 이동');
                    goHome();
                }
            });
        }
        
        function goHome() {
            location.href = 'Reservation?t_gubun=reservation';
        }
        
        window.addEventListener('beforeunload', function() {
            clearInterval(timerInterval);
        });
    </script>
</body>
</html>
