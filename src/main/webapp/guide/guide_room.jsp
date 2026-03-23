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
        	
        	<div class="content-section room-section">
	    		<div class="section-header">
	        		<h2 class="title">객실 안내 (Rooms)</h2>
	        		<p class="subtitle">고객님의 편안한 휴식을 위해 다양한 스타일의 객실을 준비했습니다.</p>
	   			</div> 
	   			
                <div class="room-list-container">
                    
                    <div class="room-card">
                        <div class="room-image">
                            <img src="images/Suite_Room.jpg" alt="Standard Room Image" class="room-img"/> 
                        </div>
                        <div class="room-details">
                            <h3>Standard Room</h3>
                            <p class="room-desc">합리적인 가격과 효율적인 공간 활용을 제공하는 기본 객실입니다. 깔끔하고 아늑한 분위기 속에서 편안함을 느껴보세요.</p>
                            
                            <ul class="room-features">
                                <li><strong>면적:</strong> 10평 (33m²)</li>
                                <li><strong>수용 인원:</strong> 2인 (최대 3인)</li>
                                <li><strong>주요 특징:</strong> 시티 뷰 또는 마운틴 뷰, 퀸 사이즈 침대</li>
                                <li><strong>예약:</strong> <a href="Reservation" class="btn-reserve">예약하기</a></li>
                            </ul>
                        </div>
                    </div>

                    <div class="room-card">
                        <div class="room-image">
                            <img src="images/Suite_Room.jpg" alt="Deluxe Room Image" class="room-img"/>
                        </div>
                        <div class="room-details">
                            <h3>Deluxe Room</h3>
                            <p class="room-desc">더 넓은 공간과 품격 있는 디자인을 갖춘 객실입니다. 출장 고객이나 여유로운 휴식을 원하는 고객에게 적합합니다.</p>
                            
                            <ul class="room-features">
                                <li><strong>면적:</strong> 15평 (50m²)</li>
                                <li><strong>수용 인원:</strong> 2인 (최대 4인)</li>
                                <li><strong>주요 특징:</strong> 레이크 뷰, 킹 사이즈 침대 또는 트윈 베드 선택 가능</li>
                                <li><strong>예약:</strong> <a href="Reservation" class="btn-reserve">예약하기</a></li>
                            </ul>
                        </div>
                    </div>

                    <div class="room-card">
                        <div class="room-image">
                            <img src="images/Suite_Room.jpg" alt="Suite Room Image" class="room-img"/>
                        </div>
                        <div class="room-details">
                            <h3>Suite Room</h3>
                            <p class="room-desc">침실과 거실이 분리된 최고급 객실입니다. 품격 있는 인테리어와 전면 통유리 창을 통한 파노라마 뷰를 경험해 보세요.</p>
                            
                            <ul class="room-features">
                                <li><strong>면적:</strong> 30평 (100m²)</li>
                                <li><strong>수용 인원:</strong> 2인 (최대 4인)</li>
                                <li><strong>주요 특징:</strong> 분리형 거실/침실, 전용 미니바, 최고급 어메니티 제공</li>
                                <li><strong>예약:</strong> <a href="Reservation" class="btn-reserve">예약하기</a></li>
                            </ul>
                        </div>
                    </div>

                </div>
                
                <div class="note">
		        	<p>객실 사진은 대표 이미지이며, 실제 객실 구조 및 비품은 다소 상이할 수 있습니다. 자세한 정보는 예약 시 문의해 주세요.</p>
		    	</div>
	   		</div>
            </div>
    </main>
    
<footer>
	<%@ include file = "../common_footer.jsp" %>	
</footer>
    
</body>
</html>