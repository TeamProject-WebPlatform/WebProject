document.addEventListener('DOMContentLoaded', function () {
    const register = document.getElementById("game-submit");

    register.addEventListener('click', FavoriteGameAdd);
});


const FavoriteGameAdd = async function(){
    const first = document.getElementById("first-game").value;
    const second = document.getElementById("second-game").value;
    const third = document.getElementById("third-game").value;

    if(first==''|| second==''|| third==''){
        alert("선호 게임을 모두 선택해주세요");
    } else if (first==second || first==third || second==third){
        alert("중복되지 않게 설정해주세요");
    } else {
        try {
            const response = await fetch('/profile/favoritegame', {
                method:'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify({
                    firstgame:first,
                    secondgame:second,
                    thirdgame:third
                })
            });
            if(!response.ok) {
                throw new Error("서버 응답이 실패했습니다");
            }
            const flag = await response.text();
            if(flag=='0') {
                alert("수정 되었습니다.");
            } else if (flag=='1') {
                alert("등록 되었습니다.");
            } else {
                alert("에러" + error.message);
            }
        } catch (error) {
            console.error("Error: " + error);
        }
    }
}
