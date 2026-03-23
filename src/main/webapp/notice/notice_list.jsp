<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_notice.jsp" %>
<script>
	function goWriteForm() {
		noti.t_gubun.value="writeForm";
		noti.method="post";
		noti.action="Notice";
		noti.submit();
	}
	function goSearch(){
		notice.method="post";
		notice.action="Notice";
		notice.submit();
	}	
	function goPage(pageNumber){
		notice.t_nowPage.value = pageNumber;
		notice.method="post";
		notice.action="Notice";
		notice.submit();
	}	
	function goView(no) {
		noti.t_gubun.value="view";
		noti.t_no.value=no;
		noti.method="post";
		noti.action="Notice";
		noti.submit();
	}
</script>
<form name = "noti">
	<input type="hidden" name="t_no">
	<input type="hidden" name="t_gubun">
</form>
    <main>
        <!-- 게시판 리스트 -->
        <div class="board-container" id="listSection">                       
	         <a href="Notice" style="text-decoration:none;">  
	            <div class="board-header">
	                <h1>공지사항</h1>
	                <p>호텔의 새로운 소식과 공지사항을 확인하세요</p>		          
	            </div> 
	         </a>     
            <form name="notice"> 
            <input type="hidden" name="t_nowPage">         
	            <div class="search-area">
	                <select id="searchType" name="t_select">
	                    <option value="n_title"<c:if test="${t_select eq 'n_title'}">selected</c:if> >제목</option>
	                    <option value="n_content"<c:if test="${t_select eq 'n_content'}">selected</c:if> >내용</option>
	                </select>
	                <input type="text" id="searchInput" name="t_search" value="${t_search}" placeholder="검색어를 입력하세요">
	                <button onclick="goSearch()">검색</button>	
					
					<c:if test="${sessionLevel eq 'top'}">	
						<a href="javascript:goWriteForm()" class="write">공지사항 등록</a>
					</c:if>	  
	            </div>
            </form>
            
           
			<div class="post-list" id="postList">								   
			    <c:forEach var="dto" items="${t_dtos}">
			    	<a href="javascript:goView('${dto.getN_no()}')" style="text-decoration:none;">
			        <!-- 긴급 공지 -->	
			        <c:if test="${dto.getN_type() eq 'urgent'}">
			            <div class="post-card urgent priority-urgent">
			                <span class="post-badge urgent">긴급공지</span>
			                <h3>${dto.getN_title()}</h3>
			                <p class="post-preview">${dto.getN_content()}</p>
			                <div class="post-meta">
			                    <span class="post-author">${dto.getN_position()}</span>
			                    <div class="post-stats">
			                        <span>${dto.getN_reg_date()}</span>
			                        <span>조회 ${dto.getN_hit()}</span>
			                    </div>
			                </div>
			            </div>
			        </c:if>
			        
					<!-- 팝업 공지 -->
			        <c:if test="${dto.getN_type() eq 'popup'}">
			        <input type="hidden" value="${dto.getN_no()}">
			            <div class="post-card popup priority-high" onclick="viewPost()">
			                <span class="post-badge popup">팝업공지</span>
			                <h3>${dto.getN_title()}</h3>
			                <p class="post-preview">${dto.getN_content()}</p>
			                <div class="post-meta">
			                    <span class="post-author">${dto.getN_position()}</span>
			                    <div class="post-stats">
			                        <span>${dto.getN_reg_date()}</span>
			                        <span>조회 ${dto.getN_hit()}</span>
			                    </div>
			                </div>
			            </div>
			        </c:if>
		            <!-- 일반 공지 -->	
			        <c:if test="${dto.getN_type() eq 'notice'}">
			            <div class="post-card notice priority-normal" onclick="viewPost()">
			                <span class="post-badge notice">공지</span>
			                <h3>${dto.getN_title()}</h3>
			                <p class="post-preview">${dto.getN_content()}</p>
			                <div class="post-meta">
			                    <span class="post-author">${dto.getN_position()}</span>
			                    <div class="post-statst">
			                        <span>${dto.getN_reg_date()}</span>
			                        <span>조회 ${dto.getN_hit()}</span>
			                    </div>
			                </div>
			            </div>
			        </c:if>
			       </a> 
			    </c:forEach>
			</div>

            <div class="pagination" id="pagination">
				${t_pageDisplay}
            </div>
        </div>
    </main>

    <footer>
		<%@ include file = "../common_footer.jsp" %>	
    </footer>
</body>
</html>