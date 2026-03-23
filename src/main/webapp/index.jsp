<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="dto.NoticeDto" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="최고급 프리미엄 호텔 - 럭셔리한 객실, 파인 다이닝, 힐링 스파를 경험하세요">
    <meta name="keywords" content="호텔, 프리미엄, 럭셔리, 객실, 스파, 다이닝">
    <title>프리미엄 호텔 - LUXURY HOTEL</title>
    
    <!-- jQuery 먼저 로드 -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- CSS 파일 -->
    <link rel="stylesheet" href="style/index.css">
    <link rel="stylesheet" href="style/reservation.css">
    
    <!-- JS 파일 (jQuery 다음에 로드) -->
    <script src="js/common.js"></script>
    <script src="js/js.js"></script>
    
    <script>
    	function goMemberPage(gubun){
    		work.t_gubun.value = gubun;
    		work.method = "post";
    		work.action = "Member";
    		work.submit();
    	}
    
    	function goManagerPage(gubun){
    		work.t_gubun.value = gubun;
    		work.method = "post";
    		work.action = "Manager";
    		work.submit();
    	}
    	
    	function goMembershipPage(){
    		work.method = "post";
    		work.action = "Membership";
    		work.submit();
    	}
    	
    	function goNotice(){
    		work.t_gubun.value = "list";
    		work.method = "post";
    		work.action = "Notice";
    		work.submit();
    	}
    	
    	function goMembership(){
    	    work.t_gubun.value = "membershipList";
    	    work.method = "post";
    	    work.action = "Membership"; 
    	    work.submit();
    	}
    	
    	function goReservation(){
    		var checkIn = document.getElementById('checkin').value;
    		var checkOut = document.getElementById('checkout').value;
    		var adults = document.getElementById('adult').value;
    		var smoking = document.getElementById('smoking').value;
    		
    		work.t_gubun.value = "reservation";
    		work.t_check_in_date.value = checkIn;
    		work.t_check_out_date.value = checkOut;
    		work.t_adult_count.value = adults;
    		work.t_smoking.value = smoking;
    		work.method = "post";
    		work.action = "Reservation";
    		work.submit();
    	}
    	
    	function goCheckReservation(){
    	    work.t_gubun.value = "checkPage";
    	    work.method = "post";
    	    work.action = "Check";
    	    work.submit();
    	}
    	
    	function goGuide(){
    	    work.t_gubun.value = "overview";
    	    work.method = "post";
    	    work.action = "Guide"; 
    	    work.submit();
    	} 
    	
    	function scrollToBookingForm(){
    		document.querySelector('.reservation-area').scrollIntoView({ 
    			behavior: 'smooth' 
    		});
    	}
    	
    	function goToCategory(gubun) {
    		alert('카테고리 ' + gubun + ' 페이지는 준비중입니다.');
    	}
    	
    	function openQA() {
    		document.getElementById('qaModal').style.display = 'flex';
    	}
    	
    	function closeQA() {
    		document.getElementById('qaModal').style.display = 'none';
    	}
    	
    	function searchRooms() {
    		var checkIn = document.getElementById('checkin').value;
    		var checkOut = document.getElementById('checkout').value;
    		
    		if (!checkIn || !checkOut) {
    			alert('체크인/체크아웃 날짜를 선택해주세요.');
    			return;
    		}
    		
    		if (checkIn >= checkOut) {
    			alert('체크아웃 날짜는 체크인 날짜보다 이후여야 합니다.');
    			return;
    		}
    		
    		goReservation();
    	}
    	
    	function showNoticeBanner() {
    		// 메뉴 활성화 상태 변경
    		$('.menu-link').removeClass('active');
    		$('.menu-link[data-menu="notice"]').addClass('active');
    		
    		// 배너 슬라이드 변경
    		$('.banner-slide').removeClass('active');
    		$('.banner-slide[data-banner="notice"]').addClass('active');
    		
    		// 페이지 상단으로 스크롤
    		window.scrollTo({ top: 0, behavior: 'smooth' });
    	}
    	
    	// 예약번호로 조회
    	function checkReservationByNumber(){
    		var rvNo = document.getElementById('check_rvNo').value;
    		
    		if (!rvNo || !rvNo.trim()) {
    			alert('예약번호를 입력해주세요.');
    			document.getElementById('check_rvNo').focus();
    			return;
    		}
    		
    		work.t_gubun.value = "checkReservation";
    		work.searchType.value = "number";
    		work.rvNo.value = rvNo.trim();
    		work.method = "post";
    		work.action = "Check";
    		work.submit();
    	}
    	
    	// 회원으로 조회
    	function checkReservationByMember(){
    		work.t_gubun.value = "checkReservation";
    		work.searchType.value = "member";
    		work.method = "post";
    		work.action = "Check";
    		work.submit();
    	}
    	
    	// 예약 조회 탭 전환
    	function switchCheckTab(tabName) {
    		// 탭 버튼 활성화
    		document.querySelectorAll('.check-tab-btn').forEach(btn => {
    			btn.classList.remove('active');
    			btn.style.background = 'white';
    			btn.style.color = '#4D869C';
    		});
    		event.target.classList.add('active');
    		event.target.style.background = '#4D869C';
    		event.target.style.color = 'white';
    		
    		// 탭 콘텐츠 표시
    		document.querySelectorAll('.check-tab-content').forEach(content => {
    			content.style.display = 'none';
    		});
    		document.getElementById(tabName).style.display = 'block';
    	}
    	
    	$(document).ready(function() {
    		const today = new Date().toISOString().split('T')[0];
    		const tomorrow = new Date();
    		tomorrow.setDate(tomorrow.getDate() + 1);
    		const tomorrowStr = tomorrow.toISOString().split('T')[0];
    		
    		// 체크인 날짜를 오늘로 자동 설정
    		$('#checkin').val(today);
    		$('#checkin').attr('min', today);
    		
    		// 체크아웃 날짜를 내일로 자동 설정
    		$('#checkout').val(tomorrowStr);
    		$('#checkout').attr('min', today);
    		
    		$('#checkin').on('change', function() {
    			const checkInDate = new Date($(this).val());
    			checkInDate.setDate(checkInDate.getDate() + 1);
    			const minCheckOut = checkInDate.toISOString().split('T')[0];
    			$('#checkout').attr('min', minCheckOut);
    			
    			if ($('#checkout').val() && $('#checkout').val() <= $(this).val()) {
    				$('#checkout').val(minCheckOut);
    			}
    		});
    		
    		// 메뉴 클릭 시 배너 슬라이드 변경
    		$('.menu-link[data-menu], .mobile-menu-link[data-menu]').on('click', function(e) {
    			e.preventDefault();
    			const menuNum = $(this).data('menu');
    			
    			// 공지사항 메뉴는 별도 처리하지 않음 (data-menu 속성이 없음)
    			if (!menuNum) {
    				return;
    			}
    			
    			// 메뉴 활성화 상태 변경
    			$('.menu-link').removeClass('active');
    			$('.menu-link[data-menu="' + menuNum + '"]').addClass('active');
    			
    			// 배너 슬라이드 변경
    			$('.banner-slide').removeClass('active');
    			$('.banner-slide[data-banner="' + menuNum + '"]').addClass('active');
    			
    			// 페이지 상단으로 스크롤
    			window.scrollTo({ top: 0, behavior: 'smooth' });
    		});
    	});
    </script>
    <style>
        .goog-te-banner-frame { display: none !important; }
        body { top: 0 !important; }
        .lang-area { display: inline-flex; align-items: center; gap: 6px; margin-right: 4px; }
        .ko-btn { padding: 3px 8px; border: 1px solid #4D869C; border-radius: 4px; background: white; color: #4D869C; cursor: pointer; font-size: 13px; }
        .ko-btn:hover { background: #4D869C; color: white; }
        .nav-menu li a,
		.login-btn {
		    white-space: nowrap;
		}
    </style>
    <script>
    function googleTranslateElementInit() {
        new google.translate.TranslateElement({
            pageLanguage: 'ko',
            includedLanguages: 'ja,en',
            layout: google.translate.TranslateElement.InlineLayout.SIMPLE,
            autoDisplay: false
        }, 'google_translate_element');
    }
    function restoreKorean() {
        document.cookie = 'googtrans=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
        document.cookie = 'googtrans=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=' + location.hostname;
        location.reload();
    }
    </script>
    <script src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</head>
<body>
    <%
        // 팝업 공지사항 조회 (서블릿에서 처리되었다면 이 부분은 생략 가능)
        NoticeDto popupNotice = (NoticeDto)request.getAttribute("popupNotice");
    %>
    
    <form name="work">
        <input type="hidden" name="t_gubun">
        <input type="hidden" name="t_check_in_date">
        <input type="hidden" name="t_check_out_date">
        <input type="hidden" name="t_adult_count">
        <input type="hidden" name="t_smoking">
        <!-- 예약 조회용 추가 -->
        <input type="hidden" name="searchType">
        <input type="hidden" name="rvNo">
    </form>
    
    <!-- ========== 팝업 HTML이 들어갈 위치 ========== -->
    <!-- 여기에 팝업 HTML 코드 삽입 -->
    <!-- ========================================== -->
	<c:if test="${popupNotice != null}">
	    <div id="noticePopupOverlay" class="notice-popup-overlay" data-notice-no="${popupNotice.n_no}">
	        <div class="notice-popup-modal">
	            <div class="notice-popup-header">
	                <h3 class="notice-popup-title">
	                    공지사항
	                </h3>
	                <button type="button" class="notice-popup-close" onclick="closeNoticePopup()">
	                    &times;
	                </button>
	            </div>
	            <div class="notice-popup-body">
	                <h4>${popupNotice.n_title}</h4>
	                <hr style="border: none; border-top: 1px solid #e5e5e5; margin: 15px 0;">
	                <div class="notice-popup-content">${popupNotice.n_content}</div>
	                <div class="notice-period">
	                    📅 공지 기간: ${popupNotice.popupStartDate} ~ ${popupNotice.popupEndDate}
	                </div>
	            </div>
	            <div class="notice-popup-footer">
	                <div class="notice-popup-checkbox">
	                    <input type="checkbox" id="todayCloseNotice">
	                    <label for="todayCloseNotice">오늘 하루 열지 않기</label>
	                </div>
	                <button type="button" class="notice-popup-btn" onclick="closeNoticePopup()">
	                    확인
	                </button>
	            </div>
	        </div>
	    </div>
	</c:if>
    <header>
        <nav>
            <div class="logo"><a href="Index">LUXURY HOTEL</a></div>
            <div style="display: flex; align-items: center; gap: 2rem;">
                <ul class="nav-menu">
                    <li><a href="#" class="menu-link active" data-menu="1">객실예약</a></li>
                    <li><a href="#" class="menu-link" data-menu="2">예약확인</a></li>
                    <li><a href="#" class="menu-link" data-menu="3">멤버십</a></li>
                    <li><a href="javascript:showNoticeBanner()" class="menu-link" data-menu="notice">공지사항</a></li>
                    <li><a href="#" class="menu-link" data-menu="5">호텔소개</a></li>
                </ul>
                <button class="mobile-menu-toggle" aria-label="메뉴 열기">☰</button>
                <c:if test="${empty sessionName}">
                    <a href="javascript:goMemberPage('join')" class="login-btn">회원가입</a>
                    <a href="javascript:goMemberPage('loginForm')" class="login-btn">로그인</a>
                </c:if>
                <c:if test="${not empty sessionName}">
                    <c:if test="${sessionLevel eq 'top'}">
                        <a href="javascript:goManagerPage('memberList')" class="login-btn">관리자</a>
                    </c:if>
                    <c:if test="${sessionLevel ne 'top'}">
                        <a href="javascript:goMemberPage('myInfo')" class="login-btn">마이페이지</a>
                    </c:if>
                    <a href="javascript:goMemberPage('logout')" class="login-btn">로그아웃</a>
                </c:if>
                 <div class="lang-area">
                    <div id="google_translate_element"></div>
                    <button class="ko-btn" onclick="restoreKorean()">한국어</button>
                </div>
            </div>
        </nav>
        
        <div class="mobile-menu">
            <ul class="mobile-nav-menu">
                <li><a href="#" class="mobile-menu-link active" data-menu="1">객실예약</a></li>
                <li><a href="#" class="mobile-menu-link" data-menu="2">예약확인</a></li>
                <li><a href="#" class="mobile-menu-link" data-menu="3">멤버십</a></li>
                <li><a href="javascript:showNoticeBanner()" class="menu-link" data-menu="notice">공지사항</a></li>
                <li><a href="#" class="mobile-menu-link" data-menu="5">호텔소개</a></li>
            </ul>
        </div>
    </header>

    <main>
        <section class="main-banner">
            <div class="banner-slide active" data-banner="1">
                <div class="banner-content1">
                    <div class="reservation-area">
                        <div class="reservation-container">
                            <h2 class="reservation-title">객실 예약</h2>
                            <div class="reservation-form">
                                <div class="form-group">
                                    <label for="checkin">체크인</label>
                                    <input type="date" id="checkin" class="date-input" required>
                                </div>
                                
                                <div class="form-group">
                                    <label for="checkout">체크아웃</label>
                                    <input type="date" id="checkout" class="date-input" required>
                                </div>
                                
                                <div class="form-group">
                                    <label for="adult">투숙 인원(성인)</label>
                                    <select id="adult" class="select-input">
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
                                    <button type="button" onclick="searchRooms()" class="btn-search">
                                        객실 검색
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
        <div class="banner-slide" style="height:200%; width:100%; position:relative;" data-banner="2">
		    <div style="position:absolute; top:0; left:0; width:100%; height:100%; background-image:url('images/banner2.png'); background-repeat:repeat-x; background-size:auto 100%; opacity: 0.8;"></div>
		    <div class="banner-content" style="position:relative; z-index:1;">
		        <h1>예약확인</h1>
		        <p>예약확인 입니다.</p>
		        <button type="button" class="banner-btn" onclick="goCheckReservation()">예약확인 하기</button>
		    </div>
		</div>
		
		<div class="banner-slide" style="height:200%; width:100%; position:relative;" data-banner="3">
		    <div style="position:absolute; top:0; left:0; width:100%; height:100%; background-repeat:repeat-x; background-image:url('images/banner2.png'); background-size:auto 100%; opacity: 0.8;"></div>
		    <div class="banner-content" style="position:relative; z-index:1;">
		        <h1>멤버십</h1>
		        <p>멤버십 설명입니다</p>
		        <button type="button" class="banner-btn" onclick="goMembership()">멤버십 보기</button>
		    </div>
		</div>
		
		<div class="banner-slide" style="height:200%; width:100%; position:relative;" data-banner="notice">
		    <div style="position:absolute; top:0; left:0; width:100%;  height:100%; background-image:url('images/banner2.png');  background-repeat:repeat-x; background-size:auto 100%; opacity: 0.8;"></div>
		    <div class="banner-content" style="position:relative; z-index:1;">
		        <h1>공지사항</h1>
		        <p>호텔의 최신 소식과 공지사항을 확인하세요</p>
		        <button type="button" class="banner-btn" onclick="goNotice()">공지사항 보기</button>
		    </div>
		</div>
		
		<div class="banner-slide" style="height:200%; width:100%; position:relative;" data-banner="5">
		    <div style="position:absolute; top:0; left:0; width:100%; height:100%;  background-image:url('images/banner2.png');  background-repeat:repeat-x; background-size:auto 100%; opacity: 0.8;"></div>
		    <div class="banner-content" style="position:relative; z-index:1;">
		        <h1>호텔소개</h1>
		        <p>Luxury Hotel을 가까이에서 만나보세요</p>
		        <button type="button" class="banner-btn" onclick="goGuide()">호텔소개 보기</button>
		    </div>
		</div>
        </section>
        
        <div class="indexgubun"></div>
        
        <section class="facilities" id="facilities-section">
            <h2>시설안내</h2>
            <p class="facilities-subtitle">호텔의 시설을 확인해보세요.</p>
            
            <div class="category-buttons">
                <button class="category-btn active" data-category="1">객실 유형</button>
                <button class="category-btn" data-category="2">레스토랑</button>
                <button class="category-btn" data-category="3">부가시설</button>
                <button class="category-btn" data-category="4">주변시설</button>
            </div>

            <div class="category-container category1 show">
                <div class="facility-grid">
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a><img src = "images/standard1.jpg" alt="스탠다드룸" style = "width:100%; height:100%;"></a>	
                        </div>
                        <div class="facility-info">
                            <h3>Standard</h3>
                            <p>비즈니즈를 위한 편안한 휴식</p>
                            <div class="facility-time">보러가기</div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a><img src = "images/delux4.jpg" alt="디럭스룸" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>Deluxe</h3>
                            <p>가족을 위한 즐거운 여행후 휴식</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/standard1.jpg" alt="스위트룸" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>Suite</h3>
                            <p>오늘 하루를 위한 최고의 선택</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="category-container category2">
                <div class="facility-grid">
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/dining1.png" alt="조식" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>조식</h3>
                            <p>06:00 - 09:00</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/dining2.png" alt="중식" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>중식</h3>
                            <p>11:00 - 14:00</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/dining3.png" alt="석식" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>석식</h3>
                            <p>17:00 - 20:00</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="category-container category3">
                <div class="facility-grid">
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/pool.png" alt="수영장" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>수영장</h3>
                            <p>수영해요</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/fitness.png" alt="피트니스" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>피트니스 센터</h3>
                            <p>건강해지죠</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/Spa.jpg" alt="스파" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>스파</h3>
                            <p>뜨끈해요</p>
                            <div class="facility-time">추가 정보</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="category-container category4">
                <div class="facility-grid">
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/cafe.png" alt="카페" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>카페</h3>
                            <p>마음의 양식과 뇌의 양식</p>
                            <div class="facility-price">추가 정보</div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/bar.png" alt="바" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>바</h3>
                            <p>당신의 눈동자에 치어스~</p>
                            <div class="facility-price"></div>
                        </div>
                    </div>
                    <div class="facility-card">
                        <div class="facility-image">
                        	<a href = "javascript:goGuide()"><img src = "images/billiards.png" alt="당구" style = "width:100%; height:100%;"></a>
                        </div>
                        <div class="facility-info">
                            <h3>어뮤니티</h3>
                            <p>친구들과 함께 피로를 날려보아요.</p>
                            <div class="facility-price"></div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <div class="notification" id="notification"></div>
    </main>

    <footer>
        <div class="footer-content">
            <p>&copy; 2025 LUXURY HOTEL. All rights reserved.</p>
            <p>📞 1588-1234 | 📧 info@luxuryhotel.com</p>
            <p>📍 인천광역시 중구 영종해안남로 19-5</p>
        </div>
    </footer>
</body>
</html>