<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>

	<aside>
        <%@ include file = "../common/menu_membership.jsp" %>
    </aside>

    <main>
        <!-- 멤버십 설명 -->
        <div class="membership-container">
            <div class="board-header">
                <h1>멤버십 등급별 혜택 및 특전</h1>
                <p>회원가입부터 최고 등급 구매까지, 특별한 가치를 누리세요.</p>
            </div>
			
			<div class="membership-name-area">
				<ul>
					<li class = "membership-name bronze"><h2>BRONZE</h2></li>
					<li class = "membership-name silver"><h2>SILVER</h2></li>
					<li class = "membership-name gold"><h2>GOLD</h2></li>
					<li class = "membership-name premium"><h2>PREMIUM</h2></li>
				</ul>
			</div>
            <div class="membership-area">
            	<div class="membership bronze">
            		<div class = "condition">
            			<span>비회원</span>
            		</div>
            		<div class = "gubun"></div>
            		<div class = "benefit">
            			<div class = "benefit-main">
            				<p>예약 패키지</p>
            			</div>
            		</div>
            	</div>
            	<div class="membership silver">
            		<div class = "condition">
            			<span>회원가입 시</span>
            		</div>
            		<div class = "gubun"></div>
            		<div class = "benefit">
            			<div class = "benefit-main">
            				<p>회원 예약 패키지</p>
            			</div>
            			<div class = "benefit-main">
            				<p>포인트 적립</p>
            				<div class = "benefit-sub">
            					<p>· 객실 10%</p>
            				</div>
            			</div>
            			<div class = "benefit-main">
            				<p>이용권</p>
            				<div class = "benefit-sub">
            					<p>· 식사 무료 제공 [조식, 중식 택1]</p>
            					<p>· 피트니스 이용권</p>
            					<p>· 사우나 이용권</p>
            				</div>
            			</div>
            			<div class = "benefit-main">
            				<p>쿠폰</p>
            				<div class = "benefit-sub">
            					<p>· 석식 뷔페 15% 할인 쿠폰</p>
            					<p>· 북카페 5,000원 할인 쿠폰</p>
            					<p>· 사우나 50% 할인 쿠폰</p>
            					<p>· 기타 어뮤니티 20% 할인 쿠폰</p>
            				</div>
            			</div>
            		</div>
            	</div>
            	<div class="membership gold">
            		<div class = "condition">
            			<span>골드 회원권 구매</span>
            		</div>
            		<div class = "gubun"></div>
            		<div class = "benefit">
            			<div class = "benefit-main">
            				<p>회원 예약 패키지</p>
            			</div>
            			<div class = "benefit-main">
            				<p>포인트 적립</p>
            				<div class = "benefit-sub">
            					<p>· 객실 15%</p>
            					<p>· 식음 5%</p>
            				</div>
            			</div>
            			<div class = "benefit-main">
            				<p>이용권</p>
            				<div class = "benefit-sub">
            					<p>· 식사 무료 제공 [조식, 중식 택1]</p>
	            				<p>· 무료 셔틀버스</p>
            					<p>· 피트니스 이용권</p>
            					<p>· 사우나 이용권</p>
            					<p>· 풀 이용권</p>
            				</div>
            			</div>
            			<div class = "benefit-main">
            				<p>쿠폰</p>
            				<div class = "benefit-sub">
            					<p>· 석식 뷔페 30% 할인 쿠폰</p>
            					<p>· 바 20% 할인 쿠폰</p>
            					<p>· 북카페 1만 원 할인 쿠폰</p>
            					<p>· 기타 어뮤니티 50% 할인 쿠폰</p>
	            				<p>· 호텔 내 올리브O 상품권 3만 원</p>
            					<p>· 관광지 쿠폰북</p>
            				</div>
            			</div>
            		</div>
            	</div>
            	<div class="membership premium">
            		<div class = "condition">
            			<span>프리미엄 회원권 구매</span>
            		</div>
            		<div class = "gubun"></div>
            		<div class = "benefit">
            			<div class = "benefit">
	            			<div class = "benefit-main">
	            				<p>회원 예약 패키지</p>
	            			</div>
	            			<div class = "benefit-main">
	            				<p>포인트 적립</p>
	            				<div class = "benefit-sub">
	            					<p>· 객실 20%</p>
	            					<p>· 식음 10%</p>
	            				</div>
	            			</div>
	            			<div class = "benefit-main">
	            				<p>이용권</p>
	            				<div class = "benefit-sub">
	            					<p>· 식사 무료 제공 [조식, 중식 택1]</p>
	            					<p>· 무료 발렛파킹</p>
	            					<p>· 무료 셔틀버스</p>
	            					<p>· 피트니스 이용권</p>
	            					<p>· 사우나 이용권</p>
	            					<p>· 풀 이용권</p>
	            					<p>· 기타 어뮤니티 이용권</p>
	            				</div>
	            			</div>
	            			<div class = "benefit-main">
	            				<p>쿠폰</p>
	            				<div class = "benefit-sub">
	            					<p>· 석식 뷔페 40% 할인 쿠폰</p>
	            					<p>· 바 30% 할인 쿠폰</p>
	            					<p>· 북카페 2만 원 할인 쿠폰</p>
	            					<p>· 호텔 내 올리브O 상품권 5만 원</p>
	            					<p>· 객실 등급 업그레이드 (기간 내 1회)</p>
	            					<p>· 관광지 쿠폰북</p>
	            				</div>
	            			</div>
	            			<div class = "benefit-main">
	            				<p>기타</p>
	            			</div>
	            		</div>
            		</div>
            	</div>
        	</div>
        	
            <div class="membership-area">
                
        	</div>
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>