<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>

	<script>
	
		function goList(){
			faq.t_gubun.value = "faqList";
			faq.method = "post";
			faq.action = "Manager";
			faq.submit();
		}	
		
		function goSave(){
			
			if(checkValue(faq.t_question, "질문 내용이 없습니다.")) return;
			if(checkValue(faq.t_answer, "답변 내용이 없습니다.")) return;
			
			faq.t_gubun.value = "faqSave";
			
			faq.method = "post";
			faq.action = "Manager";
			faq.submit();	
		}
		
	</script>

    <main>
        <div class="write-container minW800">
            <div class="write-header">
                <h1>자주 묻는 질문 (FAQ)</h1>
                <p>고객님들이 자주 문의하시는 질문을 모았습니다.</p>
            </div>

            <form class="write-form" name="faq">
                <div class="form-row">
                	<input type = "hidden" name = "t_gubun">
                	<input type = "hidden" name = "t_no">	
                    <div class="form-group">
                        <label for="postType">유형</label>
                        <select id="postType" name="t_type">
                            <option value="membership">멤버십</option>
                            <option value="point">포인트</option>
                            <option value="book">예약</option>
                            <option value="etc">기타</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="postAuthor">작성자 <span class="required">*</span></label>
                        <input type="text" id="postAuthor" name="t_reg_id" value="${sessionId}">
                    </div>

                    <div class="form-group">
                        <label for="postStartDate">작성일</label>
                        <input type="text" id="postRegDate" name="t_reg_date" value = "${toDay}">
                    </div>
                </div>

                <div class="form-group full-width">
                    <label for="postTitle">질문</label>
                    <input type="text" id="postTitle" name="t_question">
                </div>

                <div class="form-group full-width">
                    <label for="postContent">답변</label>
                    <textarea id="postContent" name="t_answer" rows="5"></textarea>
                </div>
				
                <div class="form-actions">
                	<button type="button" class="btn-cancel" onclick="goSave()">
                        <span>등 록</span>
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