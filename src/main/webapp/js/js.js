// DOM 로드 완료 후 실행
document.addEventListener('DOMContentLoaded', function() {
    initializePage();
});

// 페이지 초기화
function initializePage() {
    setMinDates();
    setupEventListeners();
}

// 오늘 날짜 설정
function setMinDates() {
    const today = new Date().toISOString().split('T')[0];
    const checkinInput = document.getElementById('checkin');
    const checkoutInput = document.getElementById('checkout');
    
    if (checkinInput) checkinInput.setAttribute('min', today);
    if (checkoutInput) checkoutInput.setAttribute('min', today);
}

// 이벤트 리스너 설정
function setupEventListeners() {
    // 체크인 날짜 변경 시 체크아웃 최소 날짜 업데이트
    const checkinInput = document.getElementById('checkin');
    if (checkinInput) {
        checkinInput.addEventListener('change', function() {
            const checkinDate = this.value;
            const checkoutInput = document.getElementById('checkout');
            if (checkoutInput) {
                checkoutInput.setAttribute('min', checkinDate);
                // 체크아웃 날짜가 체크인보다 빠르면 리셋
                if (checkoutInput.value && checkoutInput.value <= checkinDate) {
                    checkoutInput.value = '';
                }
            }
        });
    }

    // 상단 메뉴 클릭 이벤트
    setupMenuEvents();
    
    // 모바일 메뉴 이벤트
    setupMobileMenu();
    
    // 카테고리 버튼 이벤트
    setupCategoryEvents();
    
    // 부대시설 카드 클릭 이벤트
    setupFacilityCards();
    
    // 예약 버튼 이벤트
    setupBookingButton();

    // 키보드 접근성
    setupKeyboardNavigation();
}

// 상단 메뉴 이벤트 설정
function setupMenuEvents() {
    const menuLinks = document.querySelectorAll('.menu-link');
    const bannerSlides = document.querySelectorAll('.banner-slide');

    menuLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            // 활성 메뉴 변경
            menuLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
            
            // 배너 변경
            const menuType = this.getAttribute('data-menu');
            bannerSlides.forEach(slide => {
                slide.classList.remove('active');
                if(slide.getAttribute('data-banner') === menuType) {
                    slide.classList.add('active');
                }
            });
        });
    });
}

// 모바일 메뉴 설정
function setupMobileMenu() {
    const mobileToggle = document.querySelector('.mobile-menu-toggle');
    const mobileMenu = document.querySelector('.mobile-menu');
    const mobileLinks = document.querySelectorAll('.mobile-menu-link');
    const bannerSlides = document.querySelectorAll('.banner-slide');

    if (mobileToggle && mobileMenu) {
        mobileToggle.addEventListener('click', function() {
            mobileMenu.classList.toggle('active');
            this.textContent = mobileMenu.classList.contains('active') ? '✕' : '☰';
            this.setAttribute('aria-label', mobileMenu.classList.contains('active') ? '메뉴 닫기' : '메뉴 열기');
        });

        // 모바일 메뉴 링크 클릭 시
        mobileLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                
                // 데스크톱 메뉴 업데이트
                const menuLinks = document.querySelectorAll('.menu-link');
                const menuType = this.getAttribute('data-menu');
                
                menuLinks.forEach(l => l.classList.remove('active'));
                const targetMenu = document.querySelector(`.menu-link[data-menu="${menuType}"]`);
                if (targetMenu) targetMenu.classList.add('active');
                
                // 배너 변경
                bannerSlides.forEach(slide => {
                    slide.classList.remove('active');
                    if(slide.getAttribute('data-banner') === menuType) {
                        slide.classList.add('active');
                    }
                });
                
                // 모바일 메뉴 닫기
                mobileMenu.classList.remove('active');
                mobileToggle.textContent = '☰';
                mobileToggle.setAttribute('aria-label', '메뉴 열기');
            });
        });

        // 모바일 메뉴 외부 클릭 시 닫기
        document.addEventListener('click', function(e) {
            if (!mobileMenu.contains(e.target) && !mobileToggle.contains(e.target)) {
                mobileMenu.classList.remove('active');
                mobileToggle.textContent = '☰';
                mobileToggle.setAttribute('aria-label', '메뉴 열기');
            }
        });
    }
}

