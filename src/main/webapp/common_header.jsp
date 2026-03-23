<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="최고급 프리미엄 호텔 - 럭셔리한 객실, 파인 다이닝, 힐링 스파를 경험하세요">
    <meta name="keywords" content="호텔, 프리미엄, 럭셔리, 객실, 스파, 다이닝">
    <title>프리미엄 호텔 - LUXURY HOTEL</title>
    
    <link rel="stylesheet" href="style/common.css">

    <link rel="stylesheet" href="style/reservation.css">
    <link rel="stylesheet" href="style/payment.css">

    
    <script src="js/jquery-1.8.1.min.js"></script>
    <script src="js/common.js"></script>
    <script src="js/js.js"></script>
    
    <script type="text/javascript">
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
    	
    	function goCheckReservation(){
    	    work.t_gubun.value = "checkPage";
    	    work.method = "post";
    	    work.action = "Check";
    	    work.submit();
    	}
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
	<form name="work">
		<input type="hidden" name="t_gubun">
	</form>

    <header>
        <nav>
            <div class="logo"><a href="Index">LUXURY HOTEL</a></div>
            <div style="display: flex; align-items: center; gap: 2rem;">
                <ul class="nav-menu">
                    <li><a href="Index" class="<c:if test = "${headerOn eq 'room'}">active</c:if>">객실예약</a></li>
                    <li><a href="#" class="menu-link" data-menu="2" onclick="goCheckReservation()">예약확인</a></li>
                    <li><a href="Membership" class="<c:if test = "${headerOn eq 'membership'}">active</c:if>">멤버십</a></li>
                    <li><a href="Notice" class="<c:if test = "${headerOn eq 'notice' || headerOn eq 'faq' || headerOn eq 'qna'}">active</c:if>">공지사항</a></li>
                    <li><a href="Guide" class="<c:if test = "${headerOn eq 'guide'}">active</c:if>">호텔소개</a></li>
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
                <li><a href="Index">객실예약</a></li>
                <li><a href="#" onclick="alert('준비중입니다.')">예약확인</a></li>
                <li><a href="Membership">멤버십</a></li>
                <li><a href="Notice">공지사항</a></li>
                <li><a href="Guide">호텔소개</a></li>
            </ul>
        </div>
    </header>