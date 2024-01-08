document.addEventListener('DOMContentLoaded', function () {
    var categorySelect = document.getElementById('categorySelect');
    var gameCards = document.querySelectorAll('.game-card');

    categorySelect.addEventListener('change', function () {
        var selectedCategory = categorySelect.value;

        gameCards.forEach(function (card) {
            var cardCategory = card.getAttribute('data-category');

            if (selectedCategory === 'all' || selectedCategory === cardCategory) {
                card.style.display = 'block';
            } else {
                card.style.display = 'none';
            }
        });
    });
});
