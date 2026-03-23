<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>

<link rel="stylesheet" href="style/room_floor_plan.css">

<script>
	// 방 상세보기 - POST 방식
	function goRoomView(room_no){
		room.t_gubun.value = "roomView";
		room.t_room_no.value = room_no;
		
		room.method = "post";
		room.action = "RoomManager";
		room.submit();
	}
	
	// 층 변경
	function showFloor(floor) {
		currentFloor = floor;
		
		// 버튼 활성화
		try {
			document.querySelectorAll('.floor-btn').forEach(function(btn) {
				btn.classList.remove('active');
			});
			if (window.event && window.event.target) {
				window.event.target.classList.add('active');
			}
		} catch(e) {}
		
		// 객실 렌더링
		renderRooms(floor);
		
		// 정보 패널 업데이트
		updateInfoPanel(floor);
	}
	
	// 정보 패널 업데이트
	function updateInfoPanel(floor) {
		var rooms = roomsData[floor];
		var available = rooms.filter(function(r) { return r.status === 'AVAILABLE'; }).length;
		var occupied = rooms.filter(function(r) { return r.status === 'OCCUPIED'; }).length;
		var maintenance = rooms.filter(function(r) { return r.status === 'MAINTENANCE'; }).length;
		
		document.getElementById('infoPanel').innerHTML = 
			'<strong>' + floor + '층 현황</strong> | ' +
			'총 ' + rooms.length + '실 | ' +
			'예약가능: ' + available + '실 | ' +
			'투숙중: ' + occupied + '실 | ' +
			'정비중: ' + maintenance + '실';
	}
	
	// 객실 렌더링
	function renderRooms(floor) {
		var container = document.getElementById('roomsContainer');
		container.innerHTML = '';
		
		var rooms = roomsData[floor];
		
		// 각 방의 절대 위치 정의
		var roomPositions = {
			// 2층
			'201': { left: '33.5%', top: '18%', width: '11%', height: '26%' },
			'202': { left: '33.5%', top: '45%', width: '11%', height: '26%' },
			'203': { left: '33.5%', top: '72%', width: '11%', height: '26%' },
			'2F-REST': { left: '33.5%', top: '99%', width: '11%', height: '26%' },
			'204': { right: '31.5%', top: '18%', width: '13%', height: '31%' },
			'205': { right: '31.5%', top: '50%', width: '13%', height: '31%' },
			'2F-COMMUNITY': { right: '31.5%', top: '82%', width: '13%', height: '43%' },
			
			// 3층
			'301': { left: '33.5%', top: '18%', width: '11%', height: '26%' },
			'302': { left: '33.5%', top: '45%', width: '11%', height: '26%' },
			'303': { left: '33.5%', top: '72%', width: '11%', height: '26%' },
			'3F-LINEN': { left: '33.5%', top: '99%', width: '11%', height: '26%' },
			'304': { right: '31.5%', top: '18%', width: '13%', height: '31%' },
			'305': { right: '31.5%', top: '50%', width: '13%', height: '31%' },
			'3F-COMMUNITY': { right: '31.5%', top: '82%', width: '13%', height: '43%' },
			
			// 4층
			'401': { left: '33.5%', top: '18%', width: '11%', height: '26%' },
			'402': { left: '33.5%', top: '45%', width: '11%', height: '26%' },
			'403': { left: '33.5%', top: '72%', width: '11%', height: '26%' },
			'4F-LINEN': { left: '33.5%', top: '99%', width: '11%', height: '26%' },
			'404': { right: '31.5%', top: '18%', width: '13%', height: '31%' },
			'405': { right: '31.5%', top: '50%', width: '13%', height: '31%' },
			'4F-COMMUNITY': { right: '31.5%', top: '82%', width: '13%', height: '43%' },
			
			// 5층
			'501': { left: '31.5%', top: '18%', width: '13%', height: '48%' },
			'502': { right: '30.5%', top: '18%', width: '14%', height: '48%' },
			'5F-LOUNGE': { right: '30.5%', top: '67%', width: '14%', height: '38%' }
		};
		
		rooms.forEach(function(room) {
			var roomDiv = document.createElement('div');
			roomDiv.className = 'room ' + room.type.toLowerCase();
			
			var pos = roomPositions[room.no];
			if (!pos) return;
			
			if (pos.left !== undefined) {
				roomDiv.style.left = pos.left;
			} else {
				roomDiv.style.right = pos.right;
			}
			
			roomDiv.style.top = pos.top;
			roomDiv.style.width = pos.width;
			roomDiv.style.height = pos.height;
			
			var statusDot = document.createElement('div');
			statusDot.className = 'room-status-dot status-' + room.status.toLowerCase();
			roomDiv.appendChild(statusDot);
			
			var roomNumber = document.createElement('div');
			roomNumber.className = 'room-number';
			roomNumber.textContent = room.no;
			roomDiv.appendChild(roomNumber);
			
			var roomInfo = document.createElement('div');
			roomInfo.className = 'room-info';
			roomInfo.textContent = room.type + ' | ' + room.scale;
			roomDiv.appendChild(roomInfo);
			
			// 이벤트
			roomDiv.addEventListener('mouseenter', function(e) {
				var statusText = room.status === 'AVAILABLE' ? '예약가능' : 
							   room.status === 'OCCUPIED' ? '투숙중' : '정비중';
				
				var tooltipHTML = '<strong>' + room.no + '호</strong><br>' +
					'타입: ' + room.type + '<br>' +
					'크기: ' + room.scale + '<br>' +
					'기준 인원: ' + room.standard + '인 / 최대: ' + room.max + '인<br>' +
					'추가 요금: ' + room.extra.toLocaleString() + '원/인<br>' +
					'침대: ' + room.bed + '<br>' +
					'가격: ' + room.price.toLocaleString() + '원/박<br>' +
					'상태: ' + statusText;
				
				// ⭐ 메인 이미지가 있으면 표시
				if (room.mainImage) {
					tooltipHTML = '<img src="${pageContext.request.contextPath}/image' + room.mainImage + '" ' +

								  'style="width: 200px; height: 150px; object-fit: cover; border-radius: 5px; margin-bottom: 10px;"><br>' +
								  tooltipHTML;
				}
				
				tooltip.innerHTML = tooltipHTML;
				tooltip.classList.add('show');
			});
			
			roomDiv.addEventListener('mousemove', function(e) {
				tooltip.style.left = (e.clientX + 15) + 'px';
				tooltip.style.top = (e.clientY + 15) + 'px';
			});
			
			roomDiv.addEventListener('mouseleave', function() {
				tooltip.classList.remove('show');
			});
			
			roomDiv.addEventListener('click', function() {
				goRoomView(room.no);
			});
			
			container.appendChild(roomDiv);
		});
	}
	
	// 초기 렌더링
	var currentFloor = 2;
	var tooltip = null;
	
	window.onload = function() {
		tooltip = document.getElementById('tooltip');
		
		if (typeof initialFloor !== 'undefined') {
			currentFloor = initialFloor;
		}
		
		if (typeof roomsData !== 'undefined') {
			showFloor(currentFloor);
		}
	};