// 카테고리 버튼 이벤트 설정
function setupCategoryEvents() {
    const categoryBtns = document.querySelectorAll('.category-btn');

    categoryBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // 활성 버튼 변경
            categoryBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            
            // 모든 카테고리 컨테이너 숨기기
            document.querySelectorAll('.category-container').forEach(container => {
                container.classList.remove('show');
            });
            
            // 선택된 카테고리 컨테이너만 보이기
            const selectedCategory = this.getAttribute('data-category');
            const targetContainer = document.querySelector('.category' + selectedCategory);
            if (targetContainer) {
                // 약간의 지연을 주어 부드러운 전환 효과
                setTimeout(() => {
                    targetContainer.classList.add('show');
                }, 100);
            }
        });
    });
}

// 부대시설 카드 이벤트 설정
function setupFacilityCards() {
    const facilityCards = document.querySelectorAll('.facility-card');
    
    facilityCards.forEach(card => {
        card.addEventListener('click', function() {
            const facilityName = this.querySelector('h3').textContent;
            const facilityDesc = this.querySelector('p').textContent;
            showNotification(`${facilityName}\n\n${facilityDesc}`, 'info');
        });

        // 키보드 접근성
        card.setAttribute('tabindex', '0');
        card.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });
    });
}

// 예약 버튼 이벤트 설정
function setupBookingButton() {
    const searchBtn = document.querySelector('.search-btn');
    const loadingDiv = document.querySelector('.loading');
    
    if (searchBtn) {
        searchBtn.addEventListener('click', function() {
            const checkin = document.getElementById('checkin').value;
            const checkout = document.getElementById('checkout').value;
            const roomtype = document.getElementById('roomtype').value;
            const guests = document.getElementById('guests').value;
            
            // 입력 검증
            if (!checkin || !checkout) {
                showNotification('체크인/체크아웃 날짜를 선택해주세요!', 'error');
                return;
            }
            
            if (checkin >= checkout) {
                showNotification('체크아웃 날짜는 체크인 날짜보다 늦어야 합니다!', 'error');
                return;
            }
            
            if (!roomtype) {
                showNotification('객실 타입을 선택해주세요!', 'error');
                return;
            }
            
            if (!guests) {
                showNotification('투숙 인원을 선택해주세요!', 'error');
                return;
            }
            
            // 로딩 표시
            if (loadingDiv) {
                loadingDiv.classList.add('active');
            }
            
            // 가상의 예약 처리 (2초 후 완료)
            setTimeout(() => {
                if (loadingDiv) {
                    loadingDiv.classList.remove('active');
                }
                
                const roomNames = {
                    'standard': '스탠다드룸',
                    'deluxe': '디럭스룸',
                    'suite': '스위트룸',
                    'presidential': '프레지덴셜 스위트'
                };
                
                const message = `예약이 완료되었습니다!\n\n체크인: ${checkin}\n체크아웃: ${checkout}\n객실: ${roomNames[roomtype]}\n인원: ${guests}`;
                showNotification(message, 'success');
                
                // 폼 리셋
                document.getElementById('checkin').value = '';
                document.getElementById('checkout').value = '';
                document.getElementById('roomtype').value = '';
                document.getElementById('guests').value = '';
            }, 2000);
        });
    }
}

