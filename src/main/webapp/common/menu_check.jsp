<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<link rel="stylesheet" href="style/check.css">
</head>

<script>
	function goCheckPage(gubun){
		work.t_gubun.value = gubun;
		work.method = "post";
		work.action = "Check";
		work.submit();
	}
	
	function goTemp(){
		alert('기능 구현 예정');
	}
	


</script>

<aside>
	<h3>예약 조회 | 관리</h3>
	<ul id="snb">
		<li class="<c:if test='${menuOn eq "checkPage"}'>on</c:if>">
			<a href="javascript:goCheckPage('checkPage')" id="cat0">
				예약 조회
			</a>
		</li>
		
		<c:choose>
			<c:when test="${not empty sessionId or not empty sessionScope.sessionId}">
				
				<li>
					<a href="javascript:goTemp()" id="cat2">
						예약 변경 요청
					</a>
				</li>
				
				<li>
					<a href="javascript:goTemp()" id="cat3">
						취소/환불 신청
					</a>
				</li>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
	</ul>
</aside>
