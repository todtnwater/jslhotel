<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file = "../common_header.jsp" %>
 <script>
        //성, 이름 영문만 입력가능하게
        function nameUpper(event){
            const regExp = /[^a-zA-Z]/g;
            const ele = event.target;
            
            if(regExp.test(ele.value)){
                ele.value = ele.value.replace(regExp, '');
            } 
        }
        function mailUpper(event){
            const regExp = /[^a-zA-Z0-9.]/g;
            const ele = event.target;
            
            if(regExp.test(ele.value)){
                ele.value = ele.value.replace(regExp, '');
            } 
        }
        function phoneUpper(event){
            const regExp = /[^0-9]/g;  
            const ele = event.target;

            if(regExp.test(ele.value)){
                ele.value = ele.value.replace(regExp, ''); 
            }
        }
        function goMemberPage(gubun) {
            const form = document.reservationForm;
            form.t_gubun.value = gubun;
            form.method = "post";
            form.action = "Member";
            form.submit();
        }
    </script>
<c:if test="${empty dto}">
    <script>
        alert('객실 정보를 찾을 수 없습니다.');
        history.back();
       
    </script>
</c:if>

<c:set var="checkIn" value="${empty checkIn ? '' : checkIn}" />
<c:set var="checkOut" value="${empty checkOut ? '' : checkOut}" />
<c:set var="adults" value="${empty adults ? 1 : adults}" />
<c:set var="children" value="${empty children ? 0 : children}" />

<%-- 총 인원 계산 --%>
<c:set var="totalGuests" value="${adults + children}" />

<%-- 최대 인원 초과 체크 (보안) --%>
<c:if test="${totalGuests > dto.r_people_max}">
    <script>
        alert('❌ 이 객실의 최대 수용 인원은 ${dto.r_people_max}명입니다.\n현재 선택 인원: ${totalGuests}명\n\n다른 객실을 선택해주세요.');
        history.back();
    </script>
</c:if>

