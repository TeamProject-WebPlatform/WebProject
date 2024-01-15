document.addEventListener('DOMContentLoaded', function () {
    const Headregister = document.getElementById("header");
    const Cardregister = document.getElementById("card");
    const RepBadgeregister = document.getElementById("badgerep");
    const Badgeregister = document.getElementById("badge");

    Headregister.addEventListener('click', HeaderPreview);
    Cardregister.addEventListener('click', CardPreview); 
    RepBadgeregister.addEventListener('click', RepBadgePreview);
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
    console.log(imagePath);
    PreviewHeader.style.backgroundImage = 'url(' + imagePath + ')';

   
}

function CardPreview(){
    let PreviewCard = document.querySelector('.profile-card');
    let CardItem = document.getElementById('carditem').value;

    let imagePath = '../img/shop_img/' + CardItem + '.png';
    console.log(imagePath);
    PreviewCard.style.backgroundImage = 'url(' + imagePath + ')';
}

function RepBadgePreview(){
    let PreviewRepBadge = document.querySelector('.badge');
    let BadgeItem = document.getElementById('repbadge').value;

    let imagePath = '../img/shop_img/' + BadgeItem + '.png';
    console.log(imagePath);
    PreviewRepBadge.src = imagePath;
}

function BadgePreview(){
    let PreviewBadge = document.querySelector('.shopbadge');
    let BadgeItem = document.getElementById('badges').value;
    console.log(BadgeItem);
    
}