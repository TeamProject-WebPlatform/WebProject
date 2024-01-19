function toggleDivs() {
    // 화면 전환 함수
    var displayDiv = document.getElementById("displayDiv");
    var editDiv = document.getElementById("editDiv");

    if (displayDiv.style.display === "none") {
        // 표시용 <div>를 표시하고 수정용 <div>를 숨김
        displayDiv.style.display = "block";
        editDiv.style.display = "none";
    } else {
        // 표시용 <div>를 숨기고 수정용 <div>를 표시
        displayDiv.style.display = "none";
        editDiv.style.display = "block";
    }
}

//선호게임 수정하기
document.addEventListener('DOMContentLoaded', function () {
    const register = document.getElementById("mem_favorite_game_modify");

    register.addEventListener('click', FavoriteGameAdd);
});


const FavoriteGameAdd = async function(){
    const first = document.getElementById("first-game").value;
    const second = document.getElementById("second-game").value;
    const third = document.getElementById("third-game").value;
    const selectMemId = document.getElementById("select_memId").value;

    if(first==''|| second==''|| third==''){
        alert("선호 게임을 모두 선택해주세요");
    } else if (first==second || first==third || second==third){
        alert("중복되지 않게 설정해주세요");
    } else {
        try {
            const response = await fetch('/admin/favoritegame', {
                method:'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify({
                    firstgame:first,
                    secondgame:second,
                    thirdgame:third,
                    selectMemId:selectMemId
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
