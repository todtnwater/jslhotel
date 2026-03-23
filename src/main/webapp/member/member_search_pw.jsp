<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>


	<script>
		
		function goFindPw(){

			var id = pw.t_id.value;
			var e1 = pw.t_email_1.value;
			var e2 = pw.t_email_2.value;
			
			$.ajax({
				type : "POST", 
				url : "MemberCheckIdEmail", 
				data : "&t_id="+id+"&t_email_1="+e1+"&t_email_2="+e2, 
				error : function(){
					alert("통신 실패!");
				}, 
				success : function(data){
					var result = $.trim(data);
					//alert(result);
					if(result == "true") {
						pw.t_gubun.value = "tempPassword";
						pw.method = "post";
						pw.action = "Member";
						pw.submit();
					} else {
						alert("회원정보가 올바르지 않습니다.");
						return;
					}
				}
				
			});
		}
		
	</script>


    <main>
        <!-- 비밀번호 찾기 화면 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>비밀번호 찾기</h1>
                <p>비밀번호 찾고자 하는 ID를 입력해 주세요.</p>
            </div>
            <div class="change-area">
            	<form name = "pw">
            	<input type = "hidden" name = "t_gubun">
                <div class = "search-info">
                	<input type="text" name = "t_id" placeholder="ID" class = "input_100p" autofocus onkeypress="if( event.keyCode==13 ){goPassword()}">
              		<input type="text" name = "t_email_1" class="input_145w">
           			@
           			<input type="text" name = "t_email_2" class="input_140w">
                </div>
                </form>    
                <div class = "change-go">
              		<button type ="button" onclick="goFindPw()">비밀번호 찾기</button>
                </div>
            </div>
            <div class = "search-sub">
	            <div class = "id-search">
	            	<span>아이디가 기억나지 않는다면?</span>
	            	<a href = "javascript:goMemberPage('searchIdForm')">아이디 찾기</a>
	            </div>
	        </div>    
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>