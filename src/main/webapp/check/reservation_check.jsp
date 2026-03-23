<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_check.jsp" %>

<script>
    console.log('========== 세션 디버그 ==========');
    // 세션에서 직접 변수 접근
    console.log('sessionId: ${sessionScope.sessionId}');
    console.log('sessionName: ${sessionScope.sessionName}');
    // request scope 변수 (서블릿에서 설정했다면)
    console.log('request.sessionId: ${requestScope.sessionId}');
    console.log('request.sessionName: ${requestScope.sessionName}');
    console.log('================================');
</script>

<script type="text/javascript">
    function showTab(tabName) {
        document.getElementById('tab-number').style.display = 'none';
        document.getElementById('tab-member').style.display = 'none';
        
        document.getElementById('tab-' + tabName).style.display = 'block';
        
        var tabs = document.querySelectorAll('.tab-btn');
        tabs.forEach(function(tab) {
            tab.classList.remove('active');
        });
        if (tabName === 'number') {
            document.querySelector('.tab-btn:first-child').classList.add('active');
        } else {
            document.querySelector('.tab-btn:last-child').classList.add('active');
        }
    }
    
    window.onload = function() {
        showTab('number');
        <c:if test="${not empty error}">
            alert("⚠️ 오류: ${error}");
        </c:if>
    }
</script>

<link rel="stylesheet" href="style/check.css">

<div class="container">
    <div class="header">
        <h1>예약 조회</h1>
        <p>예약 번호 또는 회원 정보를 통해 예약을 확인하세요.</p>
    </div>
    
    <div class="tabs">
        <button class="tab-btn active" onclick="showTab('number')">🔢 예약번호로 조회</button>
        <button class="tab-btn" onclick="showTab('member')">👤 회원 조회</button>
    </div>
    
    <div class="search-box">
        <div id="tab-number" class="tab-content active">
            <form action="Check" method="post">
                <input type="hidden" name="t_gubun" value="checkReservation">
                <input type="hidden" name="searchType" value="number">
                
                <div class="form-group">
                    <label for="rvNo">예약번호</label>
                    <input type="text" id="rvNo" name="rvNo" placeholder="예: RV1762792288552" required>
                </div>
                
                <button type="submit" class="btn-search">🔍 예약 조회하기</button>
            </form>
            
            <p style="text-align: center; color: #666; font-size: 14px; margin-top: 20px; line-height: 1.6;">
                ℹ️ 예약 완료 시 발송된 이메일에서<br>
                예약번호를 확인하실 수 있습니다.
            </p>
        </div>
        
        <div id="tab-member" class="tab-content">
            <c:choose>
                <c:when test="${not empty sessionScope.sessionId}">
                    <form action="Check" method="post">
                        <input type="hidden" name="t_gubun" value="checkReservation">
                        <input type="hidden" name="searchType" value="sessionId">
                        
                        <div class="member-info">
                            <p>
                                <strong style="color: #4D869C; font-size: 18px;">
                                    ${sessionScope.sessionName}
                                </strong>님<br><br>
                                회원님의 모든 예약 내역을<br>
                                확인하실 수 있습니다.
                            </p>
                            
                            <button type="submit" class="btn-search">
                                👤 내 예약 조회하기
                            </button>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="member-info">
                        <p style="color: #F94C66; font-weight: 600; margin-bottom: 20px;">
                            ⚠️ 로그인이 필요한 서비스입니다.
                        </p>
                        <a href="javascript:goMemberPage('loginForm')" class="btn-search">🔑 로그인하러 가기</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>