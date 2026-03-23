<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<link rel="stylesheet" href="style/member.css">
	<link rel="stylesheet" href="style/qna.css">
	<link rel="stylesheet" href="style/write.css">
</head>

	<script>
		
		function goTemp(){
			alert('기능 구현 예정');
		}
	
	</script>

<aside>
<c:if test="${empty sessionName}">
    <h3>로그인 | 회원가입</h3>
    <ul id="snb">
	        <li class="<c:if test = "${menuOn eq 'loginForm'}">on</c:if>">
	        	<a href="javascript:goMemberPage('loginForm')" id="cat0">
	       			로그인
	      	  	</a>
			</li>
	        <li class="<c:if test = "${menuOn eq 'join'}">on</c:if>">
	        	<a href="javascript:goMemberPage('join')" id="cat1">
	        		회원가입
	        	</a>
	        </li>
	        <li class="<c:if test = "${menuOn eq 'searchIdForm'}">on</c:if>">
	        	<a href="javascript:goMemberPage('searchIdForm')" id="cat2">
	        		ID 찾기
	        	</a>
	        </li>
	        <li class="<c:if test = "${menuOn eq 'searchPwForm'}">on</c:if>">
	        	<a href="javascript:goMemberPage('searchPwForm')" id="cat2">
	        		비밀번호 찾기
	        	</a>
	        </li>
    </ul>
</c:if>
<c:if test="${not empty sessionName}">
	<c:if test="${sessionLevel ne 'top'}">
		<h3>마이페이지 | 이력</h3>
		    <ul id="snb">
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
		        <li class="<c:if test = "${menuOn eq 'qnaList' || menuOn eq 'qnaView'}">on</c:if>">
		        	<a href="javascript:goMemberPage('qnaList')" id="cat4">
		        		Q&A이력
		        	</a>
		        </li>    
	      	</ul>
      </c:if>	
</c:if>
</aside>