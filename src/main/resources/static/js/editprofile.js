document.addEventListener('DOMContentLoaded', function () {
    const register = document.getElementById("ChangeIntroduce");
    const DuplicateNick = document.getElementById("DuplicateNick");
    const ChangeNick = document.getElementById("ChangeNick");
    const ChangePassword = document.getElementById("ChangePassword");
    var result = false;

    register.addEventListener('click', UpdateIntroduce);
    DuplicateNick.addEventListener('click', async function(){
        result = await DuplicateNickCheck();
    });
    ChangeNick.addEventListener('click', function(){
        if (!result){
            alert("중복 확인을 먼저 해주세요");
        } else {
            ChangeNickname();
        }
    });
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
            location.reload();
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }
}

const DuplicateNickCheck = async function (){
    const Nickname = document.getElementById("modifyNick").value;

    let check = false;
    var regex = /[!@#$%^&*(),.?":{}|<>]/;
    
    if (regex.test(Nickname)) {
        alert("특수문자는 사용할 수 없습니다.")
    } else if (Nickname.length < 4 || Nickname.length > 8){
        alert("닉네임은 4~8자 만 설정 가능합니다.");
    } else {
        try {
            const response = await fetch('/login/DuplicateNickCheck', {
                method:'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body:Nickname
            });
            if(!response.ok) {
                throw new Error("서버 응답이 실패했습니다");
            }
            const flag = await response.text();
            if (flag=='true'){
                alert("이미 존재하는 닉네임 입니다.");
                check = false;
            } else {
                alert("사용 가능한 닉네임 입니다.");
                check = true;
            }
        } catch (error) {
            console.error("Error: " + error);
        }
    }
    return check;
}

const ChangeNickname = async function (){
    const Nickname = document.getElementById("modifyNick").value;
    
    try {
        const response = await fetch('/profile/changenick', {
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                Nickname : Nickname
            }),
        });
        if(!response.ok) {
            throw new Error("서버 응답이 실패했습니다");
        }
        const flag = await response.text();
        if(flag=='1') {
            alert("닉네임이 변경 되었습니다.");
            location.reload();
        } else {
            alert("에러" + error.message);
        }
    } catch (error) {
        console.error("Error: " + error);
    }

    document.getElementById("modifyNick").value="";
}

const ChangePw = async function (){
    const ModifyPassword = document.getElementById("modifypassword").value;
    const CheckPassword = document.getElementById("checkpassword").value;

    let chk = false; // 숫자 + 문자의 조합인지
    let haveNum = false;
    let haveChar = false;

    for (let i = 0; i < ModifyPassword.length; i++) {
        if (haveNum && haveChar) {
            chk = true;
            break;
        }
        if (!isNaN(ModifyPassword[i])) {
            haveNum = true;
        } else {
            haveChar = true;
        }
    }

    if (ModifyPassword!=CheckPassword){
        alert("비밀번호가 서로 다릅니다.");
    } else if (ModifyPassword.length < 10 || ModifyPassword.length > 20){
        alert("비밀번호의 길이를 확인하십시오.");
    } else if (!chk) {
        alert("비밀번호는 문자+숫자로 이루어져야 합니다.");
    } else {
        try {
            const response = await fetch('/profile/changepassword', {
                method:'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify({
                    ModifyPassword : ModifyPassword
                }),
            });
            if(!response.ok) {
                throw new Error("서버 응답이 실패했습니다");
            }
            const flag = await response.text();
            if(flag=='1') {
                alert("비밀번호가 변경 되었습니다.");
                location.reload();
            } else {
                alert("에러" + error.message);
            }
        } catch (error) {
            console.error("Error: " + error);
        }
    }

    document.getElementById("modifypassword").value="";
    document.getElementById("checkpassword").value="";
}