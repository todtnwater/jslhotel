<%-- m_check_list.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>
    
<link rel="stylesheet" href="style/check.css">
<style>
    /* 테이블 스타일 등 필요에 따라 추가 */
    .manager-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    .manager-table th, .manager-table td { border: 1px solid #ddd; padding: 8px; text-align: center; }
    .manager-table th { background-color: #f2f2f2; }
    .search-area { margin-bottom: 20px; display: flex; justify-content: flex-end; align-items: center; }
    .search-area select, .search-area input[type="text"] { padding: 8px; margin-left: 5px; border: 1px solid #ccc; border-radius: 4px; }
    .search-area button { padding: 8px 15px; margin-left: 5px; background-color: #4D869C; color: white; border: none; border-radius: 4px; cursor: pointer; }
</style>

<script>
    // 상세 조회 (ViewReservationDetail.java를 호출하는 로직)
    function goViewDetail(rvNo){
        workDetail.t_gubun.value = "viewDetail";
        workDetail.rvNo.value = rvNo;
        workDetail.method = "post";
        workDetail.action = "Check"; // 상세 조회는 Check 서블릿으로 이동
        workDetail.submit();
    }

    // ⭐ 관리자 검색 함수 (bookSearch)
    function goSearch(){
        const searchForm = document.forms['searchForm'];
        
        // 검색값이 없으면 전체 목록 조회 페이지로 이동
        if (searchForm.searchValue.value.trim() === '') {
            goBookList(); 
            return;
        }
        
        searchForm.t_gubun.value = "bookSearch"; // Manager.java의 bookSearch 로직으로
        searchForm.method = "post";
        searchForm.action = "Manager"; // Manager 서블릿으로
        searchForm.submit();
    }
    
    // 전체 목록으로 돌아가기/페이지 이동
    function goBookList(){
        workList.t_gubun.value = "bookList";
        workList.method = "post";
        workList.action = "Manager";
        workList.submit();
    }
    
</script>

<form name="workDetail">
    <input type="hidden" name="t_gubun">
    <input type="hidden" name="rvNo">
</form>

<form name="workList">
    <input type="hidden" name="t_gubun">
</form>

<form name="searchForm">
    <input type="hidden" name="t_gubun" value="bookSearch">
    
    <main>
        <div class="container">
            <h2 style="margin-bottom: 20px;">📋 관리자 예약 목록 (${fn:length(reservationList)}건)</h2>
            
            <div class="search-area">
                <select name="searchType">
                    <option value="rv_no" <c:if test="${searchType eq 'rv_no'}">selected</c:if>>예약번호</option>
                    <option value="rv_customer_name" <c:if test="${searchType eq 'rv_customer_name'}">selected</c:if>>예약자명</option>
                    <option value="rv_status" <c:if test="${searchType eq 'rv_status'}">selected</c:if>>예약상태</option>
                </select>
                <input type="text" name="searchValue" placeholder="검색어를 입력하세요" 
                       value="${searchValue}">
                <button type="button" onclick="goSearch()">🔍 검색</button>
                <button type="button" onclick="goBookList()" style="background-color: #6c757d;">전체 목록</button>
            </div>

            <div class="table-responsive">
                <table class="manager-table">
                    <thead>
                        <tr>
                            <th>NO.</th>
                            <th>예약번호</th>
                            <th>객실 타입</th>
                            <th>객실 번호</th>
                            <th>예약자명</th>
                            <th>체크인 날짜</th>
                            <th>총 결제 금액</th>
                            <th>상태</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="num" value="${fn:length(reservationList)}"/>
                        <c:forEach items="${reservationList}" var="rv">
                            <tr>
                                <td>
                                    ${num}
                                    <c:set var="num" value="${num - 1}"/>
                                </td>	
                                <td>
                                    <a href="javascript:goViewDetail('${rv.rv_no}')">
                                        ${rv.rv_no}
                                    </a>
                                </td>	
                                <td>${rv.rv_room_type}</td>	
                                <td>${rv.rv_room_no}</td>
                                <td>${rv.rv_customer_full_name}</td>	
                                <td>${rv.rv_check_in_date}</td>	
                                <td>
                                    <fmt:formatNumber value="${rv.rv_total_price}" pattern="#,###"/>원
                                </td>
                                <td>
                                    ${rv.rv_status}
                                </td>	
                            </tr>
                        </c:forEach>
                        <c:if test="${empty reservationList}">
                            <tr>
                                <td colspan="8"><br>검색 조건에 맞는 예약 내역이 존재하지 않습니다.<br>&nbsp;</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <div class="pagination" id="pagination">
                <%-- ${displayPage} --%>
            </div>
        </div>
    </main>
</form>

<footer>
    <%@ include file = "../common_footer.jsp" %>
</footer>
</body>
</html>