<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>

<aside>
    <%@ include file = "../common/menu_membership.jsp" %>
</aside>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>

function goSubscribe(grade){
	const currentMembership = "${sessionMembership}";
	console.log("현재 멤버십:", currentMembership);

	if (currentMembership && currentMembership !== "" && 
	    currentMembership !== "silver" && currentMembership !== "bronze") {
	    alert("이미 활성화된 멤버십이 있어 구매할 수 없습니다.\n\n현재 멤버십: " + currentMembership.toUpperCase() + "\n\n멤버십 변경은 관리자에게 문의해주세요.");
	    return;
	}
    if("${t_id}" === "" || "${t_id}" === null){
        alert("로그인이 필요한 서비스입니다.");
        location.href = "Member";
        return;
    }
    
    if(!confirm(grade === 'gold' ? '골드 멤버십을 구독하시겠습니까?' : '프리미엄 멤버십을 구독하시겠습니까?')) {
        return;
    }
    
    console.log("=== 멤버십 구독 시작 ===");
    console.log("grade:", grade);
    console.log("memberId:", "${t_id}");
    
    $.ajax({
        url: 'Membership',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            membershipGrade: grade
        }),
        success: function(response) {
            console.log("임시 멤버십 생성 성공:", response);
            
            if(response.success) {
                const orderId = response.orderId;
                console.log("orderId:", orderId);
                console.log("Payment로 리다이렉트 시작");
                
                location.href = 'Membership?t_gubun=payment&orderId=' + orderId;
            } else {
                alert(response.message);
            }
        },
        error: function(xhr, status, error) {
            console.error("AJAX Error:", status, error);
            console.error("Response:", xhr.responseText);
            alert("멤버십 생성 중 오류가 발생했습니다.");
        }
    });
}

</script>

<main>
    <div class="membership-detail-container">
        <div class="board-header">
            <h1>프리미엄 멤버십</h1>
            <p>프리미엄 회원만의 특별한 혜택을 누려보세요.</p>
        </div>

        <div class="detail-content-wrapper premium-theme">
            <!-- 상단: 카드 + 정보 -->
            <section class="detail-top-section">
                <!-- 카드 영역 -->
                <div class="card-display-area">
                    <div class="detail-fixed-card hover-effect">
                        <div class="card-content">
                            <div class="card-title">프리미엄</div>
                            <div class="card-subtitle">LUXURY</div>
                        </div>
                    </div>
                </div>

                <!-- 카드 정보 영역 -->
                <div class="card-info-area">
	                <div class="info-block">
	                    <h2 class="grade-name">Premium 회원권</h2>
	                </div>
	                <div class="info-block">
	                	<strong>가격</strong>
	                    <p>₩1,200,000</p>
	                </div>
	                <div class="info-block">
	                	<strong>사용 기간</strong>
	                    <p>2026-01-01 ~ 2026-06-30</p>
	                </div>
	            </div>
                
            </section>

            <!-- 중단: 혜택 -->
            <section class="detail-middle-section">
                <h3>프리미엄 멤버십 이용권</h3>
                <ul class="benefit-list-detail">
                    <li>객실 예약 시 20% 적립</li>
                    <li>식음 10% 적립</li>
                    <li>식사 무료 제공 [조식, 중식 택1]</li>
                    <li>무료 발렛파킹</li>
                    <li>무료 셔틀버스</li>
                    <li>피트니스 이용권</li>
                    <li>사우나 이용권</li>
                    <li>풀 이용권</li>
                    <li>기타 어뮤니티 이용권</li>
                </ul>

                <h3>프리미엄 멤버십 쿠폰</h3>
                <ul class="benefit-list-detail">
                    <li>석식 뷔페 40% 할인 쿠폰</li>
                    <li>바 30% 할인 쿠폰</li>
                    <li>북카페 2만 원 할인 쿠폰</li>
                    <li>호텔 내 올리브O 상품권 5만 원</li>
                    <li>객실 등급 업그레이드 (기간 내 1회)</li>
                    <li>관광지 쿠폰북</li>
                </ul>
            </section>

            <!-- 하단: 유의사항 -->
            <section class="detail-bottom-section">
                <h3>멤버십 유의사항</h3>
                <ul class="notes-list">
	                <li>멤버십 혜택은 카드 가입일(또는 결제일) 기준으로 적용됩니다.</li>
	                <li>멤버십 등급별 혜택은 사전 고지 후 변경될 수 있습니다.</li>
	                <li>객실 할인 혜택은 공식 홈페이지 예약 시에만 적용됩니다.</li>
	                <li>혜택은 본인만 사용할 수 있으며 타인에게 양도할 수 없습니다.</li>
	                <li>결제 오류 또는 유효기간 만료 등으로 결제가 실패하면 혜택이 중단될 수 있습니다.</li>
	                <li>회원 정보(이름, 연락처)가 잘못 입력된 경우 혜택 적용이 제한될 수 있습니다.</li>
	                <li>프로그램은 운영 정책에 따라 변경되거나 종료될 수 있습니다.</li>
	                <li>멤버십과 관련된 문의는 고객센터 또는 공식 이메일로 가능합니다.</li>
	            </ul>
            </section>

            <!-- 버튼 영역 -->
            <div class="final-button-group">
                <a href="javascript:goSubscribe('premium')" class="subscribe-button">멤버십 구독하기</a>
                <a href="javascript:goMembershipPage('membershipList')" class="list-button">멤버십 목록으로</a>
            </div>
        </div>
    </div>
</main>

<footer>
    <%@ include file = "../common_footer.jsp" %>
</footer>
</body>
</html>
