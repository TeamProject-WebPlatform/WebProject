
// 미리보기 목록에 아이템을 추가하는 함수
function addToPreviewList(itemName, category) {
    let previewList = document.getElementById('previewList');
    let PreviewHeader = document.querySelector('.profile-header');
    let PreviewCard = document.querySelector('.profile-card');
    let PreviewShopBadge = document.querySelector('.shopbadge');

    category = category.replace(/"/g, '');
    // 미리보기 목록을 위한 리스트 아이템 생성
    let listItem = document.createElement('li');
    listItem.textContent = itemName;
    listItem.classList.add('list-item'); // 'list-item' 클래스 추가
    // 리스트 아이템에 해당 아이템을 제거하는 버튼 생성
    let deleteButton = document.createElement('button');
    deleteButton.textContent = 'x';
    deleteButton.classList.add('delete-button'); // 'delete-button' 클래스 추가


    // 클릭 이벤트를 연결하여 해당 아이템을 제거하는 함수 호출
    deleteButton.addEventListener('click', function () {
        removeFromPreviewList(listItem);
        switch (category) {
            case '801' : PreviewHeader.style.backgroundImage = ""; break;
            case '802' : PreviewCard.style.backgroundImage = ""; break;
            case '803' : PreviewShopBadge.style.backgroundImage = ""; break
        }
    });

    // 제거 버튼을 리스트 아이템에 추가
    listItem.appendChild(deleteButton);

    // 리스트 아이템을 미리보기 목록에 추가
    previewList.appendChild(listItem);
}

function showPreview(itemName, category) {
    // 프로필 미리보기 div 선택
    let PreviewHeader = document.querySelector('.profile-header');
    let PreviewCard = document.querySelector('.profile-card');
    let PreviewShopBadge = document.querySelector('.shopbadge');
    let previewList = document.getElementById('previewList');

    // 아이템 이름과 카테고리에서 큰 따옴표 제거
    itemName = itemName.replace(/"/g, '');
    category = category.replace(/"/g, '');
    console.log(category);

    // 선택한 아이템에 맞는 이미지 경로 생성
    let imagePath = './img/shop_img/' + itemName + '.png';
    console.log(imagePath);
    // 각 분류에 따라 스타일 변경
    if (category === '801') {
        // 미리보기 div의 배경 이미지 변경
        if (PreviewHeader.style.backgroundImage==""){
            PreviewHeader.style.backgroundImage = 'url(' + imagePath + ')';
        } else {
            removeBeforeItem(itemName);
            PreviewHeader.style.backgroundImage = 'url(' + imagePath + ')';
        }
    } else if (category === '802') {
        if (PreviewCard.style.backgroundImage==""){
            PreviewCard.style.backgroundImage = 'url(' + imagePath + ')';
        } else {
            // let secondItem = previewList.querySelector('li:nth-child(2)');
            // previewList.removeChild(secondItem);
            removeBeforeItem(itemName);
            PreviewCard.style.backgroundImage = 'url(' + imagePath + ')';
        }
    } else if (category === '803') {
        if (PreviewShopBadge.style.backgroundImage==""){
            PreviewShopBadge.style.backgroundImage = 'url(' + imagePath + ')';
        } else {
            // previewList.removeChild(previewList.lastChild);
            removeBeforeItem(itemName);
            PreviewShopBadge.style.backgroundImage = 'url(' + imagePath + ')';
        }
    }

    // 미리보기 div를 화면에 보이도록 설정
    PreviewHeader.style.display = 'block';

     // 미리보기 목록에 아이템 추가하는 함수 호출
    addToPreviewList(itemName, category);
}

function hidePreview() {
    // 프로필 미리보기를 숨김
    document.querySelector('.profile-header').style.backgroundImage = "";
    document.querySelector('.profile-card').style.backgroundImage = "";
    document.querySelector('.shopbadge').style.backgroundImage = "";
    let previewList = document.getElementById('previewList');
    while(previewList.firstChild){
        previewList.removeChild(previewList.firstChild);
    }
    
}

// 미리보기 목록에서 아이템을 제거하는 함수
function removeFromPreviewList(listItem) {
    let previewList = document.getElementById('previewList');
    // 미리보기 목록에서 해당 리스트 아이템 제거
    previewList.removeChild(listItem);
}

// 팝업 띄우기
function openPopup(imgElement) {
    var popup = document.getElementById('popup');
    var popupImg = document.getElementById('popupImage');

    popup.style.display = 'block';
    popupImg.src = imgElement.src;
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

// 아이템 구매
async function getItem(point,category) {
    if (confirm("정말 구매 하시겠습니까??") == true){    //확인
        try {
            const response = await fetch('/itempurchase', {
                method:'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify({
                    Point : point,
                    Category : category
                })
            });
            if(!response.ok) {
                throw new Error("서버 응답이 실패했습니다");
            }
            const flag = await response.text();
            if (flag=='1') {
                alert("아이템을 구매하였습니다.");
                location.reload();
            } else {
                alert("에러" + error.message);
            }
        } catch (error) {
            console.error("Error: " + error);
        }
        sendPointChange(point, pointKindCd);
    }else{   //취소
        return false;
    }
}

// 데이터 전송

function changeCategori() {
    var ItemSearch = document.getElementById("ItemSearch").value;
    var categorySelect = document.getElementById("categorySelect").value;

    // ItemSearch가 빈 문자열이면 검색어를 비워서 전달하도록 처리
    if (ItemSearch == "") {
        ItemSearch = ""; // 빈 문자열로 설정
    }

    console.log(ItemSearch);
    console.log(categorySelect);

    if (ItemSearch == "" && categorySelect != "all") {
        location.href = "./shop_search?categorySelect=" + categorySelect;
    } else if (categorySelect == "all") {
        location.href = "./shop";
    } else {
        location.href = "./shop_search?ItemSearch=" + ItemSearch + "&categorySelect=" + categorySelect;
    }
}

function changeCategori2() {
    var ItemSearch = document.getElementById("ItemSearch").value;
    // ItemSearch가 빈 문자열이면 검색어를 비워서 전달하도록 처리
    if (ItemSearch == "") {
        ItemSearch = ""; // 빈 문자열로 설정
    }

    console.log(ItemSearch);

    location.href = "./shop_search?ItemSearch=" + ItemSearch;
}


// 미리보기 할 때 다른 아이템을 미리보기 시 이전 아이템을 리스트에서 없애는 함수
function removeBeforeItem(item){
    var list = document.getElementById('previewList');
    var beforeitem = list.getElementsByTagName('li');
    for (var i=0; i<beforeitem.length; i++){
        list.removeChild(beforeitem[i]);
        break;
    }
}