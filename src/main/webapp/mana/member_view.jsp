<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>

<script>

	//성, 이름 영문만 입력가능하게
	function nameUpper(event){
		const regExp = /[^a-zA-Z]/g;
		const ele = event.target;
		
		if(regExp.test(ele.value)){
			ele.value = ele.value.replace(regExp, '');
		} 
	}

	// 이름 영어 입력되었는지 확인
	function checkEnglish(obj){
		
		var result = false;
		const input = obj.value;
		
		const nonEnglishRegex = /[^a-zA-Z]/;
		if(nonEnglishRegex.test(input)){
			alert("영어로 입력해 주세요.");
			obj.select();
			result = true;
		}
		
		return result;
	}
	
	
	// 회원정보 수정
	function goUpdate(){
	
		if(checkValue(mem.t_first_name, "이름 입력!")) return;
		if(checkValue(mem.t_last_name, "이름 입력!")) return;
		if(checkValue(mem.t_email_1, "이메일 확인!")) return;
		if(checkValue(mem.t_email_2, "이메일 확인!")) return;
		
		if(!checkEnglish(mem.t_first_name) && !checkEnglish(mem.t_last_name)){
			mem.t_gubun.value = "memberUpdate";
			mem.method = "post";
			mem.action = "Manager";
			mem.submit();
		} else {
			alert("회원명 영어로 되어 있는지 확인하세요.");
			return;
		}
		
	}
	
	// 회원탈퇴
	function goStatus(status){
		
		mem.t_gubun.value = "memberStatus";
		mem.t_status.value = status;
		
		mem.method = "post";
		mem.action = "Manager";
		mem.submit();
		
	}
	
	
</script>


    <main>
        <!-- 회원 정보 화면 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>회원 정보 수정</h1>
                <p>관리자용</p>
            </div>

            <div class="join-area">
            	<form name = "mem">
            	<input type = "hidden" name = "t_gubun">
            	<input type = "hidden" name = "t_status">
            	<div class = "join-info">
            		<div class = "join-list">
            			<label>회원ID</label>
                		<input type="text" value = "${dto.getId()}" class="input_200w" disabled>
                		<input type = "hidden" name = "t_id" value = "${dto.getId()}">
              		</div>
              		<div class = "join-list">
              			<label>*회원명</label>
              			<input type="text" name = "t_first_name" class="input_150w upper" value = "${dto.getFirst_name()}" onkeyup="nameUpper(event)">
              			<input type="text" name = "t_last_name"  class="input_150w upper" value = "${dto.getLast_name()}" onkeyup="nameUpper(event)">
              			<span>*영어로 입력</span>
               		</div>
              		<div class = "join-list">
              			<label>*지역</label>
              			<select name = "t_area" class="select_150w">
              				<option value = "korea" <c:if test="${dto.getArea() eq 'korea'}">selected</c:if>>대한민국</option>
              				<option value = "asia" <c:if test="${dto.getArea() eq 'asia'}">selected</c:if>>Asia</option>
              				<option value = "europe" <c:if test="${dto.getArea() eq 'europe'}">selected</c:if>>Europe</option>
              				<option value = "africa" <c:if test="${dto.getArea() eq 'africa'}">selected</c:if>>Africa</option>
              				<option value = "north america" <c:if test="${dto.getArea() eq 'north america'}">selected</c:if>>North America</option>
              				<option value = "south america" <c:if test="${dto.getArea() eq 'south america'}">selected</c:if>>South America</option>
              				<option value = "oceania" <c:if test="${dto.getArea() eq 'oceania'}">selected</c:if>>Oceania</option>
              			</select>
               		</div>
              		<div class = "join-list">
              			<label>주소</label>
              			<input type="text" name = "t_address" value = "${dto.getAddress()}" class="input_450w">
               		</div>
              		<div class = "join-list">
              			<label>*이메일</label>
              			<input type="text" name = "t_email_1" value = "${dto.getEmail_1()}" class="input_150w">
              			@
              			<input type="text" name = "t_email_2" value = "${dto.getEmail_2()}" class="input_150w">
               		</div>
              		<div class = "join-list">
              			<label>휴대전화</label>
              			<input type="text" name = "t_mobile_1" value = "${dto.getMobile_1()}" class="input_70w">
              			-
              			<input type="text" name = "t_mobile_2" value = "${dto.getMobile_2()}" class="input_70w">
              			-
              			<input type="text" name = "t_mobile_3" value = "${dto.getMobile_3()}" class="input_70w">
               		</div>
              		<div class = "join-list">
              			<label>분류</label>
              			<input type="radio" value = "member" name = "t_rank" class = "radio" <c:if test="${dto.getRank() eq 'member'}">checked</c:if>>일반회원
              			<input type="radio" value = "super" name = "t_rank" class = "radio" <c:if test="${dto.getRank() eq 'super'}">checked</c:if>>관리자
               		</div>
              		<div class = "join-list">
              			<label>직책</label>
              			<input type="text" name = "t_position" value = "${dto.getPosition()}" class = "input_150w">
               		</div>
              		<div class = "join-list">
              			<label>성별</label>
              			<input type="radio" value = "m" name = "t_gender" class = "radio" <c:if test="${dto.getGender() eq 'm'}">checked</c:if>>남성
              			<input type="radio" value = "f" name = "t_gender" class = "radio" <c:if test="${dto.getGender() eq 'f'}">checked</c:if>>여성
               		</div>
              		<div class = "join-list">
              			<label>SNS 수신 여부</label>
              			<input type="radio" value = "y" name = "t_sns" class = "radio" <c:if test="${dto.getSns() eq 'y'}">checked</c:if>>예
              			<input type="radio" value = "n" name = "t_sns" class = "radio" <c:if test="${dto.getSns() eq 'n'}">checked</c:if>>아니오
               			<span>*LUXURY HOTEL의 소식, 할인 혜택 등의 정보를 수신합니다.</span>
               		</div>
               		<c:if test="${dto.getId() ne sessionId}">
               		<div class = "join-list">
              			<label>비밀번호 변경</label>
              			<input type="password" name = "t_password"  class="input_200w">
               		</div>
               		</c:if>
               		<div class = "join-list">
              			<label>회원가입일</label>
              			${dto.getReg_date()}
               		</div>
               		<div class = "join-list">
              			<label>정보수정일</label>
              			${dto.getUpdate_date()}
               		</div>
               		<div class = "join-list">
              			<label>회원탈퇴일</label>
              			${dto.getExit_date()}
               		</div>
                </div>
                </form>
                <div class = "join-button">
                	<c:if test="${empty dto.getExit_date() && dto.getId() ne sessionId}">
              			<button onclick="goStatus('exit')" class = "button_90w exit">탈퇴 처리</button>
              		</c:if>
              		<c:if test="${not empty dto.getExit_date()}">
              			<button onclick="goStatus('recovery')" class = "button_90w recovery">계정 복구</button>
              		</c:if>
              		<button onclick="goUpdate()" class = "button_90w">정보 수정</button>
                </div>
            </div>
        </div>
    </main>



    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>