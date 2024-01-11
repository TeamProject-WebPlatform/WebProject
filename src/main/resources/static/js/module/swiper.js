document.addEventListener('DOMContentLoaded', function () {
    let profileSlidesPerView = 4;
    setSwiperWrapper(profileSlidesPerView);

    // 창 크기가 변경될 때 Swiper 업데이트
    window.addEventListener('resize', function () {
        setSwiperWrapper(profileSlidesPerView);
    });
});
const SwiperData = async function () {
    let datalist = [];
    try {
        const response = await fetch("/roll", {
            method: "POST"
        });
        const data = await response.json();
        for (let i = 0; i < data.length; i++) {
            datalist.push(data[i]);
        }
    } catch (error) {
        console.log("에러", error);
        throw error;  // 에러를 다시 던져서 호출하는 쪽에서 처리할 수 있도록 함
    }
    return datalist;
}
const setSwiper = function () {
    const profileSlider = new Swiper('.js-recommend .swiper', {
        slidesPerView: profileSlidesPerView,
        slidesPerGroup: profileSlidesPerView,
        spaceBetween: 40,
        loop: true,
        watchOverflow: true,
        observeParents: true,
        observeSlideChildren: true,
        observer: true,
        speed: 800,
        autoplay: {
            delay: 4000
        },
        navigation: {
            nextEl: '.js-recommend .swiper-button-next',
            prevEl: '.js-recommend .swiper-button-prev'
        },
        pagination: {
            el: '.js-recommend .swiper-pagination',
            type: 'bullets',
            // 'bullets', 'fraction', 'progressbar'
            clickable: true
        }
    });
}

{/* <div class="recommend-slide row user"> */ }
const setSwiperWrapper = async function () {
    let datalist = await SwiperData();
    let slideNum = datalist.length
    let html = "";
    for (let i = 0; i < slideNum; i++) {
        html += `
            <div class="swiper-slide">`;

        html += createSwiperProfile(datalist[i].mem_nick, datalist[i].mem_lvl);

        html += `   
            </div>`;
    }
    const wrapper = document.querySelectorAll('.swiper-wrapper')[0];
    wrapper.innerHTML = html;

    if (window.innerWidth >= 1600) {
        profileSlidesPerView = 4;
    } else if (window.innerWidth >= 1250) {
        profileSlidesPerView = 3;
    } else if (window.innerWidth >= 630) {
        profileSlidesPerView = 2;
    } else {
        profileSlidesPerView = 1;
    }
    // Swiper 초기화 및 업데이트
    setSwiper(profileSlidesPerView);
}
const createSwiperProfile = function (nickname, level) {
    let memNickname = nickname;
    let memImageName = "doyun_icon.png";
    let memLevel = level;
    let memSymbolImageName = "starbucks.png";
    let memIntroduction = "안녕하세요";
    let membProfileHTML = `
        <div class="profile-card">
            <a href="/mypage/${memNickname}">
                <div class="profile-header">
                    <div class="profile-image"><img src="../img/${memImageName}" alt="NO-IMAGE"></div>
                    <div class="profile-user_info">
                        <p class="profile-user_level">Lv : ${memLevel}</p>
                        <p class="profile-user_nick">${memNickname}</p>
                        <img class="badge" src="../img/${memSymbolImageName}" alt="NO-SYMBOL">
                    </div>
                </div>
            </a>
            <div class="profile-dividing-line"></div>
            <div class="profile-main">
                <div class="profile-introduction">
                    ${memIntroduction}
                </div>
                <div class="profile-dividing-line"></div>
                <div class="profile-badge-section">
                    <div class="badge-line">
                        <span class="badge">&#127774;</span>
                        <span class="badge">&#127775;</span>
                        <span class="badge">&#127806;</span>
                    </div>
                    <div class="badge-line">
                        <span class="badge">&#127851;</span>
                        <span class="badge">&#127774;</span>
                        <span class="badge">&#127775;</span>
                    </div>
                    <div class="badge-line">
                        <span class="badge">&#127806;</span>
                        <span class="badge">&#127851;</span>
                        <span class="badge">&#127774;</span>
                    </div>
                </div>
            </div>
        </div>
    `
    return membProfileHTML;
}

document.addEventListener("DOMContentLoaded", SwiperData());