// 키보드 네비게이션 설정
function setupKeyboardNavigation() {
    // ESC 키로 모달 닫기
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            const qaModal = document.getElementById('qaModal');
            const mobileMenu = document.querySelector('.mobile-menu');
            const popupOverlay = document.getElementById('noticePopupOverlay');
            
            if (qaModal && qaModal.classList.contains('active')) {
                closeQA();
            }
            
            if (mobileMenu && mobileMenu.classList.contains('active')) {
                mobileMenu.classList.remove('active');
                const toggle = document.querySelector('.mobile-menu-toggle');
                if (toggle) {
                    toggle.textContent = '☰';
                    toggle.setAttribute('aria-label', '메뉴 열기');
                }
            }
            
            // 팝업 닫기 추가
            if (popupOverlay && popupOverlay.classList.contains('active')) {
                NoticePopupManager.close();
            }
        }
    });
}

// 알림 메시지 표시
function showNotification(message, type = 'info') {
    const notification = document.getElementById('notification');
    if (!notification) return;
    
    notification.textContent = message;
    notification.className = `notification ${type} show`;
    
    // 5초 후 자동 숨김
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

// Q&A 모달 열기
function openQA() {
    const qaModal = document.getElementById('qaModal');
    if (qaModal) {
        qaModal.classList.add('active');
        // 포커스 이동
        const closeBtn = qaModal.querySelector('.qa-close');
        if (closeBtn) closeBtn.focus();
    }
}

// Q&A 모달 닫기
function closeQA() {
    const qaModal = document.getElementById('qaModal');
    if (qaModal) {
        qaModal.classList.remove('active');
    }
}

// 모달 외부 클릭 시 닫기
document.addEventListener('click', function(e) {
    const qaModal = document.getElementById('qaModal');
    if (qaModal && e.target === qaModal) {
        closeQA();
    }
    
    // 팝업 오버레이 클릭 시 닫기
    const popupOverlay = document.getElementById('noticePopupOverlay');
    if (popupOverlay && e.target === popupOverlay) {
        NoticePopupManager.close();
    }
});

// =====================================================
// 공지사항 팝업 관련 코드 (정리된 버전)
// =====================================================

// 공지사항 팝업 매니저
const NoticePopupManager = {
    // 팝업 초기화
    init: function() {
        // DOM 로드 확인
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', this.showPopupIfExists.bind(this));
        } else {
            this.showPopupIfExists();
        }
    },
    
    // 팝업 존재 시 표시
    showPopupIfExists: function() {
        const popupOverlay = document.getElementById('noticePopupOverlay');
        if (!popupOverlay) return;
        
        // data 속성에서 공지 번호 가져오기
        const noticeNo = popupOverlay.getAttribute('data-notice-no');
        if (!noticeNo) return;
        
        // 쿠키 확인
        const hideToday = this.getCookie('noticePopup_' + noticeNo);
        
        // 쿠키가 없으면 팝업 표시
        if (hideToday !== 'done') {
            setTimeout(function() {
                popupOverlay.classList.add('active');
                console.log('팝업 공지 표시: ' + noticeNo);
            }, 500);
        }
    },
    
    // 팝업 닫기
    close: function() {
        const popupOverlay = document.getElementById('noticePopupOverlay');
        if (!popupOverlay) return;
        
        // 오늘 하루 보지 않기 체크 확인
        const checkbox = document.getElementById('todayCloseNotice');
        if (checkbox && checkbox.checked) {
            const noticeNo = popupOverlay.getAttribute('data-notice-no');
            if (noticeNo) {
                this.setCookie('noticePopup_' + noticeNo, 'done', 1);
                console.log('오늘 하루 보지 않기 설정됨');
            }
        }
        
        popupOverlay.classList.remove('active');
    },
    
    // 쿠키 설정
    setCookie: function(name, value, days) {
        let expires = "";
        if (days) {
            const date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "") + expires + "; path=/";
    },
    
    // 쿠키 가져오기
    getCookie: function(name) {
        const nameEQ = name + "=";
        const ca = document.cookie.split(';');
        for(let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) {
                return c.substring(nameEQ.length, c.length);
            }
        }
        return null;
    }
};

// 팝업 매니저 초기화
NoticePopupManager.init();

// 전역 함수로 등록 (onclick에서 사용)
window.closeNoticePopup = function() {
    NoticePopupManager.close();
};