<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<link rel="stylesheet" href="style/board.css">
	<link rel="stylesheet" href="style/write.css">
	<link rel="stylesheet" href="style/qna.css">
</head>

<script type="text/javascript">

	function goToCategory(gubun) {
		noticeForm.t_gubun.value = gubun;
		noticeForm.method = "post";
		noticeForm.action = "Notice";
		noticeForm.submit();
	}
	
	function goQnaPage(gubun){
		qna.t_gubun.value = gubun;
		qna.method = "post";
		qna.action = "Qna";
		qna.submit();
	}
	
	function goFaqPage(gubun){
		qna.t_gubun.value = gubun;
		qna.method = "post";
		qna.action = "Faq";
		qna.submit();
	}
	
</script>

<form name="noticeForm">
	<input type="hidden" name="t_gubun">
</form>
<form name = "qna">
	<input type = "hidden" name = "t_gubun">
</form>


<aside>
    <h3>고객지원</h3>
    <ul id="snb">
        <li class="<c:if test = "${menuOn eq 'list' || menuOn eq 'writeForm' || menuOn eq 'view' || menuOn eq 'updateForm'}">on</c:if>">
        	<a href="javascript:goToCategory('list')" id="cat0">
       			공지사항
      	  	</a>
		</li>
        <li class="<c:if test = "${menuOn eq 'faqList'}">on</c:if>">
        	<a href="javascript:goFaqPage('faqList')" id="cat1">
        		자주 묻는 질문
        	</a>
        </li>
        <li class="<c:if test = "${menuOn eq 'qnaList'  || menuOn eq 'qnaView'}">on</c:if>">
        	<a href="javascript:goQnaPage('qnaList')" id="cat2">
        		Q&A
        	</a>
        </li>
        <li class = "<c:if test = "${menuOn eq 'qnaWriteForm'}">on</c:if>">
           <a href="javascript:goQnaPage('qnaWriteForm')" id="cat0">
               문의하기
           </a>
        </li>
    </ul>
</aside>