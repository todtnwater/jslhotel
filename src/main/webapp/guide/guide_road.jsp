<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_guide.jsp" %>

<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
        
        
<script>
//     // Google Maps API 초기화 함수
//     function initMap() {
//         // 지도 중앙 좌표 설정 
//         const hotelLocation = { lat: 37.5012, lng: 127.0396 }; 

//         const mapOptions = {
//             center: hotelLocation,
//             zoom: 15,
//             gestureHandling: "cooperative"
//         };

//         const map = new google.maps.Map(document.getElementById("hotelMap"), mapOptions);

//         const marker = new google.maps.Marker({
//             position: hotelLocation,
//             map: map,
//             title: "Luxury Hotel 위치"
//         });
        
//         // 정보 창 내용 수정
//         const infoWindow = new google.maps.InfoWindow({
//             content: '<h6>Luxury Hotel</h6><p>서울특별시 강남구 테헤란로 123</p>'
//         });
        
//         marker.addListener('click', function() {
//             infoWindow.open(map, marker);
//         });
//         infoWindow.open(map, marker); 
//     }
</script>

    <main>
        <div class="board-container" id="listSection">                       
	         <a href="Guide" style="text-decoration:none;">  
	            <div class="board-header">
	                <h1>호텔소개</h1>
	                <p>Luxury hotel을 가까이에서 만나보세요</p>		          
	            </div> 
	         </a>     
        	
        	<div class="content-section road-section">
	    		<div class="section-header">
	        		<h2 class="title">오시는 길 (Road)</h2>
	        		<p class="subtitle">편안하고 신속한 여정을 위해 찾아오시는 길을 상세히 안내합니다.</p>
	   			</div> 
	   			
                <div class="map-area intro-box">
                    <div class="text-content">
                        <h3>🗺️ 호텔 위치 및 주소</h3>
                        <p>
                            저희 Luxury Hotel은 인천국제공항 인근 영종도 해안가에 위치하여 서해의 아름다운 바다 전망과 함께 편안한 휴식을 제공합니다.
                            <br>아래 지도를 통해 정확한 위치를 확인하실 수 있으며, 주차장 입구는 건물 정면에 위치해 있습니다.
                        </p>
                        <div class="address-summary">
                            <p>주소: <strong>인천광역시 중구 영종해안남로 19-5</strong></p>
                            <p>대표 전화: <strong>1588-1234</strong></p>
                        </div>
                    </div>

                    <div class="hotel-image">
                         <div id="hotelMap" style="width:100%; height:100%;">

                        </div>
                    </div>
                </div>

                <div class="transport-grid">
                    <div class="transport-card">
                        <h4><span class="icon">✈️</span> 공항 안내</h4>
                        <ul>
                            <li>
                                <strong>인천국제공항</strong>
                                <p>제1터미널 14C 게이트에서 호텔 무료 셔틀버스 이용 가능 (약 10분 소요)</p>
                            </li>
                            <li>
                                <strong>공항 자기부상열차(마글레브)</strong>
                                <p>용유역 하차 후 도보 5분 거리입니다.</p>
                            </li>
                        </ul>
                    </div>

                    <div class="transport-card">
                        <h4><span class="icon">🚌</span> 버스 안내</h4>
                        <ul>
                            <li>
                                <strong>공항 리무진/좌석버스</strong>
                                <p>서울/인천 주요 지역에서 영종도 방면 버스 이용 후 '운서동' 정류장 하차</p>
                            </li>
                            <li>
                                <strong>시내버스</strong>
                                <p>202번, 302번, 306번 이용 가능 (영종도 순환 노선)</p>
                            </li>
                        </ul>
                    </div>

                    <div class="transport-card">
                        <h4><span class="icon">🚗</span> 자가용/주차 안내</h4>
                        <ul>
                            <li>
                                <strong>내비게이션 주소</strong>
                                <p>인천광역시 중구 영종해안남로 19-5 또는 'Luxury Hotel 영종' 검색</p>
                            </li>
                            <li>
                                <strong>주차장 이용</strong>
                                <p>호텔 정면 주차장 이용 (숙박 고객 무료 주차 제공)</p>
                            </li>
                            <li>
                                <strong>오시는 길</strong>
                                <p>인천대교 또는 영종대교 이용 → 영종해안남로 방면</p>
                            </li>
                        </ul>
                    </div>
                </div>
	
	   		    <div class="note">
		        	<p>문의 사항은 1588-1234로 언제든지 연락 주시면 친절하게 안내해 드리겠습니다.</p>
		    	</div>
	   		</div>
            </div>
    </main>
    
<footer>
	<%@ include file = "../common_footer.jsp" %>	
</footer>
<script>
// 임시지도
	function initMap() {
	    const lat = 37.4808;
	    const lng = 126.4185;
	    const zoomLevel = 15;
	
	    const map = L.map('hotelMap').setView([lat, lng], zoomLevel);
	
	    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
	        maxZoom: 19,
	        attribution: '© OpenStreetMap contributors' 
	    }).addTo(map);
	
	    const marker = L.marker([lat, lng]).addTo(map)
	        .bindPopup('<b>Luxury Hotel</b><br>서울특별시 강남구 테헤란로 123').openPopup();
	    map.invalidateSize(); 
	}
	
	window.onload = function() {
	    initMap();
	    setTimeout(function() {
	        if (window.L) {
	             L.map('hotelMap').invalidateSize();
	        }
	    }, 100);
	};
</script>
<!-- <script  -->
<!--     src="https://maps.googleapis.com/maps/api/js?key=[apikey입력하는곳]&callback=initMap" -->
<!--     async defer> -->
<!-- </script> -->   
</body>
</html>