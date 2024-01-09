document.addEventListener('DOMContentLoaded', function () {
    // categorySelect 요소 찾기
    var categorySelect = document.getElementById('categorySelect');

    // categorySelect 값이 변경될 때마다 호출되는 함수 등록
    categorySelect.addEventListener('change', function () {
        // 선택된 카테고리 값 가져오기
        var selectedCategory = categorySelect.value;

        // 모든 game-card 요소 찾기
        var gameCards = document.querySelectorAll('.game-card');

        // 각 game-card에 대해 처리
        gameCards.forEach(function (card) {
            // 카테고리 데이터 속성 값 가져오기
            var cardCategory = card.getAttribute('data-category');

            // 선택된 카테고리가 'all'이거나 현재 카드의 카테고리와 일치하면 표시
            if (selectedCategory === 'all' || selectedCategory === cardCategory) {
                card.style.display = 'block';
            } else {
                // 아니면 숨김
                card.style.display = 'none';
            }
        });
    });

    // 이하 기존의 showPreview, hidePreview 함수는 그대로 사용
});

function showPreview(itemName, category) {
    // 프로필 미리보기 div 선택
    let previewheader = document.querySelector('.profile-header');
    itemName = itemName.replace(/"/g, '');
    console.log(itemName);


    // 선택한 아이템에 맞는 이미지 경로 생성
    let imagePath = './img/shop_img/' + itemName + '.png';
    console.log(imagePath);
    // 미리보기 div의 배경 이미지 변경
    previewheader.style.backgroundImage = 'url("' + imagePath + '")';
    console.log(previewheader);

    // 각 분류에 따라 스타일 변경
    if (category === '801') {
        previewheader.classList.add('profile-header');
        previewheader.classList.remove('badge', 'profile-border'); // 다른 스타일 초기화
    } else if (category === '802') {
        previewheader.classList.add('badge');
        previewheader.classList.remove('profile-header', 'profile-border');
    } else if (category === '803') {
        previewheader.classList.add('profile-border');
        previewheader.classList.remove('profile-header', 'badge');
    }

    // 미리보기 div를 화면에 보이도록 설정
    previewheader.style.display = 'block';
}

function hidePreview() {
    // 프로필 미리보기를 숨김
    document.querySelector('.preview-profile').style.display = 'none';
}


// function changeCategori() {
//     let categorySelect = document.getElementById('categorySelect');
//     let gameCards = document.querySelectorAll('.game-card');
//     let selectedCategory = categorySelect.value;

//     console.log(categorySelect);
//     console.log(gameCards);
//     console.log(selectedCategory);

//     gameCards.forEach(function (card) {
//         const cardCategory = card.getAttribute('data-category');

//         if (selectedCategory === 'all' || selectedCategory === cardCategory) {
//             console.log('Displaying card:', cardCategory);
//             card.style.display = 'block';
//         } else {
//             console.log('Hiding card:', cardCategory);
//             card.style.display = 'none';
//         }
//     });
// }


// document.addEventListener('DOMContentLoaded', function () {
//     let categorySelect = document.getElementById('categorySelect').value;
//     const gameCards = document.querySelectorAll('.game-card');

//     console.log(categorySelect);
//     console.log(gameCards);
//     function changeCategori(){
//         const selectedCategory = categorySelect.value;
//         console.log(categorySelect);
//         gameCards.forEach(function (card) {
//             const cardCategory = card.getAttribute('data-category');
//             if ((selectedCategory === 'all' || selectedCategory === cardCategory) &&
//                 (selectedCategory === 'board' && cardCategory.startsWith('801')) ||
//                 (selectedCategory === 'badge' && cardCategory.startsWith('802')) ||
//                 (selectedCategory === 'bg' && cardCategory.startsWith('803'))) {
//                 console.log('Displaying card:', cardCategory);
//                 card.style.display = 'block';
//             } else {
//                 console.log('Hiding card:', cardCategory);
//                 card.style.display = 'none';
//             }
//         });
//     }
// });

// document.addEventListener('DOMContentLoaded', function () {
//     const categorySelect = document.getElementById('categorySelect').value;
//     const gameCards = document.querySelectorAll('.game-card');

//     categorySelect.addEventListener('change', function () {
//         // const selectedCategory = categorySelect.value;
//         // console.log('Selected Category:', selectedCategory);

//         gameCards.forEach(function (card) {
//             const cardCategory = card.getAttribute('data-category');

//             if (
//                 (categorySelect === 'all' || categorySelect === cardCategory) &&
//                 ((categorySelect === 'board' && cardCategory.startsWith('801')) ||
//                 (categorySelect === 'badge' && cardCategory.startsWith('802')) ||
//                 (categorySelect === 'bg' && cardCategory.startsWith('803')))
//             ) {
//                 console.log('Displaying card:', cardCategory);
//                 card.style.display = 'block';
//             } else {
//                 console.log('Hiding card:', cardCategory);
//                 card.style.display = 'none';
//             }
//         });
//     });
// });


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