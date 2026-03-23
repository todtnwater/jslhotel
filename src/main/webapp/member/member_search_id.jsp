<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>


	<script>
		
		//성, 이름 영문만 입력가능하게
		function nameUpper(event){
			const regExp = /[^a-zA-Z]/g;
			const ele = event.target;
			
			if(regExp.test(ele.value)){
				ele.value = ele.value.replace(regExp, '');
			} 
		}
		
		// ID 표시
		function goFind(){
			
			var fn = mem.t_first_name.value;
			var ln = mem.t_last_name.value;
			var e1 = mem.t_email_1.value;
			var e2 = mem.t_email_2.value;
			
			$.ajax({
				type : "POST", 
				url : "MemberFindId", 
				data : "&t_first_name="+fn+"&t_last_name="+ln+"&t_email_1="+e1+"&t_email_2="+e2, 
				error : function(){
					alert("통신 실패!");
				}, 
				success : function(data){
					var result = $.trim(data);
					alert(result);
				}
				
			});
			
		}
		
	</script>


    <main>
        <!-- ID 찾기 화면 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>ID 찾기</h1>
                <p>찾으시려는 ID의 정보를 입력해 주세요.</p>
	            <div class="change-area">
	            	<form name = "mem">
	            	<input type = "hidden" name = "t_gubun">
	            	<div class = "search-info">
	                	<input type="text" name = "t_first_name" class="input_152w upper" placeholder="FIRST NAME" onkeyup="nameUpper(event)">
	              		<input type="text" name = "t_last_name"  class="input_152w upper" placeholder="LAST NAME" onkeyup="nameUpper(event)">
	              		<input type="text" name = "t_email_1" class="input_145w">
	           			@
	           			<input type="text" name = "t_email_2" class="input_140w" onkeypress = "if(event.keyCode==13){goFind()}">
	                </div>
	                </form>
	                <div class = "change-go">
	              		<button type ="button" onclick="goFind()">ID 찾기</button>
	                </div>
	            </div>
        	</div>
        </div>	
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>