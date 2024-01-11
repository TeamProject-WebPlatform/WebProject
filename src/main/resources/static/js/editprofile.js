document.addEventListener('DOMContentLoaded', function () {
    const register = document.getElementById("ChangeIntroduce");
    const DuplicateNick = document.getElementById("DuplicateNick");
    const ChangeNick = document.getElementById("ChangeNick");
    const ChangePassword = document.getElementById("ChangePassword");

    register.addEventListener('click', UpdateIntroduce);
    DuplicateNick.addEventListener('click', DuplicateNickCheck);
    ChangeNick.addEventListener('click', ChangeNickname);
    ChangePassword.addEventListener('click', ChangePw);
});

const UpdateIntroduce = async function (){
    const introtext = document.getElementById("introduce").value;

    try {
        const response = await fetch('/profile/updateintroduce', {
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                introduce:introtext
            }),
        });
        if(!response.ok) {
            throw new Error("서버 응답이 실패했습니다");
        }
        const flag = await response.text();
        if(flag=='1') {
            alert("소개글 수정 완료.");
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}

const DuplicateNickCheck = async function (){

}

const ChangeNickname = async function (){

}

const ChangePw = async function (){

}