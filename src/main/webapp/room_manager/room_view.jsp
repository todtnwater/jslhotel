<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_member.jsp" %>
<%@ include file = "../common/menu_manager.jsp" %>

<link rel="stylesheet" href="style/room_view.css">
<link rel="stylesheet" href="style/room_image.css">

<script src="js/room_image.js"></script>

<script>
	// 객실 목록으로 돌아가기
	function goRoomList(){
		work.t_gubun.value = "roomList";
		work.method = "post";
		work.action = "RoomManager";
		work.submit();
	}
	
	// 객실 정보 및 상태 수정 (통합)
	function goUpdate(){
		if(!confirm("객실 정보를 수정하시겠습니까?")) return;
		
		// 유효성 검사
		if(checkValue(room.t_extra_person_fee, "추가 인원 요금을 입력하세요!")) return;
		if(checkValue(room.t_price, "1박 요금을 입력하세요!")) return;
		if(checkValue(room.t_bed_type, "침대 타입을 선택하세요!")) return;
		if(checkValue(room.t_bed, "침대 수를 입력하세요!")) return;
		
		room.t_gubun.value = "roomUpdate";
		room.method = "post";
		room.action = "RoomManager";
		room.submit();
	}
	
	// 숫자만 입력
	function onlyNumber(obj){
		obj.value = obj.value.replace(/[^0-9]/g, '');
	}
</script>

