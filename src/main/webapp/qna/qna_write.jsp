<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_qna.jsp" %>

	<script>
	
		function goSave(){
			
			
			if(checkValue(writeForm.t_title, "제목을 작성해 주세요.")) return;
			if(checkLength(writeForm.t_title, 5, 100, "제목")) return;
			if(checkValue(writeForm.t_content, "내용을 작성해 주세요.")) return;
			if(checkLength(writeForm.t_content, 5, 1000, "내용")) return;
			
			if(writeForm.t_id.value == ""){

				if(writeForm.t_reg_writer.value == ""){
					alert("작성자 이름을 입력해 주세요.");
					writeForm.t_reg_writer.focus();
					return;
				}
				
				if(writeForm.t_password.value == ""){
					alert("게시글 열람용 비밀번호를 입력해 주세요.");
					writeForm.t_password.focus();
					return;
				}
			}
			
			writeForm.t_gubun.value = "save";
			writeForm.method = "post";
			writeForm.action = "Qna";
			writeForm.submit();
		}
	
	</script>

    <main>
        <div class="write-container">
            <div class="write-header">
                <h1>Q&A</h1>
                <p>궁금하신 사항을 말씀해 주세요.</p>
            </div>

            <form class="write-form" name="writeForm">
                <div class="form-row">
                	<input type = "hidden" name = "t_gubun">
                    <div class="form-group">
                        <label for="postType">유형<span class="required">*</span></label>
                        <select id="postType" name="t_type" onchange="changePostType(this)" required>
                            <option value="membership">멤버십</option>
                            <option value="point">포인트</option>
                            <option value="book">룸 예약</option>
                            <option value="etc">기타</option>
                        </select>
                    </div>
                    
                    <input type = "hidden" name = "t_id" value = "${sessionId}">
                    <c:if test = "${empty sessionId}">
	                    <div class="form-group">
	                        <label for="postPassword">게시글 비밀번호 <span class="required">*</span></label>
	                        <input type="text" id="postPassword" name="t_password" placeholder = "열람용 비밀번호를 생성하세요.">
	                    </div>
                    </c:if>
                    
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="postAuthor">작성자 <span class="required">*</span></label>
                        <input type="text" id="postAuthor" name="t_reg_writer" value="<c:if test = "${not empty sessionId}">${sessionId}</c:if>" <c:if test = "${not empty sessionId}">readonly</c:if>>
                    </div>

                    <div class="form-group">
                        <label for="postStartDate">작성일</label>
                        <input type="text" id="postStartDate" name="t_reg_date" value = "${toDay}" readonly>
                    </div>
                </div>

                <div class="form-group full-width">
                    <label for="postTitle">제목 <span class="required">*</span></label>
                    <input type="text" id="postTitle" name="t_title" placeholder="제목을 입력하세요 (최대 100자)" maxlength="100" oninput="countTitle(this)" required>
                    <span class="char-count" id="titleCount">0/100</span>
                </div>

                <div class="form-group full-width">
                    <label for="postContent">내용 <span class="required">*</span></label>
                    <textarea id="postContent" name="t_content" placeholder="내용을 입력하세요" rows="15" required></textarea>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn-submit" onclick="goSave()">
                        <span>문의</span>
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