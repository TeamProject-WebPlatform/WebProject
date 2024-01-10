  // 미리보기 목록에 아이템을 추가하는 함수
function addToPreviewList(itemName, category) {
    let previewList = document.getElementById('previewList');

    // 미리보기 목록을 위한 리스트 아이템 생성
    let listItem = document.createElement('li');
    listItem.textContent = itemName;

    // 리스트 아이템에 해당 아이템을 제거하는 버튼 생성
    let deleteButton = document.createElement('button');
    deleteButton.textContent = 'x';

    // 클릭 이벤트를 연결하여 해당 아이템을 제거하는 함수 호출
    deleteButton.addEventListener('click', function () {
    removeFromPreviewList(listItem);
    });

    // 제거 버튼을 리스트 아이템에 추가
    listItem.appendChild(deleteButton);

    // 리스트 아이템을 미리보기 목록에 추가
    previewList.appendChild(listItem);
}

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
        previewcard.style.backgroundImage = 'url(' + imagePath + ')';
        // previewheader.style.backgroundImage = 'none';
        // previewshopbadge.style.backgroundImage = 'none';
    } else if (category === '803') {
        previewshopbadge.style.backgroundImage = 'url(' + imagePath + ')';
    }

    // 미리보기 div를 화면에 보이도록 설정
    previewheader.style.display = 'block';

     // 미리보기 목록에 아이템 추가하는 함수 호출
    addToPreviewList(itemName, category);
}

function hidePreview() {
    // 프로필 미리보기를 숨김
    document.querySelector('.profile-header').style.backgroundImage = 'none';
    document.querySelector('.profile-card').style.backgroundImage = 'none';
    document.querySelector('.shopbadge').style.backgroundImage = 'none';
}

// 미리보기 목록에서 아이템을 제거하는 함수
function removeFromPreviewList(listItem) {
    let previewList = document.getElementById('previewList');
    // 미리보기 목록에서 해당 리스트 아이템 제거
    previewList.removeChild(listItem);
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