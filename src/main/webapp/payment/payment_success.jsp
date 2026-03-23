<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제 처리 중...</title>
    <link rel="stylesheet" href="style/payment.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="container">
        <div class="loading" id="loadingSection">
            <div class="spinner"></div>
            <h1>결제를 처리하고 있습니다...</h1>
            <p>잠시만 기다려 주세요.<br>페이지를 벗어나지 마세요.</p>
        </div>
        
        <div class="success" id="successSection">
            <div class="success-icon">✅</div>
            <h1 id="successTitle">예약이 완료되었습니다!</h1>
            <div class="success-message" id="successMessage">
                결제가 성공적으로 완료되었습니다.
            </div>
            
            <div class="info-box" id="reservationInfo">
                <h3>📋 예약 정보</h3>
            </div>
            
            <div class="btn-group">
                <a href="Check" class="btn btn-secondary">영수증확인</a>
                <a href="Index" class="btn btn-primary">메인으로</a>
            </div>
        </div>
        
        <div class="error" id="errorSection">
            <div class="error-icon">❌</div>
            <h1>결제 처리 중 오류가 발생했습니다</h1>
            
            <div class="error-message" id="errorMessage">
                <h3>⚠️ 오류 상세</h3>
                <p id="errorText"></p>
            </div>
            
            <div class="btn-group">
                <a href="Index" class="btn btn-primary">메인으로</a>
                <a href="javascript:location.reload();" class="btn btn-primary">다시 시도</a>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function() {
            const urlParams = new URLSearchParams(window.location.search);
            const paymentKey = urlParams.get('paymentKey');
            const orderId = urlParams.get('orderId');
            const amount = urlParams.get('amount');
            
            // ⭐ 멤버십 결제인지 체크
            const isMembership = orderId && orderId.startsWith('MEMBERSHIP_');
            
            console.log('=================================================');
            console.log('결제 성공 페이지 진입');
            console.log('paymentKey:', paymentKey);
            console.log('orderId:', orderId);
            console.log('amount:', amount);
            console.log('isMembership:', isMembership);
            console.log('=================================================');
            
            if (!paymentKey || !orderId || !amount) {
                showError('결제 정보가 올바르지 않습니다. 다시 시도해주세요.');
                return;
            }
            
            confirmPayment(paymentKey, orderId, amount, isMembership);
        });
        
        function confirmPayment(paymentKey, orderId, amount, isMembership) {
            console.log('>>> 1단계: 결제 승인 요청 시작');
            console.log('>>> 멤버십 결제:', isMembership);
            
            $.ajax({
                url: 'Payment',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    paymentKey: paymentKey,
                    orderId: orderId,
                    amount: parseInt(amount)
                }),
                dataType: 'json',
                success: function(response) {
                    console.log('============================================');
                    console.log('결제 승인 응답:', response);
                    console.log('응답 타입:', typeof response);
                    console.log('============================================');
                    
                    if (!response) {
                        console.error('❌ 응답이 null 또는 undefined입니다');
                        showError('서버 응답이 올바르지 않습니다.');
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
                            showError('서버 응답 형식이 올바르지 않습니다.');
                            return;
                        }
                    }
                    
                    if (response.status === 'DONE' || response.approvedAt) {
                        console.log('✅ 결제 승인 완료! 2단계 진행');
                        
                        // ⭐ 분기 처리
                        if (isMembership) {
                            finalizeMembershipOrder(paymentKey, orderId, response, amount);
                        } else {
                            loadReservationInfoAndFinalize(paymentKey, orderId, response);
                        }
                    } else {
                        showError('결제 승인에 실패했습니다: ' + (response.message || '알 수 없는 오류'));
                    }
                },
                error: function(xhr, status, error) {
                    console.error('============================================');
                    console.error('❌ 결제 승인 오류');
                    console.error('Status:', status);
                    console.error('Error:', error);
                    console.error('XHR Status:', xhr.status);
                    console.error('Response Text:', xhr.responseText);
                    console.error('============================================');
                    
                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        showError(errorData.message || '결제 승인 중 오류가 발생했습니다.');
                    } catch(e) {
                        showError('결제 승인 중 네트워크 오류가 발생했습니다.');
                    }
                }
            });
        }
        
        // ⭐ 멤버십 확정
        function finalizeMembershipOrder(paymentKey, orderId, paymentData, amount) {
            console.log('>>> 2단계: 멤버십 확정 요청');
            console.log('>>> 결제 데이터:', paymentData);
            
            $.ajax({
                url: 'Payment',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    orderId: orderId,
                    paymentKey: paymentKey,
                    amount: parseInt(amount),
                    paymentMethod: paymentData.method || '간편결제',
                    paymentStatus: 'COMPLETED'
                }),
                dataType: 'json',
                success: function(response) {
                    console.log('============================================');
                    console.log('멤버십 확정 응답:', response);
                    console.log('응답 타입:', typeof response);
                    console.log('============================================');
                    
                    if (!response) {
                        console.error('❌ 응답이 null 또는 undefined입니다');
                        showError('멤버십 확정 응답이 올바르지 않습니다.');
                        return;
                    }
                    
                    if (typeof response === 'string') {
                        console.warn('⚠️ 응답이 문자열입니다. JSON 파싱 시도...');
                        try {
                            response = JSON.parse(response);
                            console.log('✅ JSON 파싱 성공:', response);
                        } catch(e) {
                            console.error('❌ JSON 파싱 실패:', e);
                            showError('서버 응답 형식이 올바르지 않습니다.');
                            return;
                        }
                    }
                    
                    if (response.success) {
                        console.log('✅ 멤버십 구독 완료!');
                        showMembershipSuccess(response.data || {}, paymentData);
                    } else {
                        showError('멤버십 확정에 실패했습니다: ' + (response.message || '알 수 없는 오류'));
                    }
                },
                error: function(xhr, status, error) {
                    console.error('============================================');
                    console.error('❌ 멤버십 확정 오류');
                    console.error('Status:', status);
                    console.error('Error:', error);
                    console.error('XHR Status:', xhr.status);
                    console.error('Response Text:', xhr.responseText);
                    console.error('============================================');
                    
                    showError('멤버십 확정 중 오류가 발생했습니다. 고객센터로 문의해주세요.');
                }
            });
        }
        
        // ⭐ 예약 정보 조회 후 확정
        function loadReservationInfoAndFinalize(paymentKey, orderId, paymentData) {
            console.log('>>> 1.5단계: 임시 예약 정보 조회 (고객 정보 가져오기)');
            
            $.ajax({
                url: 'Payment?t_gubun=getTempReservation&orderId=' + orderId,
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    console.log('============================================');
                    console.log('임시 예약 정보 응답:', response);
                    console.log('응답 타입:', typeof response);
                    console.log('============================================');
                    
                    if (!response) {
                        console.error('❌ 응답이 null 또는 undefined입니다');
                        showError('예약 정보를 불러올 수 없습니다.');
                        return;
                    }
                    
                    if (typeof response === 'string') {
                        console.warn('⚠️ 응답이 문자열입니다. JSON 파싱 시도...');
                        try {
                            response = JSON.parse(response);
                            console.log('✅ JSON 파싱 성공:', response);
                        } catch(e) {
                            console.error('❌ JSON 파싱 실패:', e);
                            showError('서버 응답 형식이 올바르지 않습니다.');
                            return;
                        }
                    }
                    
                    if (response.success && response.data) {
                        const customerName = response.data.customerName || 'Guest User';
                        const customerEmail = response.data.customerEmail || 'guest@jslhotel.com';
                        const customerMobilePhone = response.data.customerMobilePhone || '010-0000-0000';
                        
                        console.log('✅ 조회된 고객 정보:');
                        console.log('  - customerName:', customerName);
                        console.log('  - customerEmail:', customerEmail);
                        console.log('  - customerMobilePhone:', customerMobilePhone);
                        
                        finalizeReservation(paymentKey, orderId, paymentData, customerName, customerEmail, customerMobilePhone);
                    } else {
                        showError('예약 정보를 불러올 수 없습니다: ' + (response.message || '알 수 없는 오류'));
                    }
                },
                error: function(xhr, status, error) {
                    console.error('============================================');
                    console.error('❌ 예약 정보 조회 오류');
                    console.error('Status:', status);
                    console.error('Error:', error);
                    console.error('XHR Status:', xhr.status);
                    console.error('Response Text:', xhr.responseText);
                    console.error('============================================');
                    
                    showError('예약 정보 조회 중 오류가 발생했습니다.');
                }
            });
        }
        
        function finalizeReservation(paymentKey, orderId, paymentData, customerName, customerEmail, customerPhone) {
            console.log('>>> 2단계: 예약 확정 요청 시작');
            console.log('>>> 결제 데이터:', paymentData);
            console.log('>>> 고객 정보:', {customerName, customerEmail, customerPhone});
            
            $.ajax({
                url: 'Payment',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    orderId: orderId,
                    paymentKey: paymentKey,
                    amount: paymentData.totalAmount || paymentData.amount,
                    paymentMethod: paymentData.method || 'CARD',
                    paymentStatus: 'COMPLETED',
                    customerName: customerName,
                    customerEmail: customerEmail,
                    customerPhone: customerPhone
                }),
                dataType: 'json',
                success: function(response) {
                    console.log('============================================');
                    console.log('예약 확정 응답:', response);
                    console.log('응답 타입:', typeof response);
                    console.log('============================================');
                    
                    if (!response) {
                        console.error('❌ 응답이 null 또는 undefined입니다');
                        showError('예약 확정 응답이 올바르지 않습니다.');
                        return;
                    }
                    
                    if (typeof response === 'string') {
                        console.warn('⚠️ 응답이 문자열입니다. JSON 파싱 시도...');
                        try {
                            response = JSON.parse(response);
                            console.log('✅ JSON 파싱 성공:', response);
                        } catch(e) {
                            console.error('❌ JSON 파싱 실패:', e);
                            showError('서버 응답 형식이 올바르지 않습니다.');
                            return;
                        }
                    }
                    
                    if (response.success) {
                        console.log('✅ 예약 완료! 화면 표시');
                        showReservationSuccess(response.data || {}, paymentData);
                    } else {
                        showError('예약 확정에 실패했습니다: ' + (response.message || '알 수 없는 오류'));
                    }
                },
                error: function(xhr, status, error) {
                    console.error('============================================');
                    console.error('❌ 예약 확정 오류');
                    console.error('Status:', status);
                    console.error('Error:', error);
                    console.error('XHR Status:', xhr.status);
                    console.error('Response Text:', xhr.responseText);
                    console.error('============================================');
                    
                    showError('예약 확정 중 오류가 발생했습니다. 고객센터로 문의해주세요.');
                }
            });
        }
        
        // ⭐ 멤버십 성공 화면
        function showMembershipSuccess(membershipData, paymentData) {
            console.log('>>> 멤버십 성공 화면 표시');
            console.log('>>> 멤버십 데이터:', membershipData);
            console.log('>>> 결제 데이터:', paymentData);
            
            $('#loadingSection').hide();
            $('#successTitle').text('멤버십 구독이 완료되었습니다!');
            $('#successMessage').text('축하합니다! 멤버십이 정상적으로 활성화되었습니다.');
            
            let infoHtml = '<h3>🎫 멤버십 정보</h3>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">✨ 멤버십 등급</span>';
            infoHtml += '<span class="info-value highlight">' + (membershipData.membershipName || membershipData.membershipGrade || '-') + '</span>';
            infoHtml += '</div>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">🎫 멤버십 번호</span>';
            infoHtml += '<span class="info-value">' + (membershipData.membershipNo || '-') + '</span>';
            infoHtml += '</div>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">📅 유효 기간</span>';
            infoHtml += '<span class="info-value">' + (membershipData.validPeriod || '2026-01-01 ~ 2026-06-30') + '</span>';
            infoHtml += '</div>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">💳 결제 금액</span>';
            infoHtml += '<span class="info-value highlight">' + formatPrice(membershipData.totalAmount) + '원</span>';
            infoHtml += '</div>';
            
            if (paymentData && paymentData.method) {
                infoHtml += '<div class="info-row">';
                infoHtml += '<span class="info-label">💳 결제 수단</span>';
                infoHtml += '<span class="info-value">' + getPaymentMethodName(paymentData.method) + '</span>';
                infoHtml += '</div>';
            }
            
            $('#reservationInfo').html(infoHtml);
            $('#successSection').fadeIn(500);
        }
        
        // ⭐ 예약 성공 화면
        function showReservationSuccess(reservationData, paymentData) {
            console.log('>>> 예약 성공 화면 표시');
            console.log('>>> 예약 데이터:', reservationData);
            console.log('>>> 결제 데이터:', paymentData);
            
            $('#loadingSection').hide();
            $('#successTitle').text('예약이 완료되었습니다!');
            $('#successMessage').text('결제가 성공적으로 완료되었습니다.');
            
            let infoHtml = '<h3>📋 예약 정보</h3>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">🎫 예약번호</span>';
            infoHtml += '<span class="info-value highlight">' + (reservationData.reservationNo || '-') + '</span>';
            infoHtml += '</div>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">🏨 객실</span>';
            infoHtml += '<span class="info-value">' + (reservationData.roomNo || '-') + '호</span>';
            infoHtml += '</div>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">📅 체크인</span>';
            infoHtml += '<span class="info-value">' + (reservationData.checkIn || '-') + '</span>';
            infoHtml += '</div>';
            
            infoHtml += '<div class="info-row">';
            infoHtml += '<span class="info-label">📅 체크아웃</span>';
            infoHtml += '<span class="info-value">' + (reservationData.checkOut || '-') + '</span>';
            infoHtml += '</div>';
            
            if (paymentData && paymentData.totalAmount) {
                infoHtml += '<div class="info-row">';
                infoHtml += '<span class="info-label">💳 결제 금액</span>';
                infoHtml += '<span class="info-value highlight">' + formatPrice(paymentData.totalAmount) + '원</span>';
                infoHtml += '</div>';
            }
            
            if (paymentData && paymentData.method) {
                infoHtml += '<div class="info-row">';
                infoHtml += '<span class="info-label">💳 결제 수단</span>';
                infoHtml += '<span class="info-value">' + getPaymentMethodName(paymentData.method) + '</span>';
                infoHtml += '</div>';
            }
            
            $('#reservationInfo').html(infoHtml);
            $('#successSection').fadeIn(500);
        }
        
        function showError(message) {
            console.error('>>> 에러 발생:', message);
            $('#loadingSection').hide();
            $('#errorText').text(message);
            $('#errorSection').fadeIn(500);
        }
        
        function formatPrice(price) {
            if (!price) return '0';
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        }
        
        function getPaymentMethodName(method) {
            const methodMap = {
                'CARD': '신용/체크카드',
                '카드': '신용/체크카드',
                'VIRTUAL_ACCOUNT': '가상계좌',
                'TRANSFER': '계좌이체',
                '간편결제': '간편결제',
                'MOBILE_PHONE': '휴대폰',
                'CULTURE_GIFT_CERTIFICATE': '문화상품권',
                'BOOK_GIFT_CERTIFICATE': '도서문화상품권',
                'GAME_GIFT_CERTIFICATE': '게임문화상품권'
            };
            
            return methodMap[method] || method;
        }
    </script>
</body>
</html>
