<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<link rel="stylesheet" href="style/membership.css">
	<link rel="stylesheet" href="style/membership_card.css">
	<link rel="stylesheet" href="style/board.css">
	<link rel="stylesheet" href="style/room_view.css">
    <link rel="stylesheet" href="style/room_floor_plan.css">
</head>

	<script>
		
		function goTemp(){
			alert('기능 구현 예정');
		}
	
		function goMembershipPage(gubun){
			membership.t_gubun.value = gubun;
			membership.method = "post";
			membership.action = "Membership";
			membership.submit();
		}
		
		function goQnaPage(gubun){
			membership.t_gubun.value = gubun;
			membership.method = "post";
			membership.action = "Qna";
			membership.submit();
		}
		
	</script>

	<form name = "membership">
		<input type = "hidden" name = "t_gubun">
	</form>

	<aside>
        <h3>멤버십</h3>
        <ul id="snb">
            <li class = "<c:if test = "${menuOn eq 'membership'}">on</c:if>">
            	<a href="javascript:goMembershipPage('membership')" id="cat0">
            		멤버십 특전
            	</a>
            </li>
            <li class = "<c:if test = "${menuOn eq 'point'}">on</c:if>">
            	<a href="javascript:goMembershipPage('point')" id="cat2">
            		포인트 안내
            	</a>
            </li>
            <li class = "<c:if test = "${menuOn eq 'membershipList'}">on</c:if>">
            	<a href="javascript:goMembershipPage('membershipList')" id="cat1">
            		멤버십 카드
            	</a>
            </li>
            <li><a href="javascript:goQnaPage('qnaWriteForm')" id="cat5">멤버십 문의</a></li>
        </ul>
    </aside>