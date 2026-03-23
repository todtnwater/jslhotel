<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>

<style>
    /* 단계 표시 스타일 */
    .step-indicator {
        display: flex;
        justify-content: center;
        padding: 20px;
        margin-bottom: 20px;
    }
    
    .step-item {
        display: flex;
        align-items: center;
    }
    
    .step-number {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: #ddd;
        color: #666;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
        font-size: 18px;
    }
    
    .step-item.active .step-number {
        background: #007bff;
        color: white;
    }
    
    .step-item.completed .step-number {
        background: #28a745;
        color: white;
    }
    
    .step-label {
        margin-left: 10px;
        font-size: 14px;
        color: #666;
    }
    
    .step-item.active .step-label {
        color: #007bff;
        font-weight: bold;
    }
    
    .step-divider {
        width: 80px;
        height: 2px;
        background: #ddd;
        margin: 0 15px;
    }
    
    .step-item.completed + .step-divider {
        background: #28a745;
    }
    
    /* 단계별 컨텐츠 숨김/표시 */
    .step-content {
        display: none;
    }
    
    .step-content.active {
        display: block;
    }
</style>

<script>

    //비밀번호 확인 문구
    document.addEventListener('DOMContentLoaded', function(){
        const pw = document.getElementById('password');
        const confirmPW = document.getElementById('confirm_password');
        const confirmMsg = document.getElementById('confirm_msg');
        
        if(pw && confirmPW) {
            pw.addEventListener('input', checkPasswords);
            confirmPW.addEventListener('input', checkPasswords);
            
            function checkPasswords(){
                const pwValue = pw.value;
                const confirmPwValue = confirmPW.value;
                
                if(pwValue === '' && confirmPwValue === ''){
                    confirmMsg.textContent = '';
                    confirmMsg.className = '';
                    return;
                }
                
                if(pwValue === confirmPwValue){
                    confirmMsg.textContent = '비밀번호가 일치합니다.';
                    confirmMsg.className = 'match';
                } else {
                    confirmMsg.textContent = '비밀번호가 일치하지 않습니다.';
                    confirmMsg.className = 'mismatch';
                }
            }
        }
    });

    //성, 이름 영문만 입력가능하게
    function nameUpper(event){
        const regExp = /[^a-zA-Z]/g;
        const ele = event.target;
        
        if(regExp.test(ele.value)){
            ele.value = ele.value.replace(regExp, '');
        } 
    }
    
    // 약관 전체 동의 [전체동의 체크 시 약관 목록 체크]
    function chkAll(){
        let tf = terms.t_check_all.checked;
        
        terms.t_check_1.checked = tf;
        terms.t_check_2.checked = tf;
        terms.t_check_3.checked = tf;
        terms.t_check_4.checked = tf;
    }

    // 약관 목록 중 하나라도 체크 해제 시 전체 동의 해제
    function chkBox(){
        let chk_1 = terms.t_check_1.checked;
        let chk_2 = terms.t_check_2.checked;
        let chk_3 = terms.t_check_3.checked;
        let chk_4 = terms.t_check_4.checked;
        
        if(chk_1 == false || chk_2 == false || chk_3 == false || chk_4 == false){
            terms.t_check_all.checked = false;
        }
    }
    
    // STEP 1에서 STEP 2로 이동
    function goNextStep(){
        // 필수 약관 체크 확인 (1, 2, 3번만 필수)
        if(!terms.t_check_1.checked || !terms.t_check_2.checked || !terms.t_check_3.checked){
            alert('필수 약관에 모두 동의해주세요.');
            return;
        }
        
        // STEP 1 숨기고 STEP 2 보이기
        document.getElementById('step1-content').classList.remove('active');
        document.getElementById('step2-content').classList.add('active');
        
        // 단계 표시 변경
        document.getElementById('step1-indicator').classList.remove('active');
        document.getElementById('step1-indicator').classList.add('completed');
        document.getElementById('step2-indicator').classList.add('active');
        
        // 페이지 상단으로 스크롤
        window.scrollTo(0, 0);
    }
    
    // STEP 2에서 STEP 1로 돌아가기
    function goPrevStep(){
        // STEP 2 숨기고 STEP 1 보이기
        document.getElementById('step2-content').classList.remove('active');
        document.getElementById('step1-content').classList.add('active');
        
        // 단계 표시 변경
        document.getElementById('step2-indicator').classList.remove('active');
        document.getElementById('step1-indicator').classList.remove('completed');
        document.getElementById('step1-indicator').classList.add('active');
        
        // 페이지 상단으로 스크롤
        window.scrollTo(0, 0);
    }

    // 아이디 중복 체크
    function checkId(){
        
        if(checkValue(join.t_id, "ID를 입력해주세요.")) return;
        
        var id = join.t_id.value;
        
        $.ajax({
            type : "POST", 
            url : "MemberCheckId", 
            data : "t_id="+id, 
            dataType : "text", 
            error : function(){
                alert("통신 실패!");
            }, 
            success : function(data){
                var result = $.trim(data);
                join.t_id_result.value = result;
            }
        });
    }
    
    // 이름 영어 입력되었는지 확인
    function checkEnglish(obj){
        let result = false;
        const input = obj.value;
        
        const nonEnglishRegex = /[^a-zA-Z]/;
        if(nonEnglishRegex.test(input)){
            alert("영어로 입력해 주세요.");
            obj.select();
            result = true;
        }
        
        return result;
    }
    
    // 아이디 상자 데이터 입력 시 중복체크 결과 초기화
    function setEmpty(){
        join.t_id_result.value = "";
    }
    
    // 회원가입
    function goSave(){
        
        if(checkValue(join.t_id, "ID를 입력해주세요.")) return;
        if(checkValue(join.t_password, "패스워드를 입력해주세요.")) return;
        if(join.t_password.value != join.t_password_confirm.value){
            alert("패스워드를 확인해 주세요.");
            join.t_password_confirm.select();
            return;
        }
        if(checkValue(join.t_first_name, "이름을 입력해주세요.")) return;
        if(checkEnglish(join.t_first_name)) return;
        if(checkValue(join.t_last_name, "이름을 입력해주세요.")) return;
        if(checkEnglish(join.t_last_name)) return;
        if(checkValue(join.t_email_1, "이메일을 입력해주세요.")) return;
        if(checkValue(join.t_email_2, "이메일을 입력해주세요.")) return;
    
        if(join.t_id_result.value != "사용가능") {
            alert("ID 중복체크를 진행해주세요.");
            return;
        }
    
        join.t_gubun.value = "save";
        join.method = "post";
        join.action = "Member";
        join.submit();
    }
    
    function goReset(){
        if(confirm("정말로 초기화하시겠습니까?")){
            join.reset();
            join.t_id.focus();
            return;
        }
    }

