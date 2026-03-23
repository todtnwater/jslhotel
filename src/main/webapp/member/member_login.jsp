<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>


	<script>
		
		function goPassword(){
			login.t_password.focus();
			return;
		}	
	
		// 웹사이트에 저장된 ID 있는지 체크
		function checkSaveId(){
			
			// 1. 웹사이트에 ID 값이 존재하는지 확인 
			const saveId = localStorage.getItem('saveId');
			
			// 2. ID 값이 존재할 경우 표시
			if(saveId){
				login.t_id.value = saveId;
				login.t_saveId.checked = true;
			}
		}
		
		window.onload = checkSaveId; // 저장된 ID값 불러오기
		
		function goLogin(){
			
			// 1. '아이디 저장' 체크박스 로직을 실행
			// 체크할 경우 'saveId'에 값 저장
			if(login.t_saveId.checked){ 
				localStorage.setItem('saveId', login.t_id.value);
			
			// 체크하지 않을 경우 'saveId' 값 제거
			} else { // 체크하지 않을 경우 'saveId' 값 제거
				localStorage.removeItem('saveId');
			}
			
			// 2. ID 입력창과 비밀번호 입력창이 빈칸이 아닐 시 로그인 진행
			if(login.t_id.value != "" && login.t_password.value != ""){
				login.t_gubun.value = "login";
				
				login.method = "post";
				login.action = "Member";
				login.submit();
			} else {
				alert("ID와 비밀번호를 확인해 주세요.");
				return;
			}
			
		}
		
	</script>


    <main>
        <!-- 로그인 화면 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>로그인</h1>
                <p>LUXURY 호텔에 어서오세요!</p>
            </div>
			<form name = "login">
				<input type = "hidden" name = "t_gubun">
	            <div class="login-area">
	            	<div class = "login-info">
	                	<input type="text" name = "t_id" placeholder="ID" autofocus onkeypress="if( event.keyCode==13 ){goPassword()}">
	              		<input type="password" name = "t_password" placeholder="PW" onkeypress="if(event.keyCode==13){goLogin()}">
	                </div>
	                <div class = "login-go">
	              		<button type = "button" onclick="goLogin()">로그인</button>
	                </div>
	                <div class = "id-check">
	                	<input type = "checkbox" name = "t_saveId" value = "on">
	                	&nbsp;
	                	<span>아이디 저장</span>
	                </div>
	                <div class = "id-search">
	                	<a href = "javascript:goMemberPage('searchIdForm')">▶ 로그인 되지 않을 경우</a>
	                </div>
	            </div>
            </form>
        </div>
    </main>

    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>