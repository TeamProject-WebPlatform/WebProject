// document.addEventListener('DOMContentLoaded', function () {
//     // categorySelect 요소 찾기
//     let categorySelect = document.getElementById('categorySelect');

//     // categorySelect 값이 변경될 때마다 호출되는 함수 등록
//     categorySelect.addEventListener('change', function () {
//         // 선택된 카테고리 값 가져오기
//         let selectedCategory = categorySelect.value;
//         console.log(selectedCategory);

//         // 모든 game-card 요소 찾기
//         let gameCards = document.querySelectorAll('.game-card');
//         console.log(gameCards);
//         // 각 game-card에 대해 처리
//         gameCards.forEach(function (card) {
//             // 카테고리 데이터 속성 값 가져오기
//             let cardCategory = card.getAttribute('data-category');

//             // 선택된 카테고리가 'all'이거나 현재 카드의 카테고리와 일치하면 표시
//             if (selectedCategory === 'all' || selectedCategory === cardCategory) {
//                 card.style.display = 'block';
//             } else {
//                 // 아니면 숨김
//                 card.style.display = 'none';
//             }
//         });
//     });
//     // 이하 기존의 showPreview, hidePreview 함수는 그대로 사용
// });

function showPreview(itemName, category) {
    // 프로필 미리보기 div 선택
    let previewheader = document.querySelector('.profile-header');
    let previewcard = document.querySelector('.profile-card');
    let previebadge = document.querySelector('.badge');
    itemName = itemName.replace(/"/g, '');
    // console.log(itemName);
    category = category.replace(/"/g, '');
    console.log(category);
    
    // 선택한 아이템에 맞는 이미지 경로 생성
    let imagePath = './img/shop_img/' + itemName + '.png';
    // console.log(imagePath);
    // 미리보기 div의 배경 이미지 변경
    previewheader.style.backgroundImage = 'url("' + imagePath + '")';
    previewcard.style.backgroundImage = 'url("' + imagePath + '")';
    // console.log(previewheader);

    // 각 분류에 따라 스타일 변경
    if (category === '801') {
        previewheader.classList.add('profile-header');
    } else if (category === '802') {
        previebadge.classList.add('badge');
    } else if (category === '803') {
        previewcard.classList.add('profile-card');
    }

    // 미리보기 div를 화면에 보이도록 설정
    previewheader.style.display = 'block';
}

function hidePreview() {
    // 프로필 미리보기를 숨김
    document.querySelector('.preview-profile').style.display = 'none';
}


function changeCategori() {
    let categorySelect = document.getElementById('categorySelect');
    let gameCards = document.querySelectorAll('.game-card__box');
    let selectedCategory = categorySelect.value;

    console.log(categorySelect);
    console.log(gameCards);
    console.log(selectedCategory);

    gameCards.forEach(function (card) {
        const cardCategory = card.getAttribute('data-category');

        if (selectedCategory === 'all' || selectedCategory === cardCategory) {
            console.log('Displaying card:', cardCategory);
            card.classList.remove('card-hidden');
        } else {
            console.log('Hiding card:', cardCategory);
            card.classList.add('card-hidden');
        }
        
        // const filterContainer = document.querySelector(".portfolio-filter"),
        // portfolioItemsContainer = document.querySelector(".portfolio-items"),
        // portfolioItems = document.querySelectorAll(".portfolio-item");
    
        // filterContainer.addEventListener("click", function(event){
        //     if(event.target.classList.contains("filter-itme")&&!event.target.classList.contains("active")){
        //         // console.log("true");
        //         // deactivate existing active 'filter-item
        //         filterContainer.querySelector(".active").classList.remove("outer-shadow","active");
        //         // activate new 'filter item'
        //         event.target.classList.add("active","outer-shadow");
        //         const target = event.target.getAttribute("data-target");
        //         // console.log(target);
        //         portfolioItems.forEach((item) =>{
        //             if(target === item.getAttribute("data-category")||target==='all'){
        //                 item.classList.remove("hide");
        //                 item.classList.add("show");
        //             }else{
        //                 item.classList.remove("show");
        //                 item.classList.add("hide");
        //             }
        //         })
        //     }else{
        //         console.log("false");
        //     }
        //     console.log(event.target);
        // })
        // if (selectedCategory === 'all' || selectedCategory === cardCategory) {
        //     console.log('Displaying card:', cardCategory);
        //     card.style.display = 'block';
        // } else {
        //     console.log('Hiding card:', cardCategory);
        //     card.style.display = 'none';
        // }
    });
};

function handleSearch() {
    let searchInput = document.getElementById('ItemSearch');
    let gameCards = document.querySelectorAll('.game-card');

    let searchTerm = searchInput.value.toLowerCase();

    gameCards.forEach(function (card) {
        const cardTitle = card.querySelector('.game-card__title').textContent.toLowerCase();

        // 정규 표현식을 사용하여 섞여 있는지 확인
        const regex = new RegExp([...searchTerm].join('.*'), 'i');
        const isMatch = cardTitle.match(regex);

        if (isMatch) {
            console.log('Displaying card:', cardTitle);
            card.style.display = 'block';
        } else {
            console.log('Hiding card:', cardTitle);
            card.style.display = 'none';
        }
    });
}

// 검색어 입력 이벤트 감지
document.getElementById('ItemSearch').addEventListener('input', handleSearch);


// document.addEventListener('DOMContentLoaded', function () {
//     const filterContainer = document.getElementById("categorySelect"),
//         itemGrid = document.getElementById("itemGrid"),
//         items = document.querySelectorAll(".game-card");

//     filterContainer.addEventListener("change", function (event) {
//         const selectedCategory = event.target.value;

//         items.forEach((item) => {
//             const category = item.dataset.category;

//             if (selectedCategory === 'all' || selectedCategory === category) {
//                 item.classList.remove("hide");
//                 item.classList.add("show");
//             } else {
//                 item.classList.remove("show");
//                 item.classList.add("hide");
//             }
//         });
//     });
// });