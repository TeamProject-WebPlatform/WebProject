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

const UserProfileData = async function (Swiper) {
    let datalist = [];
    try {
        const response = await fetch("/userprofile", {
            method: "POST",
            headers : {
                'Content-Type': 'application/json'
            },
            body:Swiper
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
    let userprofile = await UserProfileData(JSON.stringify(datalist));
    let slideNum = datalist.length
    let html = "";
    for (let i = 0; i < slideNum; i++) {
        html += `
            <div class="swiper-slide">`;

        html += createSwiperProfile(datalist[i].mem_lvl, datalist[i].mem_nick, userprofile[i].profile_intro, userprofile[i].profile_header, 
            userprofile[i].profile_card, userprofile[i].profile_rep_badge, userprofile[i].profile_image, userprofile[i].profile_badge_list);

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
const createSwiperProfile = function (level, nickname, introduce, header, card, repbadge, profileimage, badgelist) {
    let memNickname = nickname;
    let memImageName = "doyun_icon.png";
    let memLevel = level;
    let memHeader = header + ".png";
    let memCard = card + ".png";
    let memSymbolImageName = repbadge + ".png";
    let memIntroduction = introduce;

    let memHeaderStyle = "";
    let memCardStyle = "";
    let memRepBadgeStyle = "";
    let memBadgeList = ["","","","","","","","",""];

    if (header != 'x') {
        memHeaderStyle = `style="background-image:url(../img/shop_img/${memHeader});"`;
    }

    if (card != 'x') {
        memCardStyle = `style="background-image:url(../img/shop_img/${memCard});"`;
    }

    if (repbadge != 'x') {
        memRepBadgeStyle = `<img class="badge" src="../img/shop_img/${memSymbolImageName}";>`
    } 

    if(profileimage!='x'){
        memImageName= `${profileimage}`;
    }

    if (badgelist != 'x'){
        memBadgeList = badgelist.split(', ');

        for(let i=0; i<memBadgeList.length; i++) {
            if (memBadgeList[i] != 'x'){
                memBadgeList[i] = `<img src="../img/shop_img/${memBadgeList[i]}.png">`
            } else {
                memBadgeList[i] = "";
            }
        }
    }

    let membProfileHTML = `
        <div class="profile-card" ${memCardStyle}>
            <a id="profile">
                <div class="profile-header" ${memHeaderStyle}>
                    <div class="profile-image">
                        <img src="/image/${memImageName}" alt="NO-IMAGE">
                    </div>
                </div>
                <div class="profile-user_info">
                    <div class="profile-user_level"><span>LV : ${memLevel}</span></div>
                    <div class="profile-user_nick"><span>${memNickname}</span></div>
                    ${memRepBadgeStyle}
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
                        <div class="badge">${memBadgeList[0]}</div>
                        <div class="badge">${memBadgeList[3]}</div>
                        <div class="badge">${memBadgeList[6]}</div>
                    </div>
                    <div class="badge-line">
                        <div class="badge">${memBadgeList[1]}</div>
                        <div class="badge">${memBadgeList[4]}</div>
                        <div class="badge">${memBadgeList[7]}</div>
                    </div>
                    <div class="badge-line">
                        <div class="badge">${memBadgeList[2]}</div>
                        <div class="badge">${memBadgeList[5]}</div>
                        <div class="badge">${memBadgeList[8]}</div>
                    </div>
                </div>
            </div>
        </div>
    `
    return membProfileHTML;
}