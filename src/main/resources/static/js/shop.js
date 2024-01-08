function changeCategori(){
    let categorySelect = document.getElementById('categorySelect').value;
    let ItemSearch = document.getElementById('Item_search').value;
    console.log("categorySelect : " + categorySelect);
    console.log("ItemSearch : " + ItemSearch);

    location.href = "./shop/search?categorySelect=" + categorySelect + "&ItemSearch=" + ItemSearch;
}



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

document.addEventListener('DOMContentLoaded', function () {
    const categorySelect = document.getElementById('categorySelect').value;
    const gameCards = document.querySelectorAll('.game-card');

    categorySelect.addEventListener('change', function () {
        // const selectedCategory = categorySelect.value;
        // console.log('Selected Category:', selectedCategory);

        gameCards.forEach(function (card) {
            const cardCategory = card.getAttribute('data-category');

            if (
                (categorySelect === 'all' || categorySelect === cardCategory) &&
                ((categorySelect === 'board' && cardCategory.startsWith('801')) ||
                (categorySelect === 'badge' && cardCategory.startsWith('802')) ||
                (categorySelect === 'bg' && cardCategory.startsWith('803')))
            ) {
                console.log('Displaying card:', cardCategory);
                card.style.display = 'block';
            } else {
                console.log('Hiding card:', cardCategory);
                card.style.display = 'none';
            }
        });
    });
});