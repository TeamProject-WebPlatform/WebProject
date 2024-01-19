document.addEventListener('DOMContentLoaded', function () {
    const Headregister = document.getElementById("header");
    const Cardregister = document.getElementById("card");
    const RepBadgeregister = document.getElementById("badgerep");
    const Badgeregister = document.getElementById("badge");
    const ProfileImageChange = document.querySelector('#profile');


    Headregister.addEventListener('click', RegisterHeader);
    Cardregister.addEventListener('click', RegisterCard); 
    RepBadgeregister.addEventListener('click', RegisterRepBadge);
    Badgeregister.addEventListener('click', RegisterBadgeList);
    ProfileImageChange.addEventListener('click', () => document.querySelector('.upload').click());
    document.querySelector('.upload').addEventListener('change', ProfileImage);
});

// 헤더 미리보기
function HeaderPreview(){
    let PreviewHeader = document.querySelector('.profile-header');
    let HeaderItem = document.getElementById('headeritem').value;

    if(HeaderItem=='x'){
        PreviewHeader.style.backgroundImage = "";
    } else {
        let imagePath = '../img/shop_img/' + HeaderItem + '.png';
        PreviewHeader.style.backgroundImage = 'url(' + imagePath + ')';
    }
}

// 카드 미리보기
function CardPreview(){
    let PreviewCard = document.querySelector('.profile-card');
    let CardItem = document.getElementById('carditem').value;

    if(CardItem=='x') {
        PreviewCard.style.backgroundImage = "";
    } else {
        let imagePath = '../img/shop_img/' + CardItem + '.png';
        PreviewCard.style.backgroundImage = 'url(' + imagePath + ')';
    }
    
}

// 대표 뱃지 미리보기
function RepBadgePreview(){
    let PreviewRepBadge = document.querySelector('.badge');
    let BadgeItem = document.getElementById('repbadge').value;
    if(BadgeItem=='x') {
        PreviewRepBadge.removeAttribute('src');
    } else {
        let imagePath = '../img/shop_img/' + BadgeItem + '.png';
        PreviewRepBadge.src = imagePath;
    }
    
}

// 뱃지 설정 미리보기
function BadgePreview(number){
    let PreviewBadge = document.querySelector('.badge'+number);
    let BadgeItem = document.getElementById('badges'+number).value;
    if(BadgeItem=='x') {
        PreviewBadge.removeAttribute('src');
    } else {
        let imagePath = '../img/shop_img/' + BadgeItem + '.png';
        PreviewBadge.src = imagePath;
    }
    console.log(BadgeItem);
}

// 헤더 저장
const RegisterHeader = async function() {
    let HeaderItem = document.getElementById('headeritem').value;

    try {
        const response = await fetch('/profile/headerprofile', {
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                Header:HeaderItem
            }),
        });
        if(!response.ok) {
            throw new Error("서버 응답이 실패했습니다");
        }
        const flag = await response.text();
        if(flag=='1') {
            alert("프로필 사진 배경이 수정되었습니다.");
            location.reload();
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}

// 카드 저장
async function RegisterCard() {
    let CardItem = document.getElementById('carditem').value;

    try {
        const response = await fetch('/profile/cardprofile', {
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                Card:CardItem
            }),
        });
        if(!response.ok) {
            throw new Error("서버 응답이 실패했습니다");
        }
        const flag = await response.text();
        if(flag=='1') {
            alert("프로필 카드가 수정되었습니다.");
            location.reload();
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}


// 대표 뱃지 저장
async function RegisterRepBadge() {
    let BadgeItem = document.getElementById('repbadge').value;

    try {
        const response = await fetch('/profile/repbadgeprofile', {
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                RepBadge:BadgeItem
            }),
        });
        if(!response.ok) {
            throw new Error("서버 응답이 실패했습니다");
        }
        const flag = await response.text();
        if(flag=='1') {
            alert("대표 뱃지가 수정되었습니다.");
            location.reload();
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}

// 뱃지 리스트 저장
async function RegisterBadgeList() {
    let BadgeItem1 = document.getElementById('badges1').value;
    let BadgeItem2 = document.getElementById('badges2').value;
    let BadgeItem3 = document.getElementById('badges3').value;
    let BadgeItem4 = document.getElementById('badges4').value;
    let BadgeItem5 = document.getElementById('badges5').value;
    let BadgeItem6 = document.getElementById('badges6').value;
    let BadgeItem7 = document.getElementById('badges7').value;
    let BadgeItem8 = document.getElementById('badges8').value;
    let BadgeItem9 = document.getElementById('badges9').value;

    let badgelist = [BadgeItem1,BadgeItem2,BadgeItem3,BadgeItem4,BadgeItem5,BadgeItem6,BadgeItem7,BadgeItem8,BadgeItem9];
    //console.log(badgelist);

    try {
        const response = await fetch('/profile/badgelist', {
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                BadgeList:badgelist
            }),
        });
        if(!response.ok) {
            throw new Error("서버 응답이 실패했습니다");
        }
        const flag = await response.text();
        if(flag=='1') {
            alert("뱃지 칸이 수정 되었습니다.");
            location.reload();
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}

// 프로필 사진 변경
async function ProfileImage(e) {
    const file = e.currentTarget.files[0];
    var formdata = new FormData();

    formdata.append('image', file);

    try {
        const response = await fetch('/profile/upload', {
            method:'POST',
            body:formdata
        });
        if(!response.ok) {
            throw new Error("서버 응답이 실패했습니다");
        }
        const flag = await response.text();
        if(flag=='1') {
            alert("프로필 사진이 변경되었습니다.");
            location.reload();
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}