const SwiperData = async function() {
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
    const progressCircle = document.querySelector(".autoplay-progress svg");
    const progressContent = document.querySelector(".autoplay-progress span");
    var swiper = new Swiper(".memberSwiper", {
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: {
            delay: 3000,
            disableOnInteraction: false
        },
        pagination: {
            el: ".swiper-pagination",
            clickable: true
        },
        // navigation: {
        //     nextEl: ".swiper-button-next",
        //     prevEl: ".swiper-button-prev"
        // },
        on: {
            autoplayTimeLeft(s, time, progress) {
                // progressCircle.style.setProperty("--progress", 1 - progress);
                // progressContent.textContent = `${Math.ceil(time / 1000)}s`;
            }
        }
    });
}
const setSwiperWrapper = async function(){
    let datalist = await SwiperData();
    let slideNum = 4;
    let profilePerSlide = 4;
    let html = "";
    for(let i = 0;i<slideNum;i++){
        html += `
            <div class="swiper-slide">
                <div class="row user">`;
        for(let j = 0;j<profilePerSlide;j++){
            html += createSwiperProfile(datalist[4*i+j].mem_nick, datalist[4*i+j].mem_lvl);
        }            
        html +=`
                </div>
            </div>`;
    }
    const wrapper = document.querySelector('.swiper-wrapper');
    wrapper.innerHTML = html;
}
const createSwiperProfile = function (nickname, level) {
    let memNickname = nickname;
    let memImageName = "doyun_icon.png";
    let memLevel = level;
    let memSymbolImageName = "starbucks.png";
    let memIntroduction = "안녕하세요";
    let membProfileHTML = `
        <div class="profile">
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
                <span class="badge">&#127774;</span>
                <span class="badge">&#127775;</span>
                <span class="badge">&#127806;</span>
                <span class="badge">&#127851;</span>
                <span class="badge">&#127774;</span>
                <span class="badge">&#127775;</span>
                <span class="badge">&#127806;</span>
                <span class="badge">&#127851;</span>
                <span class="badge">&#127774;</span>
                </div>
            </div>
        </div>
    `
    return membProfileHTML;
}

document.addEventListener("DOMContentLoaded", SwiperData());