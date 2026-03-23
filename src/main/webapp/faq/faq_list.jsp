<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_qna.jsp" %>


	<script>
	
		function openFaq(id) {
			document.getElementById(id).style.display = 'flex';
		}
		
		function closeFaq(id) {
			document.getElementById(id).style.display = 'none';
		}
		
	</script>

    <main>
        <!-- FAQ 리스트 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>자주 묻는 질문 (FAQ)</h1>
                <p>고객님들이 자주 문의하시는 질문을 모았습니다.</p>
            </div>

	        <div class="faq-list">
				<c:forEach items="${dtos}" var = "dto">
					<div class="faq-item ${dto.getF_type()}" onclick="openFaq('${dto.getF_no()}')">
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
        
		<!-- FAQ 팝업 -->
		<c:forEach items="${dtos}" var = "dto">
			<div id="${dto.getF_no()}" class="faq-modal">
				<div class="faq-modal-content">
					<span class="faq-modal-close" onclick="closeFaq('${dto.getF_no()}')">&times;</span>
					<h2>${dto.getF_question()}</h2>
					<p>${dto.getF_answer()}</p>
				</div>
			</div>
		</c:forEach>
		
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>