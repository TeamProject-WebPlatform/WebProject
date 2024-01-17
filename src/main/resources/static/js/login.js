const checkDuplication = async function () {
    // 중복체크
    let timeoutUsername;
    let timeoutNickname;
    let timeoutEmail;

    const usernameInput = document.getElementById('username');
    const nicknameInput = document.getElementById('nickname');
    const emailInput = document.getElementById('email');

    const usernameAlert = document.getElementById('usernameAlert');
    const nicknameAlert = document.getElementById('nicknameAlert');
    const emailAlert = document.getElementById('emailAlert');

    // 입력 이벤트를 감지하여 1초 후에 중복 체크를 실행
    usernameInput.addEventListener('input', function () {
        clearTimeout(timeoutUsername);

        timeoutUsername = setTimeout(async function () {
            if(verifyIdContraints()) await checkUsernameDuplication(usernameInput.value);
        }, 1000);
    });
    nicknameInput.addEventListener('input', function () {
        clearTimeout(timeoutNickname);

        timeoutNickname = setTimeout(async function () {
            if(verifyNicknameContraints()) await checkNicknameDuplication(nicknameInput.value);
        }, 1000);
    });
    emailInput.addEventListener('input', function () {
        clearTimeout(timeoutEmail);

        timeoutEmail = setTimeout(async function () {
            if(verifyEmailContraints()) await checkEmailDuplication(emailInput.value);
        }, 1000);
    });
    async function checkUsernameDuplication(username) {
        try {
            const response = await fetch(`/login/DuplicateIdCheck`,{
                method:'POST',
                body: usernameInput.value
                });
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.');
            }
            const flag = await response.json();
            if (flag){
                usernameAlert.innerHTML = "USERNAME 중복"
            } else {
                usernameAlert.innerHTML = ""
                document.getElementById('username').setAttribute("pass","true");
            }
        } catch (error) {
            console.error('DB 호출 중 오류가 발생했습니다.', error);
        }
    }
    async function checkNicknameDuplication(nickname){
        try {
            const response = await fetch(`/login/DuplicateNickCheck`,{
                method:'POST',
                body: nicknameInput.value
                });
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.');
            }
            const flag = await response.json();
            if (flag){
                nicknameAlert.innerHTML = "NICKNAME 중복"
            } else {
                nicknameAlert.innerHTML = ""
                document.getElementById('nickname').setAttribute("pass","true");
            }
        } catch (error) {
            console.error('DB 호출 중 오류가 발생했습니다.', error);
        }
    }
    async function checkEmailDuplication(nickname){
        try {
            const response = await fetch(`/login/DuplicateMailCheck`,{
                method:'POST',
                body: emailInput.value
                });
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.');
            }
            const flag = await response.json();
            if (flag){
                emailAlert.innerHTML = "EMAIL 중복"
            } else {
                emailAlert.innerHTML = ""
                document.getElementById('email').setAttribute("pass","true");
            }
        } catch (error) {
            console.error('DB 호출 중 오류가 발생했습니다.', error);
        }
    }
}


