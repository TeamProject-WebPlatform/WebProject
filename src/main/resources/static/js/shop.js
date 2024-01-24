

// 중복 체크 함수
function isItemAlreadyInPreview(itemName, category) {
    let previewList = document.getElementById('previewList');
    let listItems = previewList.getElementsByClassName('list-item');

    for (let i = 0; i < listItems.length; i++) {
        let listItem = listItems[i];

        // 아이템 이름과 카테고리가 둘 다 동일한 경우 중복으로 처리
        if (listItem.textContent === itemName && listItem.dataset.category === category) {
            return true; // 이미 미리보기 목록에 있는 아이템
        }
    }

    return false; // 중복 없음
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
            } else if (flag=='2'){
                alert("포인트가 부족합니다.");
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