</script>

<!-- POST 전송용 form -->
<form name="room">
	<input type="hidden" name="t_gubun">
	<input type="hidden" name="t_room_no">
	<input type="hidden" name="t_floor">
</form>

<main>
    <div class="floor-plan-container">
        <h1 class="floor-plan-title">JSL 호텔 평면도</h1>
        
        <div class="floor-selector">
            <button class="floor-btn <c:if test='${t_floor == 5}'>active</c:if>" onclick="showFloor(5)">5층 (Suite)</button>
            <button class="floor-btn <c:if test='${t_floor == 4}'>active</c:if>" onclick="showFloor(4)">4층</button>
            <button class="floor-btn <c:if test='${t_floor == 3}'>active</c:if>" onclick="showFloor(3)">3층</button>
            <button class="floor-btn <c:if test='${t_floor == 2 || empty t_floor}'>active</c:if>" onclick="showFloor(2)">2층</button>
        </div>
        
        <div class="legend">
            <div class="legend-item">
                <div class="legend-box status-box-available"></div>
                <span>예약가능</span>
            </div>
            <div class="legend-item">
                <div class="legend-box status-box-occupied"></div>
                <span>투숙중</span>
            </div>
            <div class="legend-item">
                <div class="legend-box status-box-maintenance"></div>
                <span>정비중</span>
            </div>
            <div class="legend-item">
                <div class="legend-box standard"></div>
                <span>Standard</span>
            </div>
            <div class="legend-item">
                <div class="legend-box deluxe"></div>
                <span>Deluxe</span>
            </div>
            <div class="legend-item">
                <div class="legend-box suite"></div>
                <span>Suite</span>
            </div>
            <div class="legend-item">
                <div class="legend-box staff"></div>
                <span>직원공간</span>
            </div>
            <div class="legend-item">
                <div class="legend-box public"></div>
                <span>공용공간</span>
            </div>
        </div>
        
        <div class="floor-plan">
            <div class="corridor">
                <div class="corridor-label">복 도</div>
            </div>
            
            <!-- 엘리베이터 -->
            <div class="elevator">EV</div>
            
            <!-- 계단 -->
            <div class="stairs">계단</div>
            
            <div class="rooms-container" id="roomsContainer"></div>
        </div>
        
        <div class="scale-info">축척 1:100</div>
        
        <div class="info-panel" id="infoPanel">
            층을 선택하면 상세 평면도가 표시됩니다
        </div>
    </div>
</main>

<!-- 툴팁 -->
<div class="tooltip" id="tooltip"></div>

<footer>
    <%@ include file = "../common_footer.jsp" %>
</footer>

<script>
    // Java에서 전달받은 데이터를 JavaScript 객체로 변환
    var roomsData = {
        2: [],
        3: [],
        4: [],
        5: []
    };
    
    // JSTL로 각 층별 데이터 주입
    <c:forEach var="entry" items="${roomsByFloor}">
        <c:forEach var="room" items="${entry.value}">
            roomsData[${entry.key}].push({
                no: '${room.r_room_no}',
                type: '${room.r_type}',
                scale: '${room.r_scale}',
                standard: ${room.r_people_standard},
                max: ${room.r_people_max},
                extra: ${room.r_extra_person_fee},
                bed: '${room.r_bed_type}',
                price: ${room.r_price},
                status: '${room.r_status}',
                mainImage: '${mainImages[room.r_room_no]}'
            });
        </c:forEach>
    </c:forEach>
    
    var initialFloor = ${empty t_floor ? 2 : t_floor};
    
    console.log('roomsData:', roomsData);
</script>

</body>
</html>
