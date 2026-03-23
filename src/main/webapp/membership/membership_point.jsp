<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>

<aside>
    <%@ include file = "../common/menu_membership.jsp" %>
</aside>

<main>
    <div class="membership-container">
        
        <div class="board-header">
            <h1>포인트 사용 안내</h1>
            <p>지금까지 쌓으신 포인트를 현금처럼 자유롭게 사용하세요.</p>
        </div>
        
        <div class="membership-main" style="max-width: 900px; margin: 0 auto; padding: 40px 0;">
            
            <div class="info-section primary-info">
                <h2 class="section-title">✨ 포인트 가치 및 기본 정보</h2>
                <ul class="info-list">
                    <li>포인트는 **1포인트당 1원**의 가치를 가지며, 현금과 동일하게 사용하실 수 있습니다.</li>
                    <li>적립된 포인트는 **100포인트 단위**로 바로 사용 가능합니다.</li>
                    <li>**결제 금액의 100%까지** 포인트로 결제할 수 있습니다.</li>
                </ul>
            </div>
            
            <hr>
            
            <div class="info-section usage-locations">
                <h2 class="section-title">📍 포인트 사용처 안내</h2>
                <p class="section-description">포인트는 [호텔 이름] 내 거의 모든 시설 및 서비스 이용 시 사용 가능합니다.</p>
                
                <table class="usage-table">
                    <thead>
                        <tr>
                            <th>사용 영역</th>
                            <th>상세 사용처</th>
                            <th>비고</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>**객실 예약**</td>
                            <td>공식 홈페이지 또는 프론트 데스크 예약</td>
                            <td>결제 금액의 100%까지 사용 가능</td>
                        </tr>
                        <tr>
                            <td>**식음료 (F&B)**</td>
                            <td>모든 레스토랑, 바, 북카페, 룸서비스</td>
                            <td>주류 포함, 100포인트 단위 사용</td>
                        </tr>
                        <tr>
                            <td>**부대 시설**</td>
                            <td>피트니스, 사우나, 수영장(풀), 기타 어뮤니티 이용료</td>
                            <td>입장료 및 이용료에 한함</td>
                        </tr>
                        <tr>
                            <td>**호텔 상품**</td>
                            <td>호텔 내 올리브O 상품권 구매</td>
                            <td>일부 이벤트성 상품은 제외될 수 있음</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            
            <hr>
            
            <div class="info-section policy-notes">
                <h2 class="section-title">⚠️ 유의사항 및 포인트 소멸 정책</h2>
                <ul class="info-list">
                    <li>포인트는 **최소 1,000 포인트**부터 사용 가능합니다.</li>
                    <li>타 할인/쿠폰과 중복 사용은 가능하지만, 포인트 **적립 대상에서 제외**되는 항목이 있을 수 있습니다.</li>
                    <li>포인트 사용 시 **남은 결제 금액**에 한하여 포인트 적립이 진행됩니다.</li>
                    <li>적립된 포인트는 최종 사용/적립일로부터 **3년**이 경과하면 자동으로 소멸됩니다.</li>
                    <li>자세한 잔여 포인트 및 소멸 예정 포인트는 **[마이 페이지]**에서 확인해 주세요.</li>
                </ul>
            </div>
            
        </div>
    </div>
</main>

<footer>
    <%@ include file = "../common_footer.jsp" %>
</footer>

</body>
</html>