</script>

<main>
    <!-- 회원가입 화면 -->
    <div class="board-container" id="listSection">
        <div class="board-header">
            <h1>회원가입</h1>
            <p>LUXURY 호텔에 어서오세요!</p>
        </div>
        
        <!-- 단계 표시 인디케이터 -->
        <div class="step-indicator">
            <div class="step-item active" id="step1-indicator">
                <div class="step-number">1</div>
                <div class="step-label">이용약관 동의</div>
            </div>
            <div class="step-divider"></div>
            <div class="step-item" id="step2-indicator">
                <div class="step-number">2</div>
                <div class="step-label">회원정보 입력</div>
            </div>
        </div>

        <div class="join-area">
            <!-- ========== STEP 1: 이용약관 동의 ========== -->
            <div class="step-content active" id="step1-content">
                <form name="terms">
                <div class="join-info">
                    <h3>1. 이용약관 동의</h3>
                    <p>※ 개인정보 수집・이용에 관한 사항 (필수)</p>
                    <div class="join-term">
                        <textarea readonly>
1. 수집 이용 항목

아이디, 비밀번호, 성명, 성별, 연락처(휴대전화), 주소, 이메일

2. 수집 이용 목적

본인 확인 및 멤버십 회원 서비스 제공

3. 보유 이용 기간

