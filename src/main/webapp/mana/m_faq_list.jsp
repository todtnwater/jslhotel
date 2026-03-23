<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>


	<script>
	
	
		function goView(no){
			faq.t_gubun.value = "faqView";
			faq.t_no.value = no;
			
			faq.method = "post";
			faq.action = "Manager";
			faq.submit();
		}
	
		function goWriteForm(){
			faq.t_gubun.value = "faqWriteForm";
			faq.method = "post";
			faq.action = "Manager";
			faq.submit();
		}
		
	</script>
	
	<form name = "faq">
		<input type = "hidden" name = "t_gubun">
		<input type = "hidden" name = "t_no">
	</form>

    <main>
        <!-- FAQ 리스트 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>자주 묻는 질문 (FAQ)</h1>
                <p>고객님들이 자주 문의하시는 질문을 모았습니다.</p>
            </div>

	        <div class="faq-list">
				<c:forEach items="${dtos}" var = "dto">
					<div class="faq-item ${dto.getF_type()}" onclick="goView('${dto.getF_no()}')">
						<span class="q-post-badge ${dto.getF_type()}">
							<c:choose>
								<c:when test="${dto.getF_type() eq 'membership'}">멤버십</c:when>
								<c:when test="${dto.getF_type() eq 'point'}">포인트</c:when>
								<c:when test="${dto.getF_type() eq 'book'}">예약</c:when>
								<c:when test="${dto.getF_type() eq 'etc'}">기타</c:when>
							</c:choose>
							
						</span>
						<h3>${dto.getF_question()}</h3>
					</div>
				</c:forEach>
			</div>

        </div>
		
		<button class="faq-trigger" onclick="goWriteForm()" aria-label="FAQ 작성">+</button>
		
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>