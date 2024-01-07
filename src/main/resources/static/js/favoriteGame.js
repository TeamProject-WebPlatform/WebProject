document.addEventListener('DOMContentLoaded', function () {
    const tt = document.getElementById("game-submit");

    tt.addEventListener('click', FavoriteGameAdd);
});


const FavoriteGameAdd = function(){
    const first = document.getElementById("first-game").value;
    const second = document.getElementById("second-game").value;
    const third = document.getElementById("third-game").value;

    console.log(first, second, third);
}
