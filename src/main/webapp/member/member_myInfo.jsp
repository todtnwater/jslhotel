<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>

<link rel="stylesheet" href="style/membership_card.css">

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
    
    // 비밀번호 확인 [이후 수정 및 탈퇴 진행]
    function checkPassword(obj){
        
        var code = obj;
        
        if(checkValue(mem.t_password, "비밀번호 입력해 주세요.")) return;
        
        var pw = mem.t_password.value;
        var id = mem.t_id.value;
        
        $.ajax({
            type : "POST", 
            url : "MemberCheckPw", 
            data : "t_password="+pw+"&t_id="+id, 
            dataType : "text", 
            error : function(){
                alert("통신 실패!");
            }, 
            success : function(data){
                var result = $.trim(data); // trim : 공백 제거
                //alert("---"+result+"---");
                mem.t_password_result.value = result;
                
                if(code == "update"){
                    goUpdate();
                } else if(code = "exit"){
                    goExit();
                }
            }
        });
    }
    
    // 회원정보 수정
    function goUpdate(){
        
        if(mem.t_password_result.value == "true"){
            mem.t_gubun.value = "update";
            mem.method = "post";
            mem.action = "Member";
            mem.submit();
        } else {
            alert("비밀번호가 올바르지 않습니다.");
            return;
        }
    }
    
    // 회원탈퇴
    function goExit(){
        
        if(mem.t_password_result.value == "true"){
            var str = prompt("다음 문구를 입력해 주세요. [회원탈퇴]");
            
            if(str == "회원탈퇴"){
                alert("회원탈퇴를 신청합니다.");
                mem.t_gubun.value = "exit";
                mem.action = "Member";
                mem.submit();
            } else {
                alert("문구가 올바르지 않습니다.");
                return;
            }    
        } else {
            alert("비밀번호가 올바르지 않습니다.");
            return;
        }
        
    }
    
    // ⭐ 카드 플립 함수
    function flipCard() {
        const cardFlip = document.querySelector('.card-flip');
        cardFlip.classList.toggle('flipped');
    }
    
