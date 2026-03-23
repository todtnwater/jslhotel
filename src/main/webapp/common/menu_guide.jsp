<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<link rel="stylesheet" href="style/board.css">
	<link rel="stylesheet" href="style/write.css">
	<link rel="stylesheet" href="style/qna.css">
	<link rel="stylesheet" href="style/guide.css">
</head>

<script type="text/javascript">
function goToCategory(gubun) {
	guideForm.t_gubun.value = gubun;
	guideForm.method = "post";
	guideForm.action = "Guide";
	guideForm.submit();
}
</script>


<form name="guideForm">
	<input type="hidden" name="t_gubun">
</form>

<aside>
    <h3>호텔소개</h3>
    <ul id="snb">
        <li class="<c:if test = "${menuOn eq 'overview'}">on</c:if>">
        	<a href="javascript:goToCategory('overview')" id="cat0">
       			호텔 개요
      	  	</a>
		</li>
        <li class="<c:if test = "${menuOn eq 'road'}">on</c:if>">
        	<a href="javascript:goToCategory('road')" id="cat1">
        		오시는 길
        	</a>
        </li>
        <li class="<c:if test = "${menuOn eq 'room'}">on</c:if>">
        	<a href="javascript:goToCategory('room')" id="cat2">
        		객실 안내
        	</a>
        </li>
        <li class = "<c:if test = "${menuOn eq 'facility'}">on</c:if>">
           <a href="javascript:goToCategory('facility')" id="cat0">
               부속시설 안내
           </a>
        </li>
    </ul>
</aside>