회원 탈퇴 시 까지

※ 개인정보 보호법 제15조 제1항 제4호(계약이행)
                        </textarea>
                        <input type="checkbox" name="t_check_1" onclick="chkBox()" value="y"> 위 약관에 동의합니다. 
                    </div>
                    <div class="join-term">
                        <p>※ Luxury Hotel 이용 약관에 대한 동의 (필수)</p>
                        <textarea readonly>
제1조 회원가입 및 계정 생성

1. 회원가입은 홈페이지 또는 가맹호텔의 지정된 영업소에서 가능합니다.

2. 회원가입은 무료입니다.

3. 멤버십 중복 가입은 불가하며 하나를 초과하는 멤버십을 가입하거나 소유할 수 없습니다.

4. 회원 등록 시 아이디, 비밀번호, 성별, 지역, 이메일은 필수 기입 사항입니다.

5. 회원은 홈페이지 마이페이지에서 서비스 이용 내역 및 개인정보, 적립내역 등을 열람하거나 수정 가능합니다.


제2조 회원 자격정지 및 탈퇴

1. 최종 이용일이 속한 월의 말일 기준으로 3년간 실적이 없는 회원의 계정은 소멸되며, 기존 포인트 및 거래 실적도 자동 소멸됩니다. 

2. 계정 소멸 예정 회원에게는 소멸 6개월 전에 해당 내용을 이메일로 전달합니다.

3. 멤버십 재등록 시에는 신규회원으로 등록됩니다.

4. 회원 탈퇴는 탈퇴 신청 후 영업일 기준 24시간 이내 처리되며, 탈퇴 시 회원이 소유한 계정 및 포인트, 혜택 등은 자동 소멸됩니다.

5. 회원 탈퇴 시 기존 멤버십 카드는 사용할 수 없습니다.

6. 사망한 회원의 포인트 및 등급은 자녀에게 상속 가능하며, 사망 이후 6개월간 신청이 없을 경우에는 자동 소멸됩니다.  


제3조 멤버십 등급 및 특전

1. 회원 등급은 실버, 골드, 플레티넘으로 총 3단계로 분류됩니다.

2. 회원 가입과 동시에 실버 등급의 혜택이 부과됩니다. 

3. 시즌권 구매를 통해 골드, 플레티넘 등급으로 승급이 가능합니다.          			
                        </textarea>
                        <input type="checkbox" name="t_check_2" onclick="chkBox()" value="y"> 위 약관에 동의합니다.
                    </div>	
                    <div class="join-term">
                        <p>※ 개인정보 제3자 제공에 대한 동의 (필수)</p>
                        <textarea readonly>
1. 제공받는 자

JSL그룹

2. 제공받는 자의 이용 목적

JSL 그룹 내 서비스 제공

3. 제공하는 항목

아이디, 비밀번호, 성명, 성별, 연락처(휴대전화), 주소, 이메일

4. 제공받은 자의 보유·이용 기간

회원 탈퇴 시까지

※위 사항에 대한 동의를 거부할 수 있으나, 이에 대한 동의가 없을 경우 회원 가입 및 서비스 이용이 불가합니다.
                        </textarea>
                        <input type="checkbox" name="t_check_3" onclick="chkBox()" value="y"> 위 약관에 동의합니다.
                    </div>
                    <div class="join-term">
                        <p>※ 개인정보 마케팅 활용 동의 (선택)</p>
                        <textarea readonly>
1. 수집, 이용 항목

아이디, 비밀번호, 성명, 성별, 연락처(휴대전화), 주소, 이메일

2. 수집·이용 목적

회사 상품 및 서비스 소개, 기타 제휴 호텔의 상품 및 서비스 소개, 사은·판촉행사 안내, 만족도 조사, 시장 조사

3. 보유·이용 기간

회원탈퇴 시 또는 동의 철회 시까지

