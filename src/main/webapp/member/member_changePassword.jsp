<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>

	<script>
	
		//비밀번호 확인 문구
		//기존 자바스크립트 파일 다시 불러오기
		document.addEventListener('DOMContentLoaded', function(){
			const pw = document.getElementById('new_pw');
			const confirmPW = document.getElementById('new_pw_confirm');
			const confirmMsg = document.getElementById('confirm_msg');
			
			pw.addEventListener('input', checkPasswords);
			confirmPW.addEventListener('input', checkPasswords);
			
			function checkPasswords(){
				const pwValue = pw.value;
				const confirmPwValue = confirmPW.value;
				
				if(pwValue === '' && confirmPwValue === ''){
					confirmMsg.textContent = ''; // 둘 다 비어있으면 빈칸
					confirmMsg.className = ''; // 메시지 스타일 적용 X
					return;
				}
				
				if(pwValue === confirmPwValue){
					confirmMsg.textContent = '새 비밀번호가 일치합니다.';
					confirmMsg.className = 'match';
				} else {
					confirmMsg.textContent = '새 비밀번호가 일치하지 않습니다.';
					confirmMsg.className = 'mismatch';
				}
				
			}
		});
	
		// 기존 비밀번호 확인	
		function checkPassword(){
			
			if(checkValue(pw.t_new_password, "새 비밀번호를 입력해 주세요.")) return;
			if(checkValue(pw.t_new_password_confirm, "새 비밀번호 확인을 입력해 주세요.")) return;
			
			if(pw.t_new_password.value != pw.t_new_password_confirm.value){
				alert("새 비밀번호를 확인해 주세요.");
				return;
			} else {
				var id = pw.t_id.value;
				var password = pw.t_password.value;
				
				$.ajax({
					type : "POST", 
					url : "MemberCheckPw", 
					data : "t_id="+id+"&t_password="+password, 
					dataType : "text", 
					error : function(){
						alert("통신 실패!");	
					}, 
					success : function(data){
						var result = $.trim(data); 
						//alert(result);
						
						if(result == "true"){
							goChangePassword();
						} else {
							alert("비밀번호가 올바르지 않습니다.");
						}
						
					}
				});
			}
		}
		
		// 비밀번호 변경
		function goChangePassword(){
			pw.t_gubun.value = "changePassword";
			pw.method = "post";
			pw.action = "Member";
			pw.submit();
		}
		
	</script>


    <main>
        <!-- 비밀번호 변경 화면 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>비밀번호</h1>
                <p>비밀번호를 변경합니다.</p>
            </div>

            <div class="change-area">
            	<form name = "pw">
            	<input type = "hidden" name = "t_gubun">
            	<input type = "hidden" name = "t_id" value = "${dto.getId()}">
            	<div class = "change-info">
                	<input type="password" name = "t_password" placeholder="현재 비밀번호">
              		<input type="password" name = "t_new_password" id = "new_pw" placeholder="새 비밀번호">
              		<input type="password" name = "t_new_password_confirm" id = "new_pw_confirm" placeholder="새 비밀번호 확인">
                	<span id = "confirm_msg"></span>
                </div>
                </form>
                <div class = "change-go">
              		<button type ="button" onclick="checkPassword()">비밀번호 변경</button>
                </div>
            </div>
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>