<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_guide.jsp" %>

    <main>
        <div class="board-container" id="listSection">                       
	         <a href="Guide" style="text-decoration:none;">  
	            <div class="board-header">
	                <h1>호텔소개</h1>
	                <p>Luxury hotel을 가까이에서 만나보세요</p>		          
	            </div> 
	         </a>     
        	
        	<div class="content-section facility-section">
	    		<div class="section-header">
	        		<h2 class="title">부속시설 안내</h2>
	        		<p class="subtitle">고객님의 편안하고 즐거운 체류를 위한 다양한 편의 시설을 이용해 보세요.</p>
	   			</div> 
	   			
                <div class="facility-grid">
                    
                    <div class="facility-card">
                        <div class="facility-image">
                            <img src="images/Spa.jpg" alt="Fitness Center Image" class="facility-img"/> 
                        </div>
                        <div class="facility-details">
                            <h3>피트니스 센터</h3>
                            <p class="facility-desc">최신 장비와 전문 트레이너가 상주하는 공간에서 건강한 활력을 되찾으세요.</p>
                            <ul class="facility-info">
                                <li>운영 시간 : 06:00 ~ 22:00</li>
                                <li>위치 : 호텔 3층</li>
                            </ul>
                        </div>
                    </div>

                    <div class="facility-card">
                        <div class="facility-image">
                            <img src="images/Spa.jpg" alt="Indoor Pool Image" class="facility-img"/>
                        </div>
                        <div class="facility-details">
                            <h3>실내 수영장</h3>
                            <p class="facility-desc">사계절 내내 이용 가능한 온수풀과 편안한 선베드를 갖춘 실내 수영장입니다.</p>
                            <ul class="facility-info">
                                <li>운영 시간 : 09:00 ~ 20:00 (매월 셋째 주 화요일 휴무)</li>
                                <li>위치 : 호텔 2층</li>
                            </ul>
                        </div>
                    </div>

                    <div class="facility-card">
                        <div class="facility-image">
                            <img src="images/Dining.jpg" alt="All-day Dining Image" class="facility-img"/>
                        </div>
                        <div class="facility-details">
                            <h3>올데이 다이닝</h3>
                            <p class="facility-desc">신선한 재료로 만든 다채로운 뷔페와 코스 요리를 하루 종일 즐기실 수 있습니다.</p>
                            <ul class="facility-info">
                                <li>운영 시간 : 07:00 ~ 21:00 (브레이크 타임: 15:00 ~ 17:00)</li>
                                <li>위치 : 호텔 1층</li>
                            </ul>
                        </div>
                    </div>
                    
                    <div class="facility-card">
                        <div class="facility-image">
                            <img src="images/Dining.jpg" alt="Business Lounge Image" class="facility-img"/>
                        </div>
                        <div class="facility-details">
                            <h3>5F 루프탑 라운지 'The Zenith'</h3>
                            <p class="facility-desc">편안한 분위기에서 최고의 시간을 보낼 수 있는 바입니다.</p>
                            <ul class="facility-info">
                                <li>운영 시간 : 09:00 ~ 23:00 (음료/스낵 유료)</li>
                                <li>위치 : 호텔 5층</li>
                            </ul>
                        </div>
                    </div>

                </div>
                
                <div class="note">
		        	<p>부속시설 이용에 관한 자세한 규정 및 요금은 시설별로 상이하므로, 방문 전 문의해 주시기 바랍니다.</p>
		    	</div>
	   		</div>
            </div>
    </main>
    
<footer>
	<%@ include file = "../common_footer.jsp" %>	
</footer>
    
</body>
</html>