※ 위 사항에 대한 동의를 거부할 수 있으나, 동의가 없을 경우 개인형 맞춤 상품 안내 등 유용한 정보 안내를 받아보실 수 없습니다.         			
                        </textarea>
                        <input type="checkbox" name="t_check_4" onclick="chkBox()" value="y"> 위 약관에 동의합니다.
                    </div>
                    <div>
                        <input type="checkbox" name="t_check_all" onclick="chkAll()"> LUXURY HOTEL 회원가입 전체 약관에 동의합니다.
                    </div>
                </div>
                </form>
                <div class="join-button">
                    <button onclick="goNextStep()" class="button_90w">다 음</button>
                </div>
            </div>
            
            <!-- ========== STEP 2: 회원정보 입력 ========== -->
            <div class="step-content" id="step2-content">
                <form name="join">
                <input type="hidden" name="t_gubun">
                <div class="join-info">
                    <h3>2. 회원정보 입력</h3>
                    <div class="join-list">
                        <label>*ID</label>
                        <input type="text" name="t_id" class="input_200w" oninput="setEmpty()" autofocus>
                        <button type="button" onclick="checkId()" class="button_check">중복체크</button>
                        <input type="text" name="t_id_result" class="id_result" disabled>
                    </div>
                    <div class="join-list">
                        <label>*패스워드</label>
                        <input type="password" id="password" name="t_password" class="input_200w">
                    </div>
                    <div class="join-list">
                        <label>*패스워드 확인</label>
                        <input type="password" id="confirm_password" name="t_password_confirm" class="input_200w">
                        <span id="confirm_msg"></span>
                    </div>
                    <div class="join-list">
                        <label>*성함</label>
                        <input type="text" name="t_first_name" class="input_150w upper" placeholder="FIRST NAME" onkeyup="nameUpper(event)">
                        <input type="text" name="t_last_name" class="input_150w upper" placeholder="LAST NAME" onkeyup="nameUpper(event)">
                        <span>*영어로 입력</span>
                    </div>
                    <div class="join-list">
                        <label>*지역</label>
                        <select name="t_area" class="select_150w">
                            <option value="korea">대한민국</option>
                            <option value="asia">Asia</option>
                            <option value="europe">Europe</option>
                            <option value="africa">Africa</option>
                            <option value="north america">North America</option>
                            <option value="south america">South America</option>
                            <option value="oceania">Oceania</option>
                        </select>
                    </div>
                    <div class="join-list">
                        <label>주소</label>
                        <input type="text" name="t_address" class="input_450w">
                    </div>
                    <div class="join-list">
                        <label>*이메일</label>
                        <input type="text" name="t_email_1" class="input_150w">
                        @
                        <input type="text" name="t_email_2" class="input_150w" placeholder="naver.com">
                    </div>
                    <div class="join-list">
                        <label>휴대전화</label>
                        <input type="text" name="t_mobile_1" class="input_70w" maxlength="3">
                        -
                        <input type="text" name="t_mobile_2" class="input_70w" maxlength="4">
                        -
                        <input type="text" name="t_mobile_3" class="input_70w" maxlength="4">
                    </div>
                    <div class="join-list">
                        <label>성별</label>
                        <input type="radio" value="m" name="t_gender" class="radio" checked>남성
                        <input type="radio" value="f" name="t_gender" class="radio">여성
                    </div>
                    <div class="join-list">
                        <label>SNS 수신 여부</label>
                        <input type="radio" value="y" name="t_sns" class="radio">예
                        <input type="radio" value="n" name="t_sns" class="radio" checked>아니오
                        <span>*LUXURY HOTEL의 소식, 할인 혜택 등의 정보를 수신합니다.</span>
                    </div>
                </div>
                </form>
                <div class="join-button">
                    <!-- <button onclick="goPrevStep()" class="button_90w exit">이전으로</button> -->
                    <button onclick="goSave()" class="button_90w">회원가입</button>
                    <button onclick="goReset()" class="button_90w exit">초기화</button>
                </div>
            </div>
        </div>
    </div>
</main>

<footer>
    <%@ include file = "../common_footer.jsp" %>