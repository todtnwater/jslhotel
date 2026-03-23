<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_notice.jsp" %>    
<script type="text/javascript">
	
	// 값 체크 함수 
	function checkValue(field, alertMsg) {
	    if (field.value.trim() === "" || field.value === null) {
	        alert(alertMsg);
	        field.focus();
	        return true; 
	    }
	    return false;
	}

	// 길이 체크 함수
	function checkLength(field, min, max, alertMsg) { 
	    if (field.value.length < min || field.value.length > max) {
	        alert(alertMsg);
	        field.focus();
	        return true;
	    }
	    return false;
	}

	// 팝업공지 선택 시 경고 및 설정 표시/숨김
	function changePostType(obj) {
	    var popupSettings = document.getElementById('popupSettings');
	    
	    if (obj.value === 'popup') {
	        if (confirm('⚠️ 팝업공지는 메인 페이지에 하나만 표시됩니다.\n\n기존 팝업 공지가 있다면 일반공지로 자동 변경됩니다.\n\n진행하시겠습니까?')) {
	            popupSettings.style.display = 'block';
	        } else {
	            obj.value = 'notice'; 
	            popupSettings.style.display = 'none';
	        }
	    } else {
	        popupSettings.style.display = 'none';
	    }
	}

	// 제목, 내용 글자 수 카운터
	function countTitle(obj) {
	    document.getElementById('titleCount').textContent = obj.value.length + '/100'; 
	}
	
	function countContent(obj) {
	    document.getElementById('contentCount').textContent = obj.value.length + '/800'; 
	}	
	
	// 파일 업로드 처리
	function handleFileSelect(obj) {
	    var fileList = document.getElementById('fileList');
	    fileList.innerHTML = '';
	    
	    var files = obj.files;
	    for (var i = 0; i < files.length; i++) {
	        var file = files[i];
	        // 파일 크기를 KB로 표시하고 소수점 1자리까지
	        var fileSize = (file.size / 1024).toFixed(1); 
	        
	        var fileItem = document.createElement('div');
	        fileItem.className = 'file-item';
	        fileItem.innerHTML = '<span class="file-name">' + file.name + '</span>' +
	                           '<span class="file-size">(' + fileSize + 'KB)</span>' +	                           
	                           '<button type="button" class="file-remove" onclick="this.parentElement.remove()">×</button>';
	        fileList.appendChild(fileItem);
	    }
	}
	
	
	function goSave() {
		// 제목 체크 (5자 이상 40자 이내)
		if(checkValue(notice.t_title,"제목을 입력하세요!")) return;		
		if(checkLength(notice.t_title, 5,100, "제목은 5자 이상 200자 이내로 입력해야 합니다!"))  return;
		
		// 내용 체크 (5자 이상 800자 이내)
		if(checkValue(notice.t_content, "내용을 입력하세요!")) return;
		if(checkLength(notice.t_content, 5,800, "내용은 5자 이상 800자 이내로 입력해야 합니다!"))  return;

        // 팝업공지인 경우 팝업 기간 필수 체크
        if (notice.t_type.value === 'popup') { 
            if (checkValue(notice.popupStartDate, "팝업 시작일을 입력하세요!")) return;
            if (checkValue(notice.popupEndDate, "팝업 종료일을 입력하세요!")) return;
            
            var startDate = new Date(notice.popupStartDate.value);
            var endDate = new Date(notice.popupEndDate.value);
            if (endDate < startDate) {
                alert("팝업 종료일은 시작일보다 이후여야 합니다!");
                notice.popupEndDate.focus();
                return;
            }    
        }		
        
        if (notice.t_type.value === 'popup') { 
			if(!confirm("기존 등록된 팝업공지가 존재할 경우 일반공지로 전환됩니다. 진행하시겠습니까?")){
				return;
			}
        }
        
		notice.t_gubun.value="save";
		notice.method="post";
		notice.action="Notice?t_gubun=save";
		notice.submit();
	}

    function goback() {
        history.back(); 
	}
 
</script>
	
    <main>
        <div class="write-container">
            <div class="write-header">
                <h1>글쓰기</h1>
                <p>새로운 게시글을 작성해주세요</p>
            </div>
            
            <form name="notice"  class="write-form" enctype="multipart/form-data"> 
            <input type="hidden" name="t_gubun" value="">
                <div class="form-row">
                    <div class="form-group">
                        <label for="postType">게시글 유형 <span class="required">*</span></label>
                        <select id="postType" name="t_type" onchange="changePostType(this)"> 
                            <option value="notice">일반 공지</option>
                            <option value="popup">팝업 공지</option>
                            <option value="urgent">긴급 공지</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="postAuthor">작성자</label>
                        <input type="text" id="postAuthor" name="t_position" value="${t_position}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="postStartDate">게시 시작일</label>
                        <input type="date" id="postStartDate" name="t_reg_date" value="${toDay}" readonly>
                    </div>
                </div>

                <div class="form-group full-width popup-settings" id="popupSettings" style="display: none;">
                    <strong>메인 페이지 팝업 설정</strong>
                    <div class="popup-options">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="popupStartDate">팝업 시작일 <span class="required">*</span></label>
                                <input type="date" id="popupStartDate" name="popupStartDate">
                            </div>
                            <div class="form-group">
                                <label for="popupEndDate">팝업 종료일 <span class="required">*</span></label>
                                <input type="date" id="popupEndDate" name="popupEndDate">
                            </div>
                        </div>
                        <p class="popup-info">ℹ️ "오늘 하루 보지 않기" 옵션이 자동으로 제공됩니다.</p>
                        <p class="popup-info">ℹ️ 팝업 게시판으로 등록시, 파일을 등록할수 없습니다.</p>
                    </div>
                </div>
                <div class="form-group full-width">
                    <label for="postTitle">제목 <span class="required">*</span></label>
                    <input type="text" id="postTitle" name="t_title" placeholder="제목을 입력하세요 (최대 100자)" maxlength="100" oninput="countTitle(this)" required>
                    <div class="char-counter" id="titleCount">0/100</div>
                </div>

                <div class="form-group full-width">
                    <label for="postContent">내용 <span class="required">*</span></label>
                    <textarea id="postContent" name="t_content" placeholder="내용을 입력하세요 (최대 800자)" maxlength="800" oninput="countContent(this)" rows="15" required></textarea>
                    <div class="char-counter" id="contentCount">0/800</div>
                </div>

				<div class="form-group full-width">
				    <label for="postFile">첨부파일</label>
				    <div class="file-upload-wrapper">
				        <input type="file" id="postFile" name="t_attach" multiple accept="*" onchange="handleFileSelect(this)"> 
				        <label for="postFile" class="file-upload-label">
				            <span class="file-text">파일을 선택하거나 드래그하세요</span>
				        </label>
				        <div class="file-list" id="fileList"></div>
				    </div>
				</div>
			</form>	
                <div class="form-actions">
                	<a href="javascript:goback()" class="btn-cancel">뒤로</a>
					<a href="javascript:goSave()" class="btn-submit">등록</a>
                </div>
           
        </div>
    </main>

    <footer>
		<%@ include file = "../common_footer.jsp" %>
    </footer>

   