<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_notice.jsp" %>    
<script type="text/javascript">
	function goUpdateForm() {
		notice.t_gubun.value="updateForm";
		notice.method="post";
		notice.action="Notice";
		notice.submit();			
	}
    function goback() {
        history.back(); 
	}
</script>
	
    <main>
        <div class="write-container">
        	<a href="Notice" style="text-decoration:none;">  
	            <div class="write-header">
	                <h1>공지사항</h1>
	                <p>호텔의 새로운 소식과 공지사항을 확인하세요</p>
	            </div>
            </a>
            
            <form name="notice"  class="write-form" > 
            <input type="hidden" name="t_gubun" value="">
            <input type="hidden" name="t_no" value="${dto.getN_no()}">
                
			<div class="form-row">
			    <div class="form-group">
			        <label for="postType">게시글 유형</label>
			        <input type="text" 
			               id="postType" 
			               name="t_type" 
			               value="<c:choose><c:when test="${dto.getN_type() eq 'urgent'}">긴급 공지</c:when><c:when test="${dto.getN_type() eq 'popup'}">팝업 공지</c:when><c:otherwise>일반 공지</c:otherwise></c:choose>" 
			               readonly
			               
			               class="${dto.getN_type()}"> 
			    </div>
			</div>
			
			<style>                
			    .urgent {
			        color: #dc3545; /* 긴급 공지 색상 */
			    }			
			    .popup {
			        color: #17a2b8; /* 팝업 공지 색상 */
			    }
			    
			    .urgent, .popup {
			        font-weight: bold;
			    }
			</style>
				
                <div class="form-row">
                    <div class="form-group">
                        <label for="postAuthor">작성자</label>
                        <input type="text" id="postAuthor" name="t_position" value="${dto.getN_position()}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="postStartDate">게시 시작일</label>
                        <input type="text" id="postStartDate" name="t_reg_date" value="${dto.getN_reg_date()}" readonly>
                    </div>
                </div>
                
                <div class="form-group full-width">
                    <label for="postTitle">제목</label>
                    <input type="text" id="postTitle" name="t_title" value="${dto.getN_title()}"readonly>
                </div>

<div class="form-group full-width">
    <label for="postContent">내용</label>
    <textarea id="postContent" name="t_content" readonly>${fn:replace(dto.getN_content(), '<br>', '
')}</textarea>
</div>
				
				
				<c:if test="${not empty dto.getN_attach()}">
					<div class="form-group full-width">
					    <label for="postFile">첨부파일</label>
						<div class="file-display-area">
							<a href="FileDownServlet?t_fileDir=notice&t_fileName=${dto.getN_attach()}">
								${dto.getN_attach()}
							</a>
						 </div>
					</div>					
				</c:if>
					
			
                <div class="form-actions">
                	<a href="javascript:goback()" class="btn-cancel">뒤로</a>
					<c:if test="${sessionLevel eq 'top'}">	                	
						<a href="javascript:goUpdateForm()" class="btn-submit">수정</a>
                	</c:if>
                </div>
           </form>	
        </div>
    </main>

    <footer>
		<%@ include file = "../common_footer.jsp" %>
    </footer>

   