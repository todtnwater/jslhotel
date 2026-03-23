<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file = "../common_header.jsp" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>객실 예약 - LUXURY HOTEL</title>
    <link rel="stylesheet" href="style/index.css">
    <link rel="stylesheet" href="style/room_floor_plan.css">
    <link rel="stylesheet" href="style/reservation.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function goMemberPage(gubun){
            book.t_gubun.value = gubun;
            book.method = "post";
            book.action = "Member";
            book.submit();
        }
        
        function goManagerPage(gubun){
            book.t_gubun.value = gubun;
            book.method = "post";
            book.action = "Manager";
            book.submit();
        }
        
        function goNotice(){
            book.t_gubun.value = "list";
            book.method = "post";
            book.action = "Notice";
            book.submit();
        }
        
        // 방 선택 - POST 방식으로 room_view_integrated.jsp로 이동
      function goRoomView(roomNo){
       var checkIn = document.getElementById('checkIn').value;
       var checkOut = document.getElementById('checkOut').value;
       var adults = parseInt(document.getElementById('adults').value);
       var guestCount = adults;
   
       if(!availableRoomsSet.has(roomNo)) {
           alert('이 객실은 선택하신 날짜에 예약할 수 없습니다.');
           return;
       }
   
       var room = null;
       Object.keys(allRoomsData).forEach(function(floor) {
           allRoomsData[floor].forEach(function(r) {
               if(r.no === roomNo) {
                   room = r;
               }
           });
       });
   
       if(!room) {
           alert('객실 정보를 찾을 수 없습니다.');
           return;
       }
   
       if (!checkIn || !checkOut) {
           alert('체크인/체크아웃 날짜를 선택해주세요.');
           return;
       }
   
       // book 폼 존재 여부 확인
       if (!document.book) {
           alert('폼을 찾을 수 없습니다.');
           return;
       }
   
       book.t_gubun.value = "goRoomView";
       book.t_room_no.value = roomNo;
       book.t_check_in_date.value = checkIn;
       book.t_check_out_date.value = checkOut;
       book.t_adult_count.value = adults;
       
       // t_child_count가 있는 경우에만 설정
       if (book.t_child_count) {
           book.t_child_count.value = 0;
       }
       
       book.t_guest_count.value = guestCount;
       book.t_total_price.value = room.totalPrice;
       book.method = "post";
       book.action = "Reservation";
       book.submit();
   }
        
        // 층 변경
        function showFloor(floor) {
          currentFloor = floor;
          
          // 버튼 활성화 - floor 번호로 버튼 찾기
          document.querySelectorAll('.floor-btn').forEach(function(btn) {
              btn.classList.remove('active');
              if (btn.textContent.indexOf(floor + '층') !== -1) {
                  btn.classList.add('active');
              }
          });
          
          // 객실 렌더링
          renderRooms(floor);
          
          // 정보 패널 업데이트
          updateInfoPanel(floor);
      }
        
        // 정보 패널 업데이트
        function updateInfoPanel(floor) {
            var rooms = allRoomsData[floor];
            if (!rooms || rooms.length === 0) {
                document.getElementById('infoPanel').innerHTML = 
                    '<strong>' + floor + '층 현황</strong> | 날짜를 선택하고 검색하세요';
                return;
            }
            
            // 이 층의 예약 가능한 객실 수 계산
            var availableCount = 0;
            rooms.forEach(function(room) {
                if(availableRoomsSet.has(room.no)) {
                    availableCount++;
                }
            });
            
            document.getElementById('infoPanel').innerHTML = 
                '<strong>' + floor + '층 현황</strong> | ' +
                '총 ' + rooms.length + '실 | ' +
                '예약가능: ' + availableCount + '실';
        }
        
        // 객실 렌더링
        function renderRooms(floor) {
            var container = document.getElementById('roomsContainer');
            container.innerHTML = '';
            
            var rooms = allRoomsData[floor];
            
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
            
            if (!rooms || rooms.length === 0) {
                container.innerHTML = '<div style="text-align:center; padding:50px; color:#999;">이 층에는 객실이 없습니다.</div>';
                return;
            }
            
            rooms.forEach(function(room) {
                // ⭐ 모든 공간 표시 (공용공간, 직원공간 포함)
                
                var roomDiv = document.createElement('div');
                
                // 공용공간/직원공간은 클릭 불가능하도록
                var isSpecialRoom = (room.type === 'Staff' || room.type === 'Public');
                
                // 예약 가능 여부에 따라 클래스 설정
                var isAvailable = availableRoomsSet.has(room.no);
                
                if(isSpecialRoom) {
                    // 공용공간/직원공간
                    roomDiv.className = 'room ' + room.type.toLowerCase();
                    roomDiv.style.cursor = 'default';
                } else if(!isAvailable) {
                    // 예약 불가능한 방은 회색으로
                    roomDiv.className = 'room unavailable';
                    roomDiv.style.cursor = 'not-allowed';
                } else {
                    roomDiv.className = 'room ' + room.type.toLowerCase();
                }
                
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
                statusDot.className = 'room-status-dot ' + (isAvailable ? 'status-available' : 'status-occupied');
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
                    var statusText = isAvailable ? '예약가능' : '예약불가';
                    
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
                
                // ⭐ 공용공간/직원공간은 클릭 불가
                if(!isSpecialRoom) {
                    roomDiv.addEventListener('click', function() {
                        goRoomView(room.no);
                    });
                }
                
                container.appendChild(roomDiv);
            });
        }
        
     // ✅ 검색 함수 - AJAX 방식
        function searchRooms() {
            var checkIn = document.getElementById('checkIn').value;
            var checkOut = document.getElementById('checkOut').value;
            var adults = document.getElementById('adults').value;
            var smoking = document.getElementById('smoking').value;  // ✅ smoking 추가

            if (!checkIn || !checkOut) {
                alert('체크인/체크아웃 날짜를 선택해주세요.');
                return false;
            }

            // ✅ AJAX로 검색
            $.ajax({
                url: 'Reservation',
                type: 'GET',
                data: {
                    t_gubun: 'searchAvailableRooms',
                    t_check_in_date: checkIn,
                    t_check_out_date: checkOut,
                    t_adult_count: adults,
                    t_child_count: 0,  // ✅ 0으로 고정
                    t_smoking: smoking  // ✅ smoking 추가
                },
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                },
                dataType: 'json',
                success: function(rooms) {
                    console.log('검색 결과:', rooms);

                    if (rooms.success === false) {
                        alert(rooms.message);
                        return;
                    }
                  
                  // ✅ 1. availableRoomsSet 업데이트
                  availableRoomsSet.clear();
                  rooms.forEach(function(room) {
                      availableRoomsSet.add(room.r_room_no);
                  });
                  
                  console.log('예약 가능 객실:', availableRoomsSet);
                  
                  // ✅ 2. 현재 층 다시 렌더링 (이게 핵심!)
                  renderRooms(currentFloor);
                  
                  // ✅ 3. 정보 패널 업데이트
                  updateInfoPanel(currentFloor);
                  
                  // ✅ 4. 검색 결과 표시
                  var resultInfo = document.getElementById('searchResultInfo');
                  if (!resultInfo) {
                      resultInfo = document.createElement('div');
                      resultInfo.id = 'searchResultInfo';
                      resultInfo.className = 'search-result-info';
                      document.querySelector('.reservation-search-area form').after(resultInfo);
                  }
                  resultInfo.innerHTML = '<strong>검색 결과:</strong> 선택하신 날짜에 예약 가능한 객실은 총 ' + rooms.length + '개 입니다.';
                  resultInfo.style.display = 'block';
              },
              error: function(xhr, status, error) {
                  console.error('검색 오류:', error);
                  console.error('응답:', xhr.responseText);
                  alert('객실 검색 중 오류가 발생했습니다.');
              }
          });
          
          return false;
      }

        // 초기 렌더링
        var currentFloor = 5;  // ⭐ 기본 층을 5층으로 변경
        var tooltip = null;
        var allRoomsData = {
            2: [],
            3: [],
            4: [],
            5: []
        };
        var availableRoomsSet = new Set();
        
        $(document).ready(function() {
            tooltip = document.getElementById('tooltip');
            
            const today = new Date().toISOString().split('T')[0];
            const tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            const tomorrowStr = tomorrow.toISOString().split('T')[0];
            
            // 파라미터에서 값 가져오기
            const checkInParam = '${checkInDate}';
            const checkOutParam = '${checkOutDate}';
            const adultParam = '${adultCount}';
            const childParam = '${childCount}';
            
            // 파라미터가 있으면 사용, 없으면 오늘/내일
            if (checkInParam && checkInParam !== '' && checkInParam !== 'null') {
                $('#checkIn').val(checkInParam);
                $('#checkOut').val(checkOutParam);
                $('#adults').val(adultParam || '1');
                $('#children').val(childParam || '0');
            } else {
                $('#checkIn').val(today);
                $('#checkOut').val(tomorrowStr);
            }
            
            $('#checkIn').attr('min', today);
            $('#checkOut').attr('min', today);
            
            $('#checkIn').on('change', function() {
                const checkInDate = new Date($(this).val());
                checkInDate.setDate(checkInDate.getDate() + 1);
                const minCheckOut = checkInDate.toISOString().split('T')[0];
                $('#checkOut').attr('min', minCheckOut);
                
                if ($('#checkOut').val() && $('#checkOut').val() <= $(this).val()) {
                    $('#checkOut').val(minCheckOut);
                }
            });
            
            // 서버에서 받은 데이터로 초기화
            // 1. 모든 객실 데이터
            <c:forEach var="entry" items="${allRoomsByFloor}">
                <c:forEach var="room" items="${entry.value}">
                    allRoomsData[${entry.key}].push({
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
            
            // 2. 예약 가능한 객실 Set
            <c:if test="${availableRooms != null}">
                <c:forEach var="room" items="${availableRooms}">
                    availableRoomsSet.add('${room.r_room_no}');
                </c:forEach>
            </c:if>
            
            console.log('전체 객실:', allRoomsData);
            console.log('예약 가능 객실:', Array.from(availableRoomsSet));
            
            // 초기 렌더링
            showFloor(currentFloor);
        });
    </script>
</head>

<body>
    <form name="book">
        <input type="hidden" name="t_gubun">
        <input type="hidden" name="t_room_no">
        <input type="hidden" name="t_check_in_date">
        <input type="hidden" name="t_check_out_date">
        <input type="hidden" name="t_adult_count">
        <input type="hidden" name="t_guest_count">
        <input type="hidden" name="t_total_price">
        <input type="hidden" name="t_smoking">  
    </form>

    <main>
        <!-- 검색 폼 -->
        <div class="reservation-search-area">
            <h2>객실 예약 검색</h2>
            <form onsubmit="return searchRooms();">
                <div class="form-row">
                    <div class="form-group">
                        <label for="checkIn">체크인</label>
                        <input type="date" id="checkIn" class="date-input" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="checkOut">체크아웃</label>
                        <input type="date" id="checkOut" class="date-input" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="adults">성인</label>
                        <select id="adults" class="select-input">
                            <option value="1" selected>1명</option>
                            <option value="2">2명</option>
                            <option value="3">3명</option>
                            <option value="4">4명</option>
                            <option value="5">5명</option>
                            <option value="6">6명</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
			            <label for="smoking">흡연</label>
			            <select id="smoking" class="select-input">
			                <option value="" selected>상관없음</option>
			                <option value="Y">가능</option>
			                <option value="N">불가</option>
			            </select>
			        </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn-search">객실 검색</button>
                    </div>
                </div>
            </form>
            
            <c:if test="${availableRooms != null}">
                <div id="searchResultInfo" class="search-result-info">
                    <strong>검색 결과:</strong> 선택하신 날짜에 예약 가능한 객실은 총 ${availableRooms.size()}개 입니다.
                </div>
            </c:if>
        </div>
        
        <!-- 평면도 -->
        <div class="floor-plan-container">
            <h1 class="floor-plan-title">JSL 호텔 평면도</h1>
            
            <div class="floor-selector">
                <button class="floor-btn active" onclick="showFloor(5)">5층 (Suite)</button>
                <button class="floor-btn" onclick="showFloor(4)">4층</button>
                <button class="floor-btn" onclick="showFloor(3)">3층</button>
                <button class="floor-btn" onclick="showFloor(2)">2층</button>
            </div>
            
            <div class="legend">
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
                    <div class="legend-box status-box-available"></div>
                    <span>예약가능</span>
                </div>
                <div class="legend-item">
                    <div class="legend-box status-box-occupied"></div>
                    <span>예약불가</span>
                </div>
            </div>
            
            <div class="floor-plan">
                <div class="corridor">
                    <div class="corridor-label">복 도</div>
                </div>
                
                <div class="elevator">EV</div>
                <div class="stairs">계단</div>
                
                <div class="rooms-container" id="roomsContainer"></div>
            </div>
            
            <div class="scale-info">축척 1:100</div>
            
            <div class="info-panel" id="infoPanel">
                날짜와 인원을 선택한 후 "객실 검색" 버튼을 눌러주세요
            </div>
        </div>
    </main>

    <!-- 툴팁 -->
    <div class="tooltip" id="tooltip"></div>

   <footer>
        <%@ include file = "../common_footer.jsp" %>
   </footer>
</body>
</html>