<%-- 회원 정보 가져오기 --%>
<c:set var="memberId" value="${sessionId}" />
<c:set var="memberDto" value="${memberDto}" />
<c:set var="isLoggedIn" value="${not empty sessionId}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>객실 상세 정보 - 예약하기</title>
    <link rel="stylesheet" href="style/room_view.css">
    <link rel="stylesheet" href="style/room_image.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <main class="room-detail-container">
        <h1 class="page-title">객실 상세 정보</h1>
        
        <%-- 인원 정보 경고 표시 --%>
        <c:choose>
            <c:when test="${totalGuests == dto.r_people_max}">
                <div class="capacity-warning">
                    <span class="icon">⚠️</span>
                    <div>
                        <strong>최대 인원으로 예약됩니다</strong>
                        <div style="margin-top: 5px;">이 객실의 최대 수용 인원: ${dto.r_people_max}명 / 현재 선택: ${totalGuests}명</div>
                    </div>
                </div>
            </c:when>
            <c:when test="${totalGuests > dto.r_people_standard}">
                <div class="capacity-info">
                    <strong>ℹ️ 기준 인원 초과 안내</strong>
                    <div>기준 인원: ${dto.r_people_standard}명 / 최대 인원: ${dto.r_people_max}명 / 현재 선택: ${totalGuests}명</div>
                    <div style="margin-top: 5px;">추가 인원에 대한 추가 요금이 발생할 수 있습니다.</div>
                </div>
            </c:when>
        </c:choose>
        
        <%-- 비회원일 때만 회원가입 유도 배너 표시 --%>
        <c:if test="${empty sessionName}">
            <div class="member-benefit-banner">
                <h3>회원이시면 더 많은 혜택을 받으실 수 있어요!</h3>
                <p>지금 회원가입하고 포인트 적립, 할인 혜택을 누리세요</p>
                <div class="benefits-list">
                    <span class="benefit-item">✨ 최대 20% 포인트 적립</span>
                    <span class="benefit-item">🍽️ 무료 조식 제공</span>
                    <span class="benefit-item">🎟️ 다양한 할인 쿠폰</span>
                    <span class="benefit-item">⬆️ 객실 업그레이드</span>
                </div>

            </div>
        </c:if>
        
        <%-- 회원일 때만 멤버십 정보 표시 --%>
        <c:if test="${not empty sessionName}">
            <div class="membership-info-box">
                <h3>
                    <span> ${memberDto.first_name} ${memberDto.last_name}님의 멤버십 혜택</span>
                    <span class="membership-badge">${empty memberDto.rank ? 'SILVER' : memberDto.rank}</span>
                </h3>
                <div class="benefit-grid">
                    <div class="benefit-card">
                        <div class="benefit-label">객실 포인트 적립</div>
                        <div class="benefit-value">
                            <c:choose>
                                <c:when test="${memberDto.rank eq 'PREMIUM'}">20%</c:when>
                                <c:when test="${memberDto.rank eq 'GOLD'}">15%</c:when>
                                <c:when test="${memberDto.rank eq 'SILVER'}">10%</c:when>
                                <c:otherwise>10%</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="benefit-card">
                        <div class="benefit-label">식음 포인트 적립</div>
                        <div class="benefit-value">
                            <c:choose>
                                <c:when test="${memberDto.rank eq 'PREMIUM'}">10%</c:when>
                                <c:when test="${memberDto.rank eq 'GOLD'}">5%</c:when>
                                <c:otherwise>0%</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="benefit-card">
                        <div class="benefit-label">보유 포인트</div>
                        <div class="benefit-value">
                            <fmt:formatNumber value="${empty sessionScope.totalPoints ? 0 : sessionScope.totalPoints}" pattern="#,###"/>P
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        
        <div class="room-detail-content">
            <%-- 객실 이미지 --%>
            <div class="room-image-section">
                <c:choose>
                    <c:when test="${not empty mainImagePath}">
                        <img src="${pageContext.request.contextPath}/image${mainImagePath}" alt="객실 이미지" class="room-main-image">
                    </c:when>
                    <c:otherwise>
                        <img src="images/rooms/${dto.r_type}.jpg" alt="객실 이미지" class="room-main-image">
                    </c:otherwise>
                </c:choose>
            </div>
            
            <%-- 객실 정보 --%>
            <div class="room-info-section">
                <h2>${dto.r_type} - ${dto.r_room_no}호</h2>
                <div class="room-basic-info">
                    <p><strong>위치:</strong> ${dto.r_floor}층</p>
                    <p><strong>객실 크기:</strong> ${dto.r_scale}㎡</p>
                    <p style="font-size: 18px; color: #d32f2f;">
                        <strong>수용 인원:</strong> 
                        기준 ${dto.r_people_standard}명 / 최대 ${dto.r_people_max}명
                        <span style="color: #666; font-size: 14px; margin-left: 8px;">(현재 선택: ${totalGuests}명)</span>
                    </p>
                    <p><strong>침대:</strong> 
                        <c:choose>
                            <c:when test="${dto.r_bed_type eq 'S'}">싱글</c:when>
                            <c:when test="${dto.r_bed_type eq 'D'}">더블</c:when>
                            <c:when test="${dto.r_bed_type eq 'Q'}">퀸</c:when>
                            <c:when test="${dto.r_bed_type eq 'K'}">킹</c:when>
                            <c:otherwise>${dto.r_bed_type}</c:otherwise>
                        </c:choose>
                        ${dto.r_bed}개
                    </p>
                </div>
                
                <div class="room-facilities">
                    <h3>객실 시설</h3>
                    <ul>
                        <c:if test="${dto.r_wifi eq 'Y'}">
                            <li>🌐 무료 Wi-Fi</li>
                        </c:if>
                        <c:if test="${dto.r_spa eq 'Y'}">
                            <li>🛁 스파</li>
                        </c:if>
                        <c:if test="${dto.r_balcony eq 'Y'}">
                            <li>🪴 발코니</li>
                        </c:if>
                        <c:if test="${dto.r_kitchen eq 'Y'}">
                            <li>🍳 주방</li>
                        </c:if>
                        <li>🚿 욕실 ${dto.r_bathroom}개</li>
                        <c:if test="${not empty dto.r_view}">
                            <li>🏞️ ${dto.r_view} 전망</li>
                        </c:if>
                    </ul>
                </div>
                
                <div class="room-description">
                    <h3>객실 설명</h3>
                    <p>${not empty dto.r_description ? dto.r_description : '편안한 휴식을 위한 공간입니다.'}</p>
                </div>
            </div>
        </div>
        
        <%-- 객실 이미지 슬라이더 섹션 --%>
        <c:if test="${not empty roomImages && roomImages.size() > 0}">
            <div class="room-image-slider-section">
                <h4>객실 사진 갤러리 (${roomImages.size()})</h4>
                <div class="image-cards-wrapper">
                    <div class="image-cards-track">
                        <c:forEach var="image" items="${roomImages}" varStatus="status">
                            <div class="image-card" onclick="openImageModal(${status.index})">
                                <img src="${pageContext.request.contextPath}/image${image.ri_img_path}" alt="객실 이미지 ${status.index + 1}">
                                <c:if test="${image.ri_is_main eq 'Y'}">
                                    <div class="main-badge">메인</div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            
            <%-- 이미지 확대 모달 --%>
            <div class="image-modal" id="imageModal">
                <div class="modal-content">
                    <button class="modal-close" onclick="closeImageModal()">&times;</button>
                    <button class="modal-prev" onclick="changeModalImage(-1)">&#10094;</button>
                    <img id="modalImage" src="" alt="확대 이미지">
                    <button class="modal-next" onclick="changeModalImage(1)">&#10095;</button>
                    <div class="modal-info">
                        <span id="modalImageIndex"></span>
                    </div>
                </div>
            </div>
        </c:if>
        
        <%-- 예약 정보 입력 --%>
        <div class="reservation-form-section">
            <h2>예약 정보</h2>

            <form id="reservationForm" name="reservationForm">
				<input type="hidden" name="t_gubun">
            	<input type="hidden" name= "RV_MEMBER_ID" value = "${sessionId}">
            	<input type="hidden" name= "RV_MEMBERSHIP_LEVEL" value = "${sessionMembership}">
                <%-- 예약 기본 정보 --%>
                <div class="reservation-summary">
                    <div class="summary-item">
                        <label>체크인:</label>
                        <span id="displayCheckIn">${checkIn}</span>
                    </div>
                    <div class="summary-item">
                        <label>체크아웃:</label>
                        <span id="displayCheckOut">${checkOut}</span>
                    </div>
                    <div class="summary-item">
                        <label>투숙 기간:</label>
                        <span id="displayNights"></span>
                    </div>
                    <div class="summary-item">
                        <label>투숙 인원:</label>
                        <span id="displayGuests">성인 ${adults}명 / 어린이 ${children}명</span>
                    </div>
                </div>
                
                <%-- 조식 옵션 --%>
                <div class="breakfast-option-section">
                    <h3>조식 옵션 선택</h3>
                    <div class="breakfast-options">
                        <label class="breakfast-option">
                            <input type="radio" name="breakfast" value="none" checked>
                            <div class="option-content">
                                <div class="option-title">조식 없음</div>
                                <div class="option-desc">조식을 선택하지 않습니다</div>
                                <div class="option-price">₩0</div>
                            </div>
                        </label>
                        
                        <label class="breakfast-option">
                            <input type="radio" name="breakfast" value="korean">
                            <div class="option-content">
                                <div class="option-title">한식 조식</div>
                                <div class="option-desc">전통 한식 조식 뷔페</div>
                                <div class="option-price">₩200</div>
                            </div>
                        </label>
                        
                        <label class="breakfast-option">
                            <input type="radio" name="breakfast" value="western">
                            <div class="option-content">
                                <div class="option-title">양식 조식</div>
                                <div class="option-desc">양식 조식 뷔페</div>
                                <div class="option-price">₩300</div>
                            </div>
                        </label>
                    </div>
                </div>
                
                <%-- 예약자 정보 --%>
                <div class="customer-info-section">
                    <h3>예약자 정보</h3>
                    
                    <div class="form-group">
                        <label for="firstName">First Name (이름) *Only English</label>
                        <input type="text" id="firstName" name="firstName" 
                               value="${isLoggedIn ? memberDto.first_name : ''}"  onkeyup="nameUpper(event)" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="lastName">Last Name (성) *Only English</label>
                        <input type="text" id="lastName" name="lastName" 
                               value="${isLoggedIn ? memberDto.last_name : ''}"  onkeyup="nameUpper(event)" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">이메일 *Only English or Number</label>
                        <div style="display: flex; gap: 10px; align-items: center;">
                            <input type="text" id="email1" name="email1" 
                                   value="${isLoggedIn ? memberDto.email_1 : ''}"
                                   style="flex: 1;"  onkeyup="mailUpper(event)" required>
                            <span>@</span>
                            <input type="text" id="email2" name="email2" 
                                   value="${isLoggedIn ? memberDto.email_2 : ''}"
                                   style="flex: 1;"  onkeyup="mailUpper(event)" required>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="mobile">휴대전화 *Only Number</label>
                        <div style="display: flex; gap: 10px; align-items: center;">
                            <input type="text" id="mobile1" name="mobile1" maxlength="3" 
                                   value="${isLoggedIn ? memberDto.mobile_1 : ''}"
                                   style="flex: 1;" onkeyup="phoneUpper(event)" required>
                            <span>-</span>
                            <input type="text" id="mobile2" name="mobile2" maxlength="4" 
                                   value="${isLoggedIn ? memberDto.mobile_2 : ''}"
                                   style="flex: 1;" onkeyup="phoneUpper(event)" required>
                            <span>-</span>
                            <input type="text" id="mobile3" name="mobile3" maxlength="4" 
                                   value="${isLoggedIn ? memberDto.mobile_3 : ''}"
                                   style="flex: 1;" onkeyup="phoneUpper(event)" required>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="specialRequest">특별 요청사항</label>
                        <textarea id="specialRequest" name="specialRequest" 
                                  placeholder="예: 높은 층 선호, 금연실 희망 등"></textarea>
                    </div>
                </div>
                
                <%-- 포인트 사용 (회원만) --%>
                <c:if test="${isLoggedIn}">
                    <div class="point-usage-section">
                        <h3>포인트 사용</h3>
                        <div class="form-group">
                            <label for="usePoints">사용할 포인트 (보유: <fmt:formatNumber value="${empty sessionScope.totalPoints ? 0 : sessionScope.totalPoints}" pattern="#,###"/>P)</label>
                            <div style="display: flex; gap: 10px;">
                                <input class="area" type="number" id="usePoints" name="usePoints"
                                       min="0" max="${sessionScope.totalPoints}" value="0" step="10"
                                       style="flex: 1;"  onkeyup="phoneUpper(event)" >
                                <button class="m-search-area" type="button" class="btn-use-all" onclick="useAllPoints()">전액 사용</button>
                            </div>
                            <p style="margin-top: 0.5rem; font-size: 0.9rem; color: #666;">
                                ※ 10원 단위로 사용 가능하며, 최소 100원은 결제해야 합니다.
                            </p>
                            <%-- 🔧 테스트 단계: 1000P 제한 안내 주석처리 (운영 시 활성화) --%>
                            <%-- <p style="margin-top: 0.5rem; font-size: 0.9rem; color: #666;">
                                ※ 1,000P 이상부터 사용 가능합니다.
                            </p> --%>
                        </div>
                    </div>
                </c:if>
                
                <%-- 가격 요약 --%>
                <div class="price-summary-section">
                    <h3>최종 결제 금액</h3>
                    <table class="price-table">
                        <tr>
                            <td>객실 요금</td>
                            <td class="text-right" id="roomPrice">₩0</td>
                        </tr>
                        <tr>
                            <td>조식 요금</td>
                            <td class="text-right" id="breakfastPrice">₩0</td>
                        </tr>
                        <c:if test="${isLoggedIn}">
                            <tr>
                                <td>포인트 할인</td>
                                <td class="text-right" id="pointDiscount">-₩0</td>
                            </tr>
                            <tr>
                                <td>적립 예정 포인트</td>
                                <td class="text-right" id="earnPoints">0P</td>
                            </tr>
                        </c:if>
                        <tr class="total-row">
                            <td>총 결제 금액</td>
                            <td class="text-right" id="totalPrice">₩0</td>
                        </tr>
                    </table>
                </div>
                주문 내용을 확인 하였으며, 본인은 개인정보 이용 및 제공(해외직구의 경우 국외제공) 및 결제에 동의합니다.<br><br>
                현재 토스 API를 이용중이며, 실제로는 돈을 지불하지 않습니다. <br>
                다만 카카오페이를 이용할시, 본인이 결제 진행한 카카오페이의 계좌로 돈이 입금이 되니 주의하세요.
                <%-- 액션 버튼 --%>
                <div class="action-buttons">
                    <button type="button" class="btn-back" onclick="history.back()">이전으로</button>
                    <button type="submit" class="btn-reserve">예약하기</button>
                </div>
            </form>
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>

    <script>
        const roomNo = '${dto.r_room_no}';
        const checkIn = '${checkIn}';
        const checkOut = '${checkOut}';
        const adults = ${adults};
        const children = ${children};
        const totalGuests = ${totalGuests};
        const roomPrice = ${dto.r_price};
        const maxCapacity = ${dto.r_people_max};
        const isLoggedIn = ${isLoggedIn};
        const totalPoints = ${empty sessionScope.totalPoints ? 0 : sessionScope.totalPoints};
        const memberRank = '${empty memberDto.rank ? "SILVER" : memberDto.rank}';
        
        const breakfastPrices = {
            'none': 0,
            'korean': 200,
            'western': 300
        };
        
        const pointRates = {
            'PREMIUM': 0.20,
            'GOLD': 0.15,
            'SILVER': 0.10,
            'BRONZE': 0.10
        };
        
        // 이미지 모달 관련 변수
        let currentImageIndex = 0;
        const images = [];
        
        <c:if test="${not empty roomImages}">
            <c:forEach var="image" items="${roomImages}">
                images.push({
                    path: '${pageContext.request.contextPath}/image${image.ri_img_path}',
                    isMain: ${image.ri_is_main eq 'Y'}
                });
            </c:forEach>
        </c:if>
        
        function openImageModal(index) {
            currentImageIndex = index;
            updateModalImage();
            document.getElementById('imageModal').classList.add('active');
        }
        
        function closeImageModal() {
            document.getElementById('imageModal').classList.remove('active');
        }
        
        function changeModalImage(direction) {
            currentImageIndex += direction;
            if (currentImageIndex < 0) {
                currentImageIndex = images.length - 1;
            } else if (currentImageIndex >= images.length) {
                currentImageIndex = 0;
            }
            updateModalImage();
        }
        
        function updateModalImage() {
            const modalImage = document.getElementById('modalImage');
            const modalImageIndex = document.getElementById('modalImageIndex');
            
            modalImage.src = images[currentImageIndex].path;
            modalImageIndex.textContent = (currentImageIndex + 1) + ' / ' + images.length;
        }
        
        // ESC 키로 모달 닫기
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeImageModal();
            } else if (e.key === 'ArrowLeft') {
                changeModalImage(-1);
            } else if (e.key === 'ArrowRight') {
                changeModalImage(1);
            }
        });
        
        $(document).ready(function() {
            const nights = calculateNights(checkIn, checkOut);
            $('#displayNights').text(nights + '박');
            
            calculatePrice();
            
            $('input[name="breakfast"]').on('change', calculatePrice);
            
            if (isLoggedIn) {
            	$('#usePoints').on('change', function() {
                    let value = parseInt($(this).val()) || 0;

                    // 음수 방지
                    if (value < 0) value = 0;

                    // 10원 단위로 내림
                    value = Math.floor(value / 10) * 10;

                    // 최대 사용 가능 금액 계산
                    const nights = calculateNights(checkIn, checkOut);
                    const totalRoomPrice = roomPrice * nights;

                    const selectedBreakfast = $('input[name="breakfast"]:checked').val();
                    const breakfastPricePerPerson = breakfastPrices[selectedBreakfast];

                    let totalBreakfastPrice = 0;
                    if (selectedBreakfast !== 'none') {
                        totalBreakfastPrice = (breakfastPricePerPerson * adults * nights) +
                                             (breakfastPricePerPerson * 0.5 * children * nights);
                    }

                    const beforeDiscountTotal = totalRoomPrice + totalBreakfastPrice;
                    const maxDiscountAmount = beforeDiscountTotal - 100; // 최소 100원은 결제해야 함

                    // 최대 사용 가능 금액 초과 방지
                    if (value > maxDiscountAmount) value = Math.floor(maxDiscountAmount / 10) * 10;

                    // 보유 포인트 초과 방지
                    if (value > totalPoints) value = Math.floor(totalPoints / 10) * 10;

                    // 🔧 테스트 단계: 1000P 미만 제한 해제 (운영 시 활성화)
                    // if (value > 0 && value < 1000) value = 0;

                    $(this).val(value);
                    calculatePrice();
                });
            }
            
            $('#reservationForm').on('submit', function(e) {
                e.preventDefault();
                if (!validateForm()) return;
                createTempReservation();
            });
        });
        
        function calculateNights(checkIn, checkOut) {
            const date1 = new Date(checkIn);
            const date2 = new Date(checkOut);
            const diffTime = Math.abs(date2 - date1);
            const nights = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            return nights > 0 ? nights : 1;
        }
        
        function calculatePrice() {
            const nights = calculateNights(checkIn, checkOut);
            const totalRoomPrice = roomPrice * nights;
            
            const selectedBreakfast = $('input[name="breakfast"]:checked').val();
            const breakfastPricePerPerson = breakfastPrices[selectedBreakfast];
            
            let totalBreakfastPrice = 0;
            if (selectedBreakfast !== 'none') {
                totalBreakfastPrice = (breakfastPricePerPerson * adults * nights) + 
                                     (breakfastPricePerPerson * 0.5 * children * nights);
            }
            
            let pointDiscount = 0;
            if (isLoggedIn) {
                const usePointsValue = parseInt($('#usePoints').val()) || 0;
                const beforeDiscountTotal = totalRoomPrice + totalBreakfastPrice;
                const maxDiscountAmount = beforeDiscountTotal - 100; // 최소 100원은 결제해야 함

                // 🔧 테스트 단계: 1P부터 사용 가능 (운영 시 1000P 이상으로 변경)
                if (usePointsValue > 0 && usePointsValue <= totalPoints) {
                    // 최대 할인 가능 금액을 초과하지 않도록 제한
                    pointDiscount = Math.min(usePointsValue, maxDiscountAmount);
                }
                // 운영 시 활성화: if (usePointsValue >= 1000 && usePointsValue <= totalPoints) {
            }
            
            const totalPrice = totalRoomPrice + totalBreakfastPrice - pointDiscount;
            
            $('#roomPrice').text('₩' + totalRoomPrice.toLocaleString());
            $('#breakfastPrice').text('₩' + Math.round(totalBreakfastPrice).toLocaleString());
            
            if (isLoggedIn) {
                $('#pointDiscount').text('-₩' + pointDiscount.toLocaleString());
                
                const rate = pointRates[memberRank] || pointRates['SILVER'];
                const earnPoints = Math.floor(totalRoomPrice * rate);
                $('#earnPoints').text(earnPoints.toLocaleString() + 'P');
            }
            
            $('#totalPrice').text('₩' + Math.round(totalPrice).toLocaleString());
        }
        
        function useAllPoints() {
            // 총 금액 계산
            const nights = calculateNights(checkIn, checkOut);
            const totalRoomPrice = roomPrice * nights;

            const selectedBreakfast = $('input[name="breakfast"]:checked').val();
            const breakfastPricePerPerson = breakfastPrices[selectedBreakfast];

            let totalBreakfastPrice = 0;
            if (selectedBreakfast !== 'none') {
                totalBreakfastPrice = (breakfastPricePerPerson * adults * nights) +
                                     (breakfastPricePerPerson * 0.5 * children * nights);
            }

            const beforeDiscountTotal = totalRoomPrice + totalBreakfastPrice;
            const maxDiscountAmount = beforeDiscountTotal - 100; // 최소 100원은 결제해야 함

            // 🔧 테스트 단계: 1P부터 전액 사용 가능 (운영 시 1000P 이상으로 제한)
            if (totalPoints > 0) {
                // 보유 포인트와 최대 할인 가능 금액 중 작은 값 사용 (10원 단위로 내림)
                let useAmount = Math.min(totalPoints, maxDiscountAmount);
                useAmount = Math.floor(useAmount / 10) * 10; // 10원 단위로 내림

                if (useAmount <= 0) {
                    alert('사용 가능한 포인트가 없습니다.\n(최소 100원은 결제해야 합니다)');
                    return;
                }

                $('#usePoints').val(useAmount);
                calculatePrice();
            } else {
                alert('사용 가능한 포인트가 없습니다.');
            }
            // 운영 시 활성화:
            // if (totalPoints >= 1000) {
            //     let useAmount = Math.min(totalPoints, maxDiscountAmount);
            //     useAmount = Math.floor(useAmount / 10) * 10; // 10원 단위로 내림
            //     $('#usePoints').val(useAmount);
            //     calculatePrice();
            // } else {
            //     alert('보유 포인트가 1,000P 미만입니다.');
            // }
        }
        
        function validateForm() {
            const firstName = $('#firstName').val().trim();
            const lastName = $('#lastName').val().trim();
            const email1 = $('#email1').val().trim();
            const email2 = $('#email2').val().trim();
            const mobile1 = $('#mobile1').val().trim();
            const mobile2 = $('#mobile2').val().trim();
            const mobile3 = $('#mobile3').val().trim();
            
            // 이름 검증
            if (!firstName || !lastName) {
                alert('성함을 모두 입력해주세요.');
                if (!firstName) $('#firstName').focus();
                else $('#lastName').focus();
                return false;
            }
            
            // 영문만 입력 확인
            const namePattern = /^[A-Za-z]+$/;
            if (!namePattern.test(firstName) || !namePattern.test(lastName)) {
                alert('성함은 영문으로만 입력해주세요.');
                return false;
            }
            
            // 이메일 검증
            if (!email1 || !email2) {
                alert('이메일을 모두 입력해주세요.');
                if (!email1) $('#email1').focus();
                else $('#email2').focus();
                return false;
            }
            
			 // 휴대전화 검증
            
            if (mobile1.length < 3 || mobile2.length < 3 || mobile3.length < 4) {
                alert('올바른 휴대전화 번호 형식이 아닙니다.');
                return false;
            }
           
            return true;
        }
        
        function createTempReservation() {
            // 최대 인원 최종 체크 (보안)
            if (totalGuests > maxCapacity) {
                alert('❌ 이 객실의 최대 수용 인원은 ' + maxCapacity + '명입니다.\n현재 선택 인원: ' + totalGuests + '명');
                return;
            }
            
            const nights = calculateNights(checkIn, checkOut);
            const totalRoomPrice = roomPrice * nights;
            
            const selectedBreakfast = $('input[name="breakfast"]:checked').val();
            const breakfastPricePerPerson = breakfastPrices[selectedBreakfast];
            
            let totalBreakfastPrice = 0;
            if (selectedBreakfast !== 'none') {
                totalBreakfastPrice = (breakfastPricePerPerson * adults * nights) + 
                                     (breakfastPricePerPerson * 0.5 * children * nights);
            }
            
            let pointDiscount = 0;
            if (isLoggedIn) {
                pointDiscount = parseInt($('#usePoints').val()) || 0;
            }
            
            const totalAmount = Math.round(totalRoomPrice + totalBreakfastPrice - pointDiscount);
            
            // 이름 조합
            const customerName = $('#firstName').val().trim() + ' ' + $('#lastName').val().trim();
            
            // 이메일 조합
            const customerEmail = $('#email1').val().trim() + '@' + $('#email2').val().trim();
            
            // 휴대전화 조합
            const customerMobilePhone = $('#mobile1').val().trim() + '-' + 
                                        $('#mobile2').val().trim() + '-' + 
                                        $('#mobile3').val().trim();
            
            const reservationData = {
                roomNo: roomNo,
                checkIn: checkIn,
                checkOut: checkOut,
                guestCount: totalGuests,
                customerName: customerName,
                customerEmail: customerEmail,
                customerMobilePhone: customerMobilePhone,
                specialRequest: $('#specialRequest').val().trim(),
                breakfastOption: selectedBreakfast,
                totalAmount: totalAmount,
                usePoints: pointDiscount,
                isLoggedIn: isLoggedIn,
                memberRank: isLoggedIn ? memberRank : 'BRONZE'
            };
            
            console.log('임시 예약 생성:', reservationData);
            
            $.ajax({
                url: 'Reservation',
                type: 'POST',
                data: JSON.stringify(reservationData),
                contentType: 'application/json',
                dataType: 'json',
                success: function(response) {
                    console.log('성공:', response);
                    if (response.success) {
                        location.href = 'Reservation?t_gubun=payment&orderId=' + response.data.orderId;
                    } else {
                        alert('예약 생성 실패: ' + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('에러:', error);
                    alert('예약 생성 중 오류가 발생했습니다.');
                }
            });
        }
    </script>
</body>
</html>