const openSignupForm = async function () {
    const signin = document.querySelectorAll(".sign")[0];
    const signup = document.querySelectorAll(".sign")[1];
    const i_transition = document.querySelectorAll("section .sign .content .form .inputBox i");

    i_transition.forEach(function (x) {
        x.style.transition = "0s";
    })

    signin.style.visibility = "hidden";
    signup.style.visibility = "visible";
    await sleep(0); // 비동기 처리
    i_transition.forEach(function (x) {
        x.style.transition = "0.5s";
    });
    clearInput();

    const logo = document.querySelector(".logo");
    logo.style.transform = `translate(0, -50px)`;
}
const openSigninForm = async function () {
    const signin = document.querySelectorAll(".sign")[0];
    const signup = document.querySelectorAll(".sign")[1];
    const i_transition = document.querySelectorAll("section .sign .content .form .inputBox i");

    i_transition.forEach(function (x) {
        x.style.transition = "0s";
    })

    signup.style.visibility = "hidden";
    signin.style.visibility = "visible";
    await sleep(0); // 비동기 처리
    i_transition.forEach(function (x) {
        x.style.transition = "0.5s";
    });
    clearInput();
    
    const logo = document.querySelector(".logo");
    logo.style.transform = `translate(0, 0px)`;
}
const clearInput = function () {
    const input = document.querySelectorAll("input[clear='true']");
    input.forEach(function (x) {
        x.value = "";
    })
}
const comparePassword = function () {
    const password = document.getElementById('password').value;
    const passwordCheck = document.getElementById('passwordCheck').value;
    const passwordCheckLabel = document.getElementById('passwordCheckLabel');

    if (passwordCheck === '') {
        passwordCheckLabel.style.color = "#aaa";
        return false;
    }

    let check = (password === passwordCheck);
    if (!check) {
        passwordCheckLabel.style.color = "red";
    } else {
        passwordCheckLabel.style.color = "green";
    }
    return check;
}
const verifyIdContraints = function () {
    // 회원가입 시 아이디 조건체크
    const username = document.getElementById('username').value;
    const usernameLabel = document.getElementById('usernameLabel');
    const usernameAlert = document.getElementById('usernameAlert');
    usernameAlert.innerHTML = "";
    if (username === '') {
        usernameLabel.style.color = "#aaa";
        return false;
    }

    if (username.search(/\W|\s/g) > -1) {
        usernameLabel.style.color = "red";
        return false;
    }

    if (username.length < 8 || username.length > 15) {
        usernameLabel.style.color = "red";
        return false;
    }

    usernameLabel.style.color = "green";

    return true;
}
const verifyPasswordContraints = function () {
    // 회원가입 시 비밀번호 조건 체크
    const password = document.getElementById('password').value;
    const passwordLabel = document.getElementById('passwordLabel');

    let chk = false; // 숫자 + 문자의 조합인지
    let haveNum = false;
    let haveChar = false;

    if (password === '') {
        passwordLabel.style.color = "#aaa";
        return false;
    }

    if (password.search(/\W|\s/g) > -1) {
        passwordLabel.style.color = "red";
        return false;
    }

    if (password.length < 10 || password.length > 20) {
        passwordLabel.style.color = "red";
        return false;
    }

    for (let i = 0; i < password.length; i++) {
        if (haveNum && haveChar) {
            chk = true;
            break;
        }
        if (!isNaN(password[i])) {
            haveNum = true;
        } else {
            haveChar = true;
        }
    }
    if (haveNum && haveChar) {
        chk = true;
    }
    if (!chk) {
        passwordLabel.style.color = "red";
        return false;
    }
    passwordLabel.style.color = "green";
    return true
};
const verifyNicknameContraints = function () {
    // 회원가입 시 닉네임 조건 체크
    const nickname = document.getElementById('nickname').value;
    const nicknameLabel = document.getElementById('nicknameLabel');
    const nicknameAlert = document.getElementById('nicknameAlert');
    nicknameAlert.innerHTML = "";

    if (nickname == '') {
        nicknameLabel.style.color = "#aaa";
        return false;
    }
    if (nickname.search(/\W|\s/g) > -1) {
        nicknameLabel.style.color = "red";
        return false;
    }
    if (nickname.length < 4 || nickname.length > 8) {
        nicknameLabel.style.color = "red";
        return false;
    }
    nicknameLabel.style.color = "green";
    return true;
};
const verifyEmailContraints = function () {
    // 회원가입 시 이메일 조건 체크
    const email = document.getElementById('email').value;
    const emailLabel = document.getElementById('emailLabel');
    const emailAlert = document.getElementById('emailAlert');
    emailAlert.innerHTML = "";
    if (email == '') {
        emailLabel.style.color = "#aaa";
        return false;
    }
    var emailArr = email.split('@');
    if (!emailArr[1] || !emailArr[1].split('.')[1]) {
        emailLabel.style.color = "red";
        return false;
    }
    emailLabel.style.color = "green";
    return true;
};
const verifyEmailCode = function () {
    const emailCode = document.getElementById('emailCode').value;
    const emailLabel = document.getElementById('emailCodeLabel');
    if (emailCheckNumber !== 0 && emailCheckNumber !== -1) {
        if (Number(emailCode) === emailCheckNumber) {
            emailLabel.style.color = "green";
            return true;
        }
    }

    emailLabel.style.color = "red";
    return false;
};
const flash = async function (object) {
    object.classList.add('flashing');
    await sleep(500);
    object.classList.remove('flashing');
}
const signup = async function () {
    const btn = document.getElementById('signupBtn');
    btn.addEventListener('click', async function (e) {
        e.preventDefault();
    })

    if (btn.getAttribute("state") === '0') {
        // 인증 번호 보내기
        const email = document.getElementById('email');
        const emailLabel = document.getElementById('emailLabel');
        if (email.getAttribute('pass')==='false') return;
        if (!verifyEmailContraints()) {
            flash(emailLabel);
        } else {
            // 이메일에 코드 보내기
            btn.value = "⚫ ⚫ ⚫";
            try {
                const response = await fetch(`/login/mail_ok`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        memEmail: email.value,
                    })
                });
                if (!response.ok) {
                    throw new Error('서버 응답이 실패했습니다.');
                }
                const number = await response.json();
                if (number === -1) {
                    // 에러뜬거
                }
                emailCheckNumber = number;
                console.log(number);
                btn.setAttribute("state", '1');
                btn.value = "회원가입";
            } catch (error) {
                console.error('DB 호출 중 오류가 발생했습니다.', error);
            };
        }
    } else if (btn.getAttribute("state") === '1') {
        let success = true;
        if (!verifyIdContraints()) {
            success = false;
            flash(document.getElementById('usernameLabel'));
        }

        if (!verifyPasswordContraints()) {
            success = false;
            flash(document.getElementById('passwordLabel'));
        }

        if (!comparePassword()) {
            success = false;
            flash(document.getElementById('passwordCheckLabel'));
        }

        if (!verifyNicknameContraints()) {
            success = false;
            flash(document.getElementById('nicknameLabel'));
        }

        if (!verifyEmailCode()) {
            success = false;
            flash(document.getElementById('emailLabel'));
        }

        if (!success) return;
        if(document.getElementById('username').getAttribute('pass')==='false') return;
        if(document.getElementById('nickname').getAttribute('pass')==='false') return;
        if(document.getElementById('email').getAttribute('pass')==='false') return;

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const nickname = document.getElementById('nickname').value;
        const email = document.getElementById('email').value;

        try {
            const response = await fetch(`/login/signup_ok`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    memUserid: username,
                    memPw: password,
                    memNick: nickname,
                    memEmail: email
                })
            });
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.');
            }
            const flag = await response.json();
            switch (flag) {
                case 0:
                    window.location.href = "/";
                    break;
                default:
                    window.location.href = "/";
                    break;
            }
        } catch (error) {
            console.error('DB 호출 중 오류가 발생했습니다.', error);
        }
    }

}
const signin = async function(){
    let username = document.getElementById('usernameLogin').value;
    let password = document.getElementById('passwordLogin').value;
    try {
        const response = await fetch(`/login/generateToken`,{
            method:'POST',
            headers:{
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                memUserid: username,
                memPw: password,
            })
        });
        if (!response.ok) {
            throw new Error('서버 응답이 실패했습니다.');
        }
        const flag = await response.json();
        switch (flag){
            case 1: 
                alert('아이디 또는 비번 오류');
                // check_recaptcha();
                break;
            case 0:
                history.go(-1);
                break;
            default:
                break;
        }
    } catch (error) {
        console.error('DB 호출 중 오류가 발생했습니다.', error);
    }
}    
function check_recaptcha(){
    loginCnt+=1;
    var Captcha_Check = grecaptcha.getResponse();
    if (Captcha_Check.length == 0) {
        if (loginCnt ==3){
            document.getElementById("captcha").style.display="";
        } else if (loginCnt > 3){
            alert("Please complete the reCAPTCHA!");
            return;
        }
    }
    else{
        alert("인증 성공");
        window.location.href = "./";
    }
}
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));
