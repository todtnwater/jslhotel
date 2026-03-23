<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>

	<script>
	
		// qna 폼은 menu_qna.jsp에 있음
		function goList(){
			qnaWork.t_gubun.value = "qnaList";
			
			qnaWork.method = "post";
			qnaWork.action = "Member";
			qnaWork.submit();
		}
		
		function goUpdate(){
			if(qnaWork.t_answer.value != ""){
				alert('답변이 작성되어 수정하실 수 없습니다. 자세한 사항은 관리자에게 문의 부탁드립니다.');
				return;
			}
			
			writeForm.t_gubun.value = "updateQuestion";
			writeForm.t_page.value = "member";
			
			writeForm.method = "post";
			writeForm.action = "Qna";
			writeForm.submit();
		}
		
		function goDelete(){
			
			if(qnaWork.t_level.value != 'top' && qnaWork.t_answer.value != ""){
				alert('답변이 작성되어 삭제하실 수 없습니다. 자세한 사항은 관리자에게 문의 부탁드립니다.');
				return;
			}
			
			if(confirm("정말로 삭제하시겠습니까?")){
				qnaWork.t_gubun.value = "delete";
				qnaWork.t_page.value = "manager";
				
				qnaWork.method = "post";
				qnaWork.action = "Qna";
				qnaWork.submit();
			}
			
		}
		
	</script>
	
	<form name = "qnaWork">
		<input type = "hidden" name = "t_page">
		<input type = "hidden" name = "t_answer" value = "${dto.getA_content()}">	
		<input type = "hidden" name = "t_level" value = "${sessionLevel}">	
		<input type = "hidden" name = "t_no" value = "${dto.getQ_no()}">	
		<input type = "hidden" name = "t_gubun">	
	</form>

    <main>
        <div class="write-container minW800">
            <div class="write-header">
                <h1>Q&A</h1>
                <p>궁금하신 사항을 말씀해 주세요.</p>
            </div>

            <form class="write-form" name="writeForm">
                <div class="form-row">
                	<input type = "hidden" name = "t_page">
                	<input type = "hidden" name = "t_gubun">
                	<input type = "hidden" name = "t_no" value = "${dto.getQ_no()}">	
                    <div class="form-group">
                        <label for="postType">유형</label>
                        <c:set value = "${dto.getQ_type()}" var = "type"/>
                        	<c:choose>
                        		<c:when test="${dto.getQ_type() eq 'membership'}">
                        			<c:set value = "${type = '멤버십'}" var = "type"/>
                        		</c:when>
                        		<c:when test="${dto.getQ_type() eq 'point'}">
                        			<c:set value = "${type = '포인트'}" var = "type"/>
                        		</c:when>
                        		<c:when test="${dto.getQ_type() eq 'book'}">
                        			<c:set value = "${type = '예약'}" var = "type"/>
                        		</c:when>
                        		<c:when test="${dto.getQ_type() eq 'etc'}">
                        			<c:set value = "${type = '기타'}" var = "type"/>
                        		</c:when>
                        	</c:choose>
                        <input type="text" id="postAuthor" name="t_type" value="${type}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="postAuthor">작성자 <span class="required">*</span></label>
                        <input type="text" id="postAuthor" name="t_reg_writer" value="${dto.getQ_reg_writer()}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="postStartDate">작성일</label>
                        <input type="text" id="postStartDate" name="t_reg_date" value = "${dto.getQ_reg_date()}" readonly>
                    </div>
                </div>

                <div class="form-group full-width">
                    <label for="postTitle">제목</label>
                    <input type="text" id="postTitle" name="t_title" value = "${dto.getQ_title()}">
                </div>

                <div class="form-group full-width">
                    <label for="postContent">내용</label>
                    <textarea id="postContent" name="t_content" rows="10">${dto.getQ_content()}</textarea>
                </div>
				
	
				<div class="form-group full-width">
                    <label for="postAdmin">답변</label>
                    <textarea id="postAdmin" name="t_answer" rows="5" readonly>${dto.getA_content()}</textarea>
                </div>
                <h4 style = "text-align:right;">
                	${dto.getA_position()} <c:if test="${not empty dto.getA_update_date()}"> | </c:if>${dto.getA_update_date()}
                </h4>
				
                <div class="form-actions">
                	<button type="button" class="btn-submit" onclick="goUpdate()">
                        <span>수 정</span>
                    </button>
                
                    <button type="button" class="btn-delete" onclick="goDelete()">
                        <span>삭 제</span>
                    </button>
                    
                    <button type="button" class="btn-submit" onclick="goList()">
                        <span>목 록</span>
                    </button>
                </div>
            </form>
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>

    <div class="notification" id="notification"></div>


</body>
</html>