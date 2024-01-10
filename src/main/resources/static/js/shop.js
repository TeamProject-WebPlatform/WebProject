function showPreview(itemName, category) {
    // 프로필 미리보기 div 선택
    let previewheader = document.querySelector('.profile-header');
    let previewcard = document.querySelector('.profile-card');
    let previewshopbadge = document.querySelector('.shopbadge');

    // 아이템 이름과 카테고리에서 큰 따옴표 제거
    itemName = itemName.replace(/"/g, '');
    category = category.replace(/"/g, '');
    console.log(category);

    // 선택한 아이템에 맞는 이미지 경로 생성
    let imagePath = './img/shop_img/' + itemName + '.png';

    
    // 각 분류에 따라 스타일 변경
    if (category === '801') {
        // 미리보기 div의 배경 이미지 변경
        previewheader.style.backgroundImage = 'url(' + imagePath + ')';
        // previewcard.style.backgroundImage = 'none';
        // previewshopbadge.style.backgroundImage = 'none';
    } else if (category === '802') {
        previewshopbadge.style.backgroundImage = 'url(' + imagePath + ')';
        // previewheader.style.backgroundImage = 'none';
        // previewcard.style.backgroundImage = 'none';
    } else if (category === '803') {
        previewcard.style.backgroundImage = 'url(' + imagePath + ')';
        // previewheader.style.backgroundImage = 'none';
        // previewshopbadge.style.backgroundImage = 'none';
    }

    // 미리보기 div를 화면에 보이도록 설정
    previewheader.style.display = 'block';
}

function hidePreview() {
    // 프로필 미리보기를 숨김
    document.querySelector('.profile-header').style.backgroundImage = 'none';
    document.querySelector('.profile-card').style.backgroundImage = 'none';
    document.querySelector('.badge').style.backgroundImage = 'none';
}


function changeCategori(){
    var ItemSearch = document.getElementById("ItemSearch").value;
    var categorySelect = document.getElementById("categorySelect").value;
    console.log(ItemSearch);
    console.log(categorySelect);

    if(ItemSearch==""){
        location.href = "./shop_search?categorySelect=" + categorySelect;
    }
    if(categorySelect=="all"){
        location.href = "./shop";
    }else{
        location.href = "./shop_search?ItemSearch=" + ItemSearch +"&categorySelect=" + categorySelect;
    }
}