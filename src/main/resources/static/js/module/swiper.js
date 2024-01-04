
const setSwiper = function () {
<<<<<<< HEAD
    console.log("스와이퍼 set");
=======
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
    const progressCircle = document.querySelector(".autoplay-progress svg");
    const progressContent = document.querySelector(".autoplay-progress span");
    var swiper = new Swiper(".memberSwiper", {
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: {
<<<<<<< HEAD
            delay: 5000,
=======
            delay: 3000,
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
            disableOnInteraction: false
        },
        pagination: {
            el: ".swiper-pagination",
            clickable: true
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev"
        },
        on: {
            autoplayTimeLeft(s, time, progress) {
<<<<<<< HEAD
                progressCircle.style.setProperty("--progress", 1 - progress);
                progressContent.textContent = `${Math.ceil(time / 1000)}s`;
=======
                // progressCircle.style.setProperty("--progress", 1 - progress);
                // progressContent.textContent = `${Math.ceil(time / 1000)}s`;
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
            }
        }
    });
}
const setSwiperWrapper = function(){
<<<<<<< HEAD
    console.log("스와이퍼래퍼 set");
=======
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
    const wrapper = document.querySelector('.swiper-wrapper');
    let slideNum = 4;
    let profilePerSlide = 4;
    let totalProfilNum = slideNum * profilePerSlide;
    let members = []; // 메인페이지 프로필 전광판에 표시될 사람들의 닉네임 배열

    for (var i = 0; i < totalProfilNum; i++) {
        members[i] = "doyun"+i;
    }

    // 슬라이더 세팅
    let html = "";
    for(let i = 0;i<slideNum;i++){
        html += `
            <div class="swiper-slide">
                <div class="row user">`;
        for(let j = 0;j<profilePerSlide;j++){
            html += createSwiperProfile(members[4*i+j+1]);
        }            
        html +=`
                </div>
            </div>`;
    }
    wrapper.innerHTML = html;
}
const createSwiperProfile = function (nickname) {
    let memNickname = nickname;
    let memImageName = "doyun_icon.png";
    let memLevel = 100;
    let memSymbolImageName = "starbucks.png";
    let memIntroduction = "안녕하세요";
    let membProfileHTML = `
        <div class="profile">
            <a href="/mypage/${memNickname}">
                <div class="profile-header">
                    <div class="profile-image"><img src="../img/${memImageName}" alt="NO-IMAGE"></div>
<<<<<<< HEAD
                    <div class="profile-info">
                        <p class="profile-level">Lv : ${memLevel}</p>
                        <p class="profile-nick">${memNickname}</p>
=======
                    <div class="profile-user_info">
                        <p class="profile-user_level">Lv : ${memLevel}</p>
                        <p class="profile-user_nick">${memNickname}</p>
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
                        <img class="badge" src="../img/${memSymbolImageName}" alt="NO-SYMBOL">
                    </div>
                </div>
            </a>
            <div class="profile-main">
                <div class="profile-introduction">
                    ${memIntroduction}
                </div>
                <div class="profile-badge">
                    뱃지 나열 공간
                </div>
            </div>
        </div>
    `
    return membProfileHTML;
}
