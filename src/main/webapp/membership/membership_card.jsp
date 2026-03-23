<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>

	<aside>
	    <%@ include file = "../common/menu_membership.jsp" %>
	</aside>
	
	<style>
	.card-link {
	    /* <a> 태그가 카드를 완벽하게 감싸도록 블록 요소로 만듭니다. */
	    display: block; 
	    
	    /* <a> 태그 기본 스타일 제거 */
	    text-decoration: none;
	    color: inherit; /* 내부 텍스트 색상 상속 */
	    
	    /* 3D 효과를 위해 perspective 속성을 여기에 다시 추가할 수도 있습니다. */
	    perspective: 2000px; 
	}
	
	</style>
	
	<script>
	
		function goCardPage(gubun){
			card.t_gubun.value = gubun;
			card.method = "post";
			card.action = "Membership";
			card.submit();
		}
	
	</script>
	
	<form name = "card">
		<input type = "hidden" name = "t_gubun">
	</form>
	
	<main>
	    <div class="membership-container">
	        <div class="board-header">
	            <h1>멤버십 카드</h1>
	            <p>시즌권 구매를 통해 다양한 혜택을 누려보세요.</p>
	        </div>
	
	        <div class="membership-main2">
	            <div class="card-block-left">
	            
	            <a href="javascript:goCardPage('gold')" class="card-link">
	                <div class="membership-card gold">
	                    <div class="card-inner">
	                        <div class="card-face card-front">
	                            <div class="card-background gold"></div>
	                            <div class="card-shine"></div>
	                            <div class="card-sparkle"></div>
	                            <div class="card-glare"></div>
	                            
	                            <div class="card-content">
	                                <div class="card-header">
	                                    <h2 class="card-title">GOLD</h2>
	                                    <p class="card-subtitle">골드 등급</p>
	                                </div>
	                                
	                                <div class="card-body">
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">회원 예약 패키지</h3>
	                                    </div>
	                                    
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">포인트 적립</h3>
	                                        <ul class="benefit-list">
	                                            <li class="benefit-item">객실 15%</li>
	                                            <li class="benefit-item">식음 5%</li>
	                                        </ul>
	                                    </div>
	                                    
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">이용권</h3>
	                                        <ul class="benefit-list">
	                                            <li class="benefit-item">식사 무료 제공 [조식, 중식 택1]</li>
	                                            <li class="benefit-item">무료 셔틀버스</li>
	                                            <li class="benefit-item">피트니스 이용권</li>
	                                            <li class="benefit-item">사우나 이용권</li>
	                                            <li class="benefit-item">풀 이용권</li>
	                                        </ul>
	                                    </div>
	                                    
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">쿠폰</h3>
	                                        <ul class="benefit-list">
	                                            <li class="benefit-item">석식 뷔페 30% 할인 쿠폰</li>
	                                            <li class="benefit-item">바 20% 할인 쿠폰</li>
	                                            <li class="benefit-item">북카페 1만 원 할인 쿠폰</li>
	                                            <li class="benefit-item">기타 어뮤니티 50% 할인 쿠폰</li>
	                                            <li class="benefit-item">호텔 내 올리브O 상품권 3만 원</li>
	                                            <li class="benefit-item">관광지 쿠폰북</li>
	                                        </ul>
	                                    </div>
	                                </div>
	                                
	                                <div class="card-footer">
	                                    <p class="card-condition">골드 회원권 구매</p>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	                
	                </a>
	             </div>
	                
	          <div class="card-block-right">
	
				<a href="javascript:goCardPage('premium')" class="card-link">
	                <div class="membership-card premium">
	                    <div class="card-inner">
	                        <div class="card-face card-front">
	                            <div class="card-background premium"></div>
	                            <div class="card-shine"></div>
	                            <div class="card-sparkle"></div>
	                            <div class="card-glare"></div>
	                            
	                            <div class="card-content">
	                                <div class="card-header">
	                                    <h2 class="card-title">PREMIUM</h2>
	                                    <p class="card-subtitle">프리미엄 등급</p>
	                                </div>
	                                
	                                <div class="card-body">
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">회원 예약 패키지</h3>
	                                    </div>
	                                    
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">포인트 적립</h3>
	                                        <ul class="benefit-list">
	                                            <li class="benefit-item">객실 20%</li>
	                                            <li class="benefit-item">식음 10%</li>
	                                        </ul>
	                                    </div>
	                                    
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">이용권</h3>
	                                        <ul class="benefit-list">
	                                            <li class="benefit-item">식사 무료 제공 [조식, 중식 택1]</li>
	                                            <li class="benefit-item">무료 발렛파킹</li>
	                                            <li class="benefit-item">무료 셔틀버스</li>
	                                            <li class="benefit-item">피트니스 이용권</li>
	                                            <li class="benefit-item">사우나 이용권</li>
	                                            <li class="benefit-item">풀 이용권</li>
	                                            <li class="benefit-item">기타 어뮤니티 이용권</li>
	                                        </ul>
	                                    </div>
	                                    
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">쿠폰</h3>
	                                        <ul class="benefit-list">
	                                            <li class="benefit-item">석식 뷔페 40% 할인 쿠폰</li>
	                                            <li class="benefit-item">바 30% 할인 쿠폰</li>
	                                            <li class="benefit-item">북카페 2만 원 할인 쿠폰</li>
	                                            <li class="benefit-item">호텔 내 올리브O 상품권 5만 원</li>
	                                            <li class="benefit-item">객실 등급 업그레이드 (기간 내 1회)</li>
	                                            <li class="benefit-item">관광지 쿠폰북</li>
	                                        </ul>
	                                    </div>
	                                    
	                                    <div class="benefit-section">
	                                        <h3 class="benefit-title">기타</h3>
	                                    </div>
	                                </div>
	                                
	                                <div class="card-footer">
	                                    <p class="card-condition">프리미엄 회원권 구매</p>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                </div>
					</a>
	            </div>
	        </div>
	    </div>
	</main>
	
	<footer>
	    <%@ include file = "../common_footer.jsp" %>
	</footer>
	
	<script>
	document.addEventListener('DOMContentLoaded', function() {
	    const cards = document.querySelectorAll('.membership-card');
	    
	    cards.forEach(card => {
	        card.addEventListener('mousemove', function(e) {
	            const rect = card.getBoundingClientRect();
	            const x = e.clientX - rect.left;
	            const y = e.clientY - rect.top;
	            
	            const centerX = rect.width / 2;
	            const centerY = rect.height / 2;
	            
	            const rotateX = (y - centerY) / 10;
	            const rotateY = (centerX - x) / 10;
	            
	            const pointerX = (x / rect.width) * 100;
	            const pointerY = (y / rect.height) * 100;
	            
	            card.style.setProperty('--rotate-x', rotateY + 'deg');
	            card.style.setProperty('--rotate-y', rotateX + 'deg');
	            card.style.setProperty('--pointer-x', pointerX + '%');
	            card.style.setProperty('--pointer-y', pointerY + '%');
	        });
	        
	        card.addEventListener('mouseleave', function() {
	            card.style.setProperty('--rotate-x', '0deg');
	            card.style.setProperty('--rotate-y', '0deg');
	        });
	    });
	});
	</script>

</body>
</html>