</script>


    <main>
        <!-- 마이페이지 화면 -->
        <div class="board-container" id="listSection">
            <div class="board-header">
                <h1>마이페이지</h1>
                <p>회원정보</p>
            </div>

            <!-- ⭐ 멤버십 카드 + 포인트 (같은 박스) -->
            <div class="membership-card-box-unified">
                <h3>🎖️ 내 멤버십 & 포인트</h3>
                
                <div class="card-and-point-wrapper">
                    <!-- 왼쪽: 멤버십 카드 (플립 가능) -->
                    <div class="card-container">
                        <c:choose>
                            <c:when test="${sessionMembership eq 'bronze' || empty sessionMembership}">
                                <!-- 브론즈(비회원) -->
                                <div class="no-membership-message">
                                    <h4>아직 멤버십이 없습니다</h4>
                                    <p>멤버십을 구독하고<br>다양한 혜택을 받아보세요!</p>
                                    <a href="Membership?t_gubun=membershipList" class="upgrade-btn">
                                        멤버십 둘러보기
                                    </a>
                                </div>
                            </c:when>
                            
                            <c:otherwise>
                                <!-- 플립 카드 -->
                                <div class="card-flip" onclick="flipCard()">
                                    <!-- 앞면 -->
                                    <div class="card-front">
                                        <c:choose>
                                            <c:when test="${sessionMembership eq 'silver'}">
                                                <div class="membership-card-vertical silver-card">
                                                    <div class="card-content">
                                                        <div class="card-title">SILVER</div>
                                                        <div class="card-subtitle">${dto.getFirst_name()} ${dto.getLast_name()}</div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            
                                            <c:when test="${sessionMembership eq 'gold'}">
                                                <div class="membership-card-vertical gold-card">
                                                    <div class="card-content">
                                                        <div class="card-title">GOLD</div>
                                                        <div class="card-subtitle">${dto.getFirst_name()} ${dto.getLast_name()}</div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            
                                            <c:when test="${sessionMembership eq 'premium'}">
                                                <div class="membership-card-vertical premium-card">
                                                    <div class="card-content">
                                                        <div class="card-title">PREMIUM</div>
                                                        <div class="card-subtitle">${dto.getFirst_name()} ${dto.getLast_name()}</div>
                                                    </div>
                                                </div>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                    
                                    <!-- 뒷면 -->
                                    <div class="card-back">
                                        <div class="membership-card-vertical 
                                            <c:choose>
                                                <c:when test='${sessionMembership eq "silver"}'>silver-card</c:when>
                                                <c:when test='${sessionMembership eq "gold"}'>gold-card</c:when>
                                                <c:when test='${sessionMembership eq "premium"}'>premium-card</c:when>
                                            </c:choose>">
                                            <div class="card-back-content">
                                                <div class="card-info-row">
                                                    <span class="card-info-label">회원번호</span>
                                                    <span class="card-info-value">${membershipInfo.mb_no}</span>
                                                </div>
                                                <div class="card-info-row">
                                                    <span class="card-info-label">주문번호</span>
                                                    <span class="card-info-value">${membershipInfo.mb_order_id}</span>
                                                </div>
                                                <div class="card-info-row">
                                                    <span class="card-info-label">시작일</span>
                                                    <span class="card-info-value">${membershipInfo.mb_start_date}</span>
                                                </div>
                                                <div class="card-info-row">
                                                    <span class="card-info-label">종료일</span>
                                                    <span class="card-info-value">
                                                        <c:choose>
                                                            <c:when test="${empty membershipInfo.mb_end_date}">
                                                                평생회원
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${membershipInfo.mb_end_date}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </div>
                                                <div class="card-back-subtitle">LUXURY HOTEL</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- 오른쪽: 포인트 정보 -->
                    <div class="point-info-section">
                        <h4>💎 포인트 현황</h4>
                        <div class="point-grid">
                            <div class="point-item">
                                <div class="point-label">보유 포인트</div>
                                <div class="point-value">
                                    ${formattedPoint}
                                    <span class="point-unit">P</span>
                                </div>
                            </div>
                            <div class="point-item">
                                <div class="point-label">누적 포인트</div>
                                <div class="point-value">
                                    ${formattedTotalPoint}
                                    <span class="point-unit">P</span>
                                </div>
                            </div>
                        </div>
                        <div class="point-note">
                            💡 포인트는 객실 예약 시 사용 가능합니다 (1P = 1원)
                        </div>
                    </div>
                </div>
            </div>

            <div class="join-area">
                <form name = "mem">
                <input type = "hidden" name = "t_gubun">
                <div class = "join-info">
                    <div class = "join-list">
                        <label>*ID</label>
                        <input type="text" value = "${dto.getId()}" class="input_200w" disabled>
                        <input type = "hidden" name = "t_id" value = "${dto.getId()}">
                    </div>
                    <div class = "join-list">
                        <label>*성함</label>
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
                    <div class = "join-list">
                        <label>*비밀번호 입력</label>
                        <input type="password" name = "t_password"  class="input_200w">
                        <input type="hidden" name = "t_password_result">
                    </div>
                    <div class = "join-list">
                        <label>회원가입일</label>
                        ${dto.getReg_date()}
                    </div>
                    <div class = "join-list">
                        <label>정보수정일</label>
                        ${dto.getUpdate_date()}
                    </div>
                </div>
                </form>
                <div class = "join-button">
                    <button onclick="checkPassword('exit')" class = "button_90w exit">탈퇴 신청</button>
                    <button onclick="checkPassword('update')" class = "button_90w">정보 수정</button>
                </div>
            </div>
        </div>
    </main>



    <footer>
        <%@ include file = "../common_footer.jsp" %>
    </footer>
</body>
</html>
