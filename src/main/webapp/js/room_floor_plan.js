/* /js/room_floor_plan.js */

// 전역 변수 선언
var currentFloor = 5;
var tooltip = null;

// 층 변경
function showFloor(floor) {
    currentFloor = floor;
    
    // 버튼 활성화 - event 체크를 완전히 안전하게
    try {
        document.querySelectorAll('.floor-btn').forEach(function(btn) {
            btn.classList.remove('active');
        });
        if (window.event && window.event.target) {
            window.event.target.classList.add('active');
        } else {
            // event가 없으면 floor로 찾아서 active 설정
            document.querySelectorAll('.floor-btn').forEach(function(btn) {
                if (btn.textContent.indexOf(floor + '층') !== -1) {
                    btn.classList.add('active');
                }
            });
        }
    } catch(e) {
        // 에러 무시하고 계속 진행
    }
    
    // 객실 렌더링
    renderRooms(floor);
    
    // 정보 패널 업데이트
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
    
    // HTML 이미지 기준 - 각 방의 절대 위치 정의
    var roomPositions = {
        // 2층 - HTML 이미지와 동일하게
        '201': { left: '33.5%', top: '18%', width: '11%', height: '26%' },
        '202': { left: '33.5%', top: '45%', width: '11%', height: '26%' },
        '203': { left: '33.5%', top: '72%', width: '11%', height: '26%' },
        '2F-REST': { left: '33.5%', top: '99%', width: '11%', height: '26%' },
        '204': { right: '31.5%', top: '18%', width: '13%', height: '31%' },
        '205': { right: '31.5%', top: '50%', width: '13%', height: '31%' },
        '2F-COMMUNITY': { right: '31.5%', top: '82%', width: '13%', height: '43%' },
        
        // 3층 - HTML 이미지와 동일하게
        '301': { left: '33.5%', top: '18%', width: '11%', height: '26%' },
        '302': { left: '33.5%', top: '45%', width: '11%', height: '26%' },
        '303': { left: '33.5%', top: '72%', width: '11%', height: '26%' },
        '3F-LINEN': { left: '33.5%', top: '99%', width: '11%', height: '26%' },
        '304': { right: '31.5%', top: '18%', width: '13%', height: '31%' },
        '305': { right: '31.5%', top: '50%', width: '13%', height: '31%' },
        '3F-COMMUNITY': { right: '31.5%', top: '82%', width: '13%', height: '43%' },
        
        // 4층 - HTML 이미지와 동일하게
        '401': { left: '33.5%', top: '18%', width: '11%', height: '26%' },
        '402': { left: '33.5%', top: '45%', width: '11%', height: '26%' },
        '403': { left: '33.5%', top: '72%', width: '11%', height: '26%' },
        '4F-LINEN': { left: '33.5%', top: '99%', width: '11%', height: '26%' },
        '404': { right: '31.5%', top: '18%', width: '13%', height: '31%' },
        '405': { right: '31.5%', top: '50%', width: '13%', height: '31%' },
        '4F-COMMUNITY': { right: '31.5%', top: '82%', width: '13%', height: '43%' },
        
        // 5층 - HTML 이미지와 동일하게
        '501': { left: '31.5%', top: '18%', width: '13%', height: '48%' },
        '502': { right: '30.5%', top: '18%', width: '14%', height: '48%' },
        '5F-LOUNGE': { right: '30.5%', top: '67%', width: '14%', height: '38%' }
    };
    
    rooms.forEach(function(room) {
        var roomDiv = document.createElement('div');
        roomDiv.className = 'room ' + room.type.toLowerCase();
        
        // 고정된 위치 가져오기
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
            
            tooltip.innerHTML = 
                '<strong>' + room.no + '호</strong><br>' +
                '타입: ' + room.type + '<br>' +
                '크기: ' + room.scale + '<br>' +
                '기준 인원: ' + room.standard + '인 / 최대: ' + room.max + '인<br>' +
                '추가 요금: ' + room.extra.toLocaleString() + '원/인<br>' +
                '침대: ' + room.bed + '<br>' +
                '가격: ' + room.price.toLocaleString() + '원/박<br>' +
                '상태: ' + statusText;
            tooltip.classList.add('show');
        });
        
        roomDiv.addEventListener('mousemove', function(e) {
            tooltip.style.left = (e.clientX + 15) + 'px';
            tooltip.style.top = (e.clientY + 15) + 'px';
        });
        
        roomDiv.addEventListener('mouseleave', function() {
            tooltip.classList.remove('show');
        });
        
        // 클릭 이벤트
        roomDiv.addEventListener('click', function() {
            if (room.type !== 'Staff' && room.type !== 'Public') {
                location.href = 'Manager?t_gubun=roomView&t_room_no=' + room.no;
            }
        });
        
        container.appendChild(roomDiv);
    });
}

// 초기 렌더링
document.addEventListener('DOMContentLoaded', function() {
    tooltip = document.getElementById('tooltip');
    
    // JSP에서 주입된 초기 층수 사용 (없으면 5층)
    if (typeof initialFloor !== 'undefined') {
        currentFloor = initialFloor;
    }
    
    if (typeof roomsData !== 'undefined') {
        showFloor(currentFloor);
    }
});