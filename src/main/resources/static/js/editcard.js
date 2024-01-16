document.addEventListener('DOMContentLoaded', function () {
    const Headregister = document.getElementById("header");
    const Cardregister = document.getElementById("card");
    const RepBadgeregister = document.getElementById("badgerep");
    const Badgeregister = document.getElementById("badge");

    Headregister.addEventListener('click', RegisterHeader);
    Cardregister.addEventListener('click', RegisterCard); 
    RepBadgeregister.addEventListener('click', RegisterRepBadge);
    Badgeregister.addEventListener('click', BadgePreview);
});


function addToPreviewList(category) {
    let PreviewHeader = document.querySelector('.profile-header');
    let PreviewCard = document.querySelector('.profile-card');
    let PreviewShopBadge = document.querySelector('.shopbadge');

    category = category.replace(/"/g, '');


    // 리스트 아이템에 해당 아이템을 제거하는 버튼 생성
    let deleteButton = document.createElement('button');
    //deleteButton.textContent = 'x';

    // 클릭 이벤트를 연결하여 해당 아이템을 제거하는 함수 호출
    deleteButton.addEventListener('click', function () {
        removeFromPreviewList(listItem);
        switch (category) {
            case '801' : PreviewHeader.style.backgroundImage = ""; break;
            case '802' : PreviewCard.style.backgroundImage = ""; break;
            case '803' : PreviewShopBadge.style.backgroundImage = ""; break
        }
    });
}

function HeaderPreview(){
    let PreviewHeader = document.querySelector('.profile-header');
    let HeaderItem = document.getElementById('headeritem').value;

    let imagePath = '../img/shop_img/' + HeaderItem + '.png';
    PreviewHeader.style.backgroundImage = 'url(' + imagePath + ')';
}

function CardPreview(){
    let PreviewCard = document.querySelector('.profile-card');
    let CardItem = document.getElementById('carditem').value;

    let imagePath = '../img/shop_img/' + CardItem + '.png';
    PreviewCard.style.backgroundImage = 'url(' + imagePath + ')';
}

function RepBadgePreview(){
    let PreviewRepBadge = document.querySelector('.badge');
    let BadgeItem = document.getElementById('repbadge').value;

    let imagePath = '../img/shop_img/' + BadgeItem + '.png';
    PreviewRepBadge.src = imagePath;
}

function BadgePreview(){
    let PreviewBadge = document.querySelector('.shopbadge');
    let BadgeItem = document.getElementById('badges').value;
    console.log(BadgeItem);
    
}

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