// ========== 전역 변수 ==========
let currentModalIndex = 0;
let roomImages = [];
let currentRoomNo = '';

// ========== 초기화 함수 ==========
function initRoomImages(roomNo, images) {
    currentRoomNo = roomNo;
    roomImages = images;
    
    // 파일 input 이벤트 연결
    document.getElementById('imageFileInput').addEventListener('change', handleFileSelect);
}

// ========== 카드 슬라이더 ==========
function slideCards(direction) {
    const wrapper = document.querySelector('.image-cards-wrapper');
    if (!wrapper) return;
    
    const cardWidth = 266; // 250px + 16px gap
    const scrollAmount = cardWidth * 3; // 한 번에 3개씩 이동
    
    wrapper.scrollBy({
        left: direction * scrollAmount,
        behavior: 'smooth'
    });
}

// ========== 이미지 업로드 ==========
function uploadImage() {
    document.getElementById('imageFileInput').click();
}

function handleFileSelect(event) {
    const files = event.target.files;
    
    if (files.length === 0) return;
    
    // 여러 파일 업로드
    for (let i = 0; i < files.length; i++) {
        uploadSingleImage(files[i]);
    }
    
    // input 초기화
    event.target.value = '';
}

function uploadSingleImage(file) {
    // 파일 크기 체크 (10MB)
    if (file.size > 10 * 1024 * 1024) {
        alert('파일 크기는 10MB를 초과할 수 없습니다.');
        return;
    }
    
    // 이미지 파일 체크
    if (!file.type.startsWith('image/')) {
        alert('이미지 파일만 업로드 가능합니다.');
        return;
    }
    
    const formData = new FormData();
    formData.append('roomImage', file);
    formData.append('roomNo', currentRoomNo);
    
    fetch('RoomManager?t_gubun=imageUpload', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        // 응답 텍스트를 먼저 가져옴
        return response.text().then(text => {
            console.log('서버 응답:', text); // 디버깅용
            
            // HTML이 아니라 JSON인지 확인
            if (text.startsWith('{')) {
                return JSON.parse(text);
            } else {
                // HTML 에러 페이지가 온 경우
                console.error('서버 에러 페이지:', text);
                throw new Error('서버에서 에러가 발생했습니다. 콘솔을 확인하세요.');
            }
        });
    })
    .then(data => {
        if (data.success) {
            alert('이미지 업로드 성공!');
            location.reload();
        } else {
            alert('업로드 실패: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('서버 오류: ' + error.message);
    });
}

// ========== 모달 열기 ==========
function openImageModal(index) {
    if (!roomImages || roomImages.length === 0) return;
    
    currentModalIndex = index;
    const modal = document.getElementById('imageModal');
    const modalImage = document.getElementById('modalImage');
    
    modalImage.src = roomImages[index].path;
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
    
    updateModalInfo();
}

// ========== 모달 닫기 ==========
function closeImageModal() {
    const modal = document.getElementById('imageModal');
    modal.classList.remove('active');
    document.body.style.overflow = 'auto';
}

// 모달 배경 클릭시 닫기
function closeModal(event) {
    if (event.target.id === 'imageModal') {
        closeImageModal();
    }
}

// ESC 키로 닫기
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeImageModal();
    }
});

// ========== 모달 이미지 변경 ==========
function changeModalImage(direction) {
    if (!roomImages || roomImages.length === 0) return;
    
    currentModalIndex += direction;
    
    // 순환
    if (currentModalIndex < 0) {
        currentModalIndex = roomImages.length - 1;
    } else if (currentModalIndex >= roomImages.length) {
        currentModalIndex = 0;
    }
    
    document.getElementById('modalImage').src = roomImages[currentModalIndex].path;
    updateModalInfo();
}

// ========== 모달 정보 업데이트 ==========
function updateModalInfo() {
    if (!roomImages || roomImages.length === 0) return;
    
    document.getElementById('modalImageNumber').textContent = 
        `${currentModalIndex + 1} / ${roomImages.length}`;
}

// ========== 메인 이미지 설정 ==========
function setMainImage() {
    if (!roomImages || roomImages.length === 0) return;
    
    if (!confirm('이 이미지를 메인 이미지로 설정하시겠습니까?')) return;
    
    const currentImg = roomImages[currentModalIndex];
    
    fetch('RoomManager?t_gubun=setMainImage', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `roomNo=${currentRoomNo}&imgNo=${currentImg.imgNo}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('메인 이미지로 설정되었습니다.');
            location.reload();
        } else {
            alert('설정 실패: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('서버 오류가 발생했습니다.');
    });
}

// ========== 이미지 삭제 ==========
function deleteImage() {
    if (!roomImages || roomImages.length === 0) return;
    
    if (!confirm('이 이미지를 삭제하시겠습니까?')) return;
    
    const currentImg = roomImages[currentModalIndex];
    
    fetch('RoomManager?t_gubun=imageDelete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `imgNo=${currentImg.imgNo}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('이미지가 삭제되었습니다.');
            location.reload();
        } else {
            alert('삭제 실패: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('서버 오류가 발생했습니다.');
    });
}