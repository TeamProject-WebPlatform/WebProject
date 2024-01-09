//  // 페이지 로드 시 실행
//  document.addEventListener('DOMContentLoaded', function () {
//     updateItemGrid();
// });

// // 카테고리 선택이 변경될 때 호출되는 함수
// document.getElementById('categorySelect').addEventListener('change', function () {
//     updateItemGrid();
// });

// // 상품 그리드 업데이트 함수
// function updateItemGrid() {
//     var selectedCategory = document.getElementById('categorySelect').value;
//     var items = document.querySelectorAll('#itemGrid .game-card');

//     // 모든 상품을 숨김
//     items.forEach(function (item) {
//         item.style.display = 'none';
//     });

//     // 선택한 카테고리에 해당하는 상품만 표시
//     if (selectedCategory === 'all') {
//         items.forEach(function (item) {
//             item.style.display = 'block';
//         });
//     } else {
//         var selectedItems = document.querySelectorAll('#itemGrid .game-card[data-category="' + selectedCategory + '"]');
//         selectedItems.forEach(function (item) {
//             item.style.display = 'block';
//         });
//     }
// }




// document.addEventListener('DOMContentLoaded', function () {
//     // categorySelect 요소 찾기
//     var categorySelect = document.getElementById('categorySelect');

//     // categorySelect 값이 변경될 때마다 호출되는 함수 등록
//     categorySelect.addEventListener('change', function () {
//         // 선택된 카테고리 값 가져오기
//         var selectedCategory = categorySelect.value;

//         // 모든 game-card 요소 찾기
//         var gameCards = document.querySelectorAll('.game-card');

//         // 각 game-card에 대해 처리
//         gameCards.forEach(function (card) {
//             // 카테고리 데이터 속성 값 가져오기
//             var cardCategory = card.getAttribute('data-category');

//             // 선택된 카테고리가 'all'이거나 현재 카드의 카테고리와 일치하면 표시
//             if (selectedCategory === 'all' || selectedCategory === cardCategory) {
//                 card.style.display = 'block';
//             } else {
//                 // 아니면 숨김
//                 card.style.display = 'none';
//             }
//         });
//     });
// });




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