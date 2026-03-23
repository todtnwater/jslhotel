<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
<link rel="stylesheet" href="style/manager.css">
<link rel="stylesheet" href="style/member.css">
<link rel="stylesheet" href="style/qna.css">
<link rel="stylesheet" href="style/faq.css">
<link rel="stylesheet" href="style/write.css">
<link rel="stylesheet" href="style/room_floor_plan.css">
</head>

<script>

	function goTemp(){
		alert('기능 구현 예정');
	}
	
	// 관리자 페이지 이동
	function goManagerPage(gubun){
		work.t_gubun.value = gubun;
		work.method = "post";
		work.action = "Manager";
		work.submit();
	}
	
	// 룸 페이지
	function goRoomPage(gubun){
		work.t_gubun.value = gubun;
		work.method = "post";
		work.action = "RoomManager";
		work.submit();
	}

</script>

<!-- 관리자 권한이 있을 때만 표시 -->
<c:if test="${sessionLevel eq 'top'}">
<aside class="manager-menu">

<h3>관리자페이지</h3>
<ul id="snb">
	<li class="<c:if test = "${menuOn eq 'memberList' || menuOn eq 'memberView'}">on</c:if>">
		<a href="javascript:goManagerPage('memberList')" id="cat5">
			회원관리
		</a>
	</li>
	<li class="<c:if test = "${menuOn eq 'roomList' || menuOn eq 'roomView'}">on</c:if>">
		<a href="javascript:goRoomPage('roomList')" id="cat6">
			객실관리
		</a>
	</li>
	<li class="<c:if test = "${menuOn eq 'bookList'}">on</c:if>">
	<a href="javascript:goManagerPage('bookList')" id="cat4">
		예약리스트
	</a>
	</li>
	<li class="<c:if test = "${menuOn eq 'qnaList' || menuOn eq 'qnaView'}">on</c:if>">
		<a href="javascript:goManagerPage('qnaList')" id="cat4">
			Q&A관리
		</a>
	</li>
	<li class="<c:if test = "${menuOn eq 'faqList' || menuOn eq 'faqView' || menuOn eq 'faqWriteForm'}">on</c:if>">
		<a href="javascript:goManagerPage('faqList')" id="cat4">
			FAQ관리
		</a>
	</li>
	<li class="<c:if test = "${menuOn eq 'myInfo'}">on</c:if>">
		<a href="javascript:goMemberPage('myInfo')" id="cat3">
			마이페이지
		</a>
	</li>
	<li class="<c:if test = "${menuOn eq 'password'}">on</c:if>">
		<a href="javascript:goMemberPage('password')" id="cat3">
			비밀번호 변경
		</a>
	</li>
</ul>

</aside>
</c:if>
