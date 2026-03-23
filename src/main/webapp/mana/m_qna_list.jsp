<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>

	<script>
	
		function goView(no){

			view.t_gubun.value = "qnaView";
			view.t_no.value = no;
			
			view.method = "post";
			view.action = "Manager";
			view.submit();
		}
		
		function goSearch(){
			search.t_gubun.value = "qnaList";
			search.method = "post";
			search.action = "Manager";
			search.submit();
		}
		
    	function goPage(page){
    		search.t_gubun.value = "qnaList";
    		search.t_nowPage.value = page;
    		
    		search.method = "post";
    		search.action = "Manager";
    		search.submit();
    	}
	
	</script>

	<form name = "view">
		<input type = "hidden" name = "t_gubun">
		<input type = "hidden" name = "t_no">
	</form>

	<form name = "qnaPage">
		<input type = "hidden" name = "t_gubun">
	</form>

    <main>
        <!-- 문의사항 리스트 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>Q&A</h1>
                <p>궁금하신 사항을 말씀해 주세요.</p>
            </div>

			<div class="q-search-area">
            	<form name = "search">
           		<input type = "hidden" name ="t_gubun">
				<input type = "hidden" name = "t_nowPage">
                <select name = "t_order">
                    <option value="desc" <c:if test="${t_order eq 'desc'}">selected</c:if>>날짜순</option>
                	<option value="asc" <c:if test="${t_order eq 'asc'}">selected</c:if>>미답변순</option>
                </select>
                <select name = "t_listCount">
                    <option value="4" <c:if test="${t_listCount eq '4'}">selected</c:if>>4개씩</option>
                    <option value="8" <c:if test="${t_listCount eq '8'}">selected</c:if>>8개씩</option>
                </select>
                <select name = "t_select">
                    <option value="title" <c:if test="${t_select eq 'title'}">selected</c:if>>제목</option>
                    <option value="content" <c:if test="${t_select eq 'content'}">selected</c:if>>내용</option>
                    <option value="reg_writer" <c:if test="${t_select eq 'reg_writer'}">selected</c:if>>작성자</option>
                </select>
                <select name = "t_type">
                    <option value="all" <c:if test="${t_type eq 'all'}">selected</c:if>>유형</option>
                    <option value="membership" <c:if test="${t_type eq 'membership'}">selected</c:if>>멤버십</option>
                    <option value="book" <c:if test="${t_type eq 'book'}">selected</c:if>>예약</option>
                    <option value="etc" <c:if test="${t_type eq 'etc'}">selected</c:if>>기타</option>
                    <option value="point" <c:if test="${t_type eq 'point'}">selected</c:if>>포인트</option>
                </select>
                <input type="text" name = "t_search" value = "${t_search}" placeholder="검색어를 입력하세요" onkeypress="if(event.keyCode === 13){goSearch()}">
                <button type = "button" onclick="goSearch()">검색</button>
				</form>
            </div>

			<c:forEach items="${dtos}" var = "dto">
	            <div class="q-post-card ${dto.getQ_type()}" onclick="goView('${dto.getQ_no()}')">
	                <span class="q-post-badge ${dto.getQ_type()}">
	                	<c:choose>
	                		<c:when test="${dto.getQ_type() eq 'membership'}">멤버십</c:when>  
	                		<c:when test="${dto.getQ_type() eq 'book'}">예약</c:when>  
	                		<c:when test="${dto.getQ_type() eq 'point'}">포인트</c:when>  
	                		<c:when test="${dto.getQ_type() eq 'etc'}">기타</c:when>  
	                	</c:choose>
	                </span>
	                <c:if test="${not empty dto.getA_content()}">
		                <span class="q-post-badge answer">
		                	답변완료
		                </span>
	                </c:if>
	                <p class="q-post-preview">${dto.getQ_title()}</p>
	                <div class="q-post-meta">
	                    <span class="q-post-author">${dto.getQ_reg_writer()}</span>
	                    <div class="q-post-stats">
	                        <span>${dto.getQ_reg_date()}</span>
	                    </div>
	                </div>
	            </div>
            </c:forEach>

            <div class="q-pagination" id="pagination">
            	${displayPage}
            </div>
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>