<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>
    
    <script>
    
    	function goMemberView(id){
    		mem.t_gubun.value = "memberView";
    		mem.t_id.value = id;
    		
    		mem.method = "post";
    		mem.action = "Manager";
    		mem.submit();
    	}
    
    	function goSearch(){
    		search.t_gubun.value = "memberList";
    		search.method = "post";
    		search.action = "Manager";
    		search.submit();
    	}
    	
    	function goPage(page){
    		mem.t_gubun.value = "memberList";
    		mem.t_nowPage.value = page;
    		mem.method = "post";
    		mem.action = "Manager";
    		mem.submit();
    	}
    	
    	
    </script>
    
    <form name = "mem">
    	<input type = "hidden" name = "t_gubun">
    	<input type = "hidden" name = "t_id">
        <input type = "hidden" name = "t_nowPage">
    </form>
    
    
   <main>
        <!-- 회원 리스트 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>회원 리스트</h1>
                <p>회원 목록 및 회원 정보 수정</p>
            </div>
            
            <div class="m-search-area">
            	<form name = "search">
           		<input type = "hidden" name ="t_gubun">
                <select name = "t_listCount">
                    <option value="5" <c:if test="${t_listCount eq '5'}">selected</c:if>>5명씩</option>
                    <option value="10" <c:if test="${t_listCount eq '10'}">selected</c:if>>10명씩</option>
                    <option value="20" <c:if test="${t_listCount eq '20'}">selected</c:if>>20명씩</option>
                </select>
                <select name = "t_select">
                    <option value="id" <c:if test="${t_select eq 'id'}">selected</c:if>>아이디</option>
                    <option value="email_1" <c:if test="${t_select eq 'email_1'}">selected</c:if>>이메일</option>
                    <option value="name" <c:if test="${t_select eq 'name'}">selected</c:if>>성명</option>
                </select>
                <select name = "t_membership">
                    <option value="all" <c:if test="${t_membership eq 'all'}">selected</c:if>>등급</option>
                    <option value="silver" <c:if test="${t_membership eq 'silver'}">selected</c:if>>silver</option>
                    <option value="gold" <c:if test="${t_membership eq 'gold'}">selected</c:if>>gold</option>
                    <option value="premium" <c:if test="${t_membership eq 'premium'}">selected</c:if>>premium</option>
                </select>
                <input type="text" name = "t_search" value = "${t_search}" placeholder="검색어를 입력하세요">
                <button type = "button" onclick="goSearch()">검색</button>
				</form>
            </div>
			
            <div class = "list-area">
            	<table>
            		<colgroup>
						<col width = "5%">            		
						<col width = "15%">            		
						<col width = "*">            		
						<col width = "26%">            		
						<col width = "8%">            		
						<col width = "12%">            		
						<col width = "12%">            		
            		</colgroup>
            		<tr>
            			<th>No.</th>
            			<th>ID</th>
            			<th>성명</th>
            			<th>이메일</th>
            			<th>등급</th>
            			<th>가입일자</th>
            			<th>탈퇴일자</th>
            		</tr>
            		<c:set var = "num" value = "${dtos.size()}"/>
            		<c:forEach items = "${dtos}" var = "dto">
	            		<tr>
	            			<td>
	            				${num}
	            				<c:set var = "num" value = "${num -1}"/>
	            			</td>	
	            			<td>
	            				<a href = "javascript:goMemberView('${dto.getId()}')">
	            					${dto.getId()}
	            				</a>
	            			</td>	
	            			<td>
	            				<a href = "javascript:goMemberView('${dto.getId()}')">
		            				${dto.getFirst_name()} 
		            				${dto.getLast_name()}
	            				</a>
	            			</td>	
	            			<td>${dto.getEmail_1()}@${dto.getEmail_2()}
	            			</td>	
	            			<td>${dto.getMembership()}</td>	
	            			<td>${dto.getReg_date()}</td>	
	            			<td>${dto.getExit_date()}</td>	
	            		</tr>
            		</c:forEach>
            		<c:if test = "${empty dtos}">
            			<tr>
            				<th colspan = "7"><br>해당 정보의 회원은 존재하지 않습니다.<br>&nbsp;</th>
            			</tr>
            		</c:if>
            	</table>
            </div>
            
            <div class="pagination" id="pagination">
                ${displayPage}
            </div>
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>