<form name="room">
	<input type="hidden" name="t_gubun">
	<input type="hidden" name="t_room_no" value="${dto.r_room_no}">
	
	<main>
		<div class="join-area">
			<div class="join-info">
			<h3>객실 상세 정보</h3>
			
				<!-- ========== 객실 이미지 슬라이더 섹션 ========== -->
				<div class="room-image-slider-section">
					<h4>객실 사진</h4>
					<div class="image-cards-wrapper">
						<div class="image-cards-track" id="imageCardsTrack">
							<c:forEach var="img" items="${imageList}" varStatus="status">
								<div class="image-card" onclick="openImageModal(${status.index})">
									<img src="${pageContext.request.contextPath}/image${img.ri_img_path}"
										 alt="객실 이미지 ${status.index + 1}">
									<c:if test="${img.ri_is_main == 'Y'}">
										<span class="main-badge">메인</span>
									</c:if>
								</div>
							</c:forEach>
							
							<!-- 관리자용 업로드 카드 -->
							<div class="image-card upload-card" onclick="uploadImage()">
								<div class="upload-icon">+</div>
								<p>이미지 추가</p>
							</div>
						</div>
					</div>
					
					<!-- 좌우 화살표 버튼 -->
					<c:if test="${not empty imageList && imageList.size() > 3}">
						<button type="button" class="slider-arrow slider-prev" onclick="slideCards(-1)">◀</button>
						<button type="button" class="slider-arrow slider-next" onclick="slideCards(1)">▶</button>
					</c:if>
				</div>
				
				<hr class="hr-gray">
			
				<!-- 객실 기본 정보 (수정 불가) -->
				<div class="room-detail-box">
					<div class="room-detail-row">
						<label>객실 번호</label>
						<div class="room-detail-content">
							<strong>${dto.r_room_no}</strong>
							<span class="ml-50">층: ${dto.r_floor}층</span>
						</div>
					</div>
					
					<div class="room-detail-row">
						<label>객실 타입</label>
						<div class="room-detail-content">
							<strong>${dto.r_type}</strong>
							<span class="ml-50">면적: ${dto.r_scale}</span>
						</div>
					</div>
					
					<div class="room-detail-row">
						<label>기준/최대 인원</label>
						<div class="room-detail-content">
							<strong>기준 ${dto.r_people_standard}명 / 최대 ${dto.r_people_max}명</strong>
						</div>
					</div>
				</div>
				
				<hr class="hr-yellow">
				
				<!-- 운영 정보 (수정 가능) - 노란색 배경 -->
				<div class="join-list bg-yellow">
					<label>1박 요금 <span class="required">*</span></label>
					<input type="text" name="t_price" value="${dto.r_price}" 
						   onkeyup="onlyNumber(this)" class="input_150w" placeholder="250000">
					<span class="text-red">원</span>
				</div>
				
				<div class="join-list bg-yellow">
					<label>추가 인원 요금 <span class="required">*</span></label>
					<input type="text" name="t_extra_person_fee" value="${dto.r_extra_person_fee}" 
						   onkeyup="onlyNumber(this)" class="input_150w" placeholder="50000">
					<span>원/인</span>
				</div>
				
				<div class="join-list bg-yellow">
					<label>침대 타입 <span class="required">*</span></label>
					<select name="t_bed_type" class="select_150w">
						<option value="S" <c:if test="${dto.r_bed_type eq 'S'}">selected</c:if>>Single (싱글)</option>
						<option value="D" <c:if test="${dto.r_bed_type eq 'D'}">selected</c:if>>Double (더블)</option>
						<option value="Q" <c:if test="${dto.r_bed_type eq 'Q'}">selected</c:if>>Queen (퀸)</option>
						<option value="K" <c:if test="${dto.r_bed_type eq 'K'}">selected</c:if>>King (킹)</option>
					</select>
					<span class="ml-30">침대 수:</span>
					<input type="text" name="t_bed" value="${dto.r_bed}" 
						   onkeyup="onlyNumber(this)" class="input_70w" placeholder="2">
					<span>개</span>
				</div>
				
				<div class="join-list bg-yellow">
					<label>흡연</label>
					<select name="t_smoking" class="select_150w">
						<option value="Y" <c:if test="${dto.r_smoking eq 'Y'}">selected</c:if>>가능</option>
						<option value="N" <c:if test="${dto.r_smoking eq 'N'}">selected</c:if>>불가</option>
					</select>
					<span class="ml-30">욕실:</span>
					<input type="text" name="t_bathroom" value="${dto.r_bathroom}" 
						   onkeyup="onlyNumber(this)" class="input_70w">
					<span>개</span>
				</div>
				
				<hr class="hr-gray">
				
				<!-- 편의시설 정보 -->
				<div class="join-list">
					<label>주방</label>
					<select name="t_kitchen" class="select_150w">
						<option value="Y" <c:if test="${dto.r_kitchen eq 'Y'}">selected</c:if>>있음</option>
						<option value="N" <c:if test="${dto.r_kitchen eq 'N'}">selected</c:if>>없음</option>
					</select>
					<span class="ml-30">WiFi:</span>
					<select name="t_wifi" class="select_150w">
						<option value="Y" <c:if test="${dto.r_wifi eq 'Y'}">selected</c:if>>있음</option>
						<option value="N" <c:if test="${dto.r_wifi eq 'N'}">selected</c:if>>없음</option>
					</select>
				</div>
				
				<div class="join-list">
					<label>스파</label>
					<select name="t_spa" class="select_150w">
						<option value="Y" <c:if test="${dto.r_spa eq 'Y'}">selected</c:if>>있음</option>
						<option value="N" <c:if test="${dto.r_spa eq 'N'}">selected</c:if>>없음</option>
					</select>
					<span class="ml-30">발코니:</span>
					<select name="t_balcony" class="select_150w">
						<option value="Y" <c:if test="${dto.r_balcony eq 'Y'}">selected</c:if>>있음</option>
						<option value="N" <c:if test="${dto.r_balcony eq 'N'}">selected</c:if>>없음</option>
					</select>
				</div>
				
				<div class="join-list">
					<label>뷰</label>
					<select name="t_view" class="select_200w">
						<option value="Ocean" <c:if test="${dto.r_view eq 'Ocean'}">selected</c:if>>Ocean View (오션뷰)</option>
						<option value="City" <c:if test="${dto.r_view eq 'City'}">selected</c:if>>City View (시티뷰)</option>
						<option value="Mountain" <c:if test="${dto.r_view eq 'Mountain'}">selected</c:if>>Mountain View (마운틴뷰)</option>
						<option value="Garden" <c:if test="${dto.r_view eq 'Garden'}">selected</c:if>>Garden View (가든뷰)</option>
					</select>
				</div>
				
				<hr class="hr-blue">
				
				<!-- 객실 상태 통합 - 파란색 배경 -->
				<div class="join-list bg-blue">
					<label>객실 상태</label>
					<select name="t_room_status" class="select_250w">
						<c:set var="currentStatus" value="${dto.r_status}_${dto.r_clean_status}" />
						
						<!-- 사용 가능 상태만 선택 가능 -->
						<option value="AVAILABLE_CLEAN" 
							<c:if test="${currentStatus eq 'AVAILABLE_CLEAN'}">selected</c:if>>
							 사용가능 - 청소완료
						</option>
						
						<!-- 정비중 상태들 -->
						<option value="MAINTENANCE_DIRTY" 
							<c:if test="${currentStatus eq 'MAINTENANCE_DIRTY'}">selected</c:if>>
							🔧 정비중 - 청소필요
						</option>
						<option value="MAINTENANCE_CLEANING" 
							<c:if test="${currentStatus eq 'MAINTENANCE_CLEANING'}">selected</c:if>>
							🔧 정비중 - 청소중
						</option>
						<option value="MAINTENANCE_CLEAN" 
							<c:if test="${currentStatus eq 'MAINTENANCE_CLEAN'}">selected</c:if>>
							🔧 정비중 - 청소완료
						</option>
						
						<!-- 투숙중 상태는 표시만 (선택 불가) -->
						<c:if test="${dto.r_status eq 'OCCUPIED'}">
							<option value="OCCUPIED_${dto.r_clean_status}" selected disabled>
								🚫 투숙중 - ${dto.r_clean_status eq 'CLEAN' ? '청소완료' : dto.r_clean_status eq 'DIRTY' ? '청소필요' : '청소중'} (변경불가)
							</option>
						</c:if>
					</select>
				</div>
				
				<div class="join-list">
					<label>등록일</label>
					<span class="text-gray">${dto.r_register_date}</span>
					<span class="ml-30">최종 수정일:</span>
					<span class="text-gray">${dto.r_last_update}</span>
				</div>
				
				<div class="join-list auto-height">
					<label>객실 설명</label>
					<textarea name="t_description">${dto.r_description}</textarea>
				</div>
				
				<div class="info-box">
					<strong>💡 안내사항</strong><br>
					• <span class="text-yellow">노란색 배경</span>: 수정 가능한 운영 정보 (요금, 침대, 흡연 등)<br>
					• <span class="text-blue">파란색 배경</span>: 객실 상태 선택<br>
					• <strong class="text-red">⚠️ 투숙중(OCCUPIED) 상태는 체크인 시스템에서만 변경됩니다!</strong><br>
					• 객실 기본 정보(번호, 타입, 면적, 인원)는 변경할 수 없습니다.<br>
					<br>
					<strong>📌 상태 변경 규칙</strong><br>
					- 사용가능 ↔ 정비중: 관리자가 자유롭게 변경 가능<br>
					- 투숙중 → 사용가능: 체크아웃 시 자동 변경<br>
					- 사용가능 → 투숙중: 체크인 시 자동 변경
				</div>
			</div>
			
			<div class="join-button">
				<button type="button" onclick="goUpdate()" class="button_90w btn-green">수정하기</button>
				<button type="button" onclick="goRoomList()" class="button_90w btn-gray">목록으로</button>
			</div>
		</div>
	</main>
</form>

<!-- ========== 이미지 확대 모달 ========== -->
<div id="imageModal" class="image-modal" onclick="closeModal(event)">
	<div class="modal-content" onclick="event.stopPropagation()">
		<button type="button" class="modal-close" onclick="closeImageModal()">✕</button>
		
		<button type="button" class="modal-prev" onclick="changeModalImage(-1)">◀</button>
		<button type="button" class="modal-next" onclick="changeModalImage(1)">▶</button>
		
		<img id="modalImage" src="" alt="확대 이미지">
		
		<div class="modal-info">
			<span id="modalImageNumber"></span>
			<button type="button" class="btn-set-main" onclick="setMainImage()">메인 이미지로 설정</button>
			<button type="button" class="btn-delete" onclick="deleteImage()">삭제</button>
		</div>
	</div>
</div>

<!-- 숨겨진 파일 input -->
<input type="file" id="imageFileInput" accept="image/*" multiple style="display:none">

<script>
	// 이미지 데이터 초기화
	const ROOM_NO = '${dto.r_room_no}';
	const images = [
		<c:forEach var="img" items="${imageList}" varStatus="status">
			{
				imgNo: '${img.ri_img_no}',
				path: '${pageContext.request.contextPath}/image${img.ri_img_path}',
				isMain: '${img.ri_is_main}'
			}<c:if test="${!status.last}">,</c:if>
		</c:forEach>
	];
	
	// 페이지 로드시 초기화
	window.onload = function() {
		initRoomImages(ROOM_NO, images);
	};
</script>

<footer>
	<%@ include file = "../common_footer.jsp" %>
</footer>

</body>
</html>
