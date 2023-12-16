
document.addEventListener('DOMContentLoaded', function(){
    const inputNickname = document.getElementById('nickname');
    const inputEmail = document.getElementById('email');
    document.getElementById( 'btn-email' ).addEventListener('click', async function (e) {
        if( document.frmLogin.email.value.trim() == '' ) {
        // if( inputEmail.value.trim() == '' ) {
            alert( '이메일을 입력해주세요.' );
            return;
        }else{
            alert('인증번호를 전송하였습니다.');
            inputEmail.readOnly=true;
            inputEmail.style.backgroundColor = 'rgb(192, 192, 192)';
            document.getElementById('mail_number').style.display="block";

            e.preventDefault();
            try {
                const response = await fetch(`/login/mail_ok`,{
                    method:'POST',
                    headers:{
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        nickname: inputNickname.value,
                        email: inputEmail.value,
                    })
                });
                if (!response.ok) {
                    throw new Error('서버 응답이 실패했습니다.');
                }
                const flag = await response.json();
                switch (flag){
                    case 1:
                        console.log("닉네임 없음");
                        break;
                    case 2:
                        console.log("이메일 없음");
                        break;
                    case 0:
                        window.location.href = "/";
                        break;
                    default:
                        break;
                }
            } catch (error) {
                console.error('DB 호출 중 오류가 발생했습니다.', error);
            };
        }
    });
    const confirmNumber = function(){
        const number1 = document.getElementById('Confirm').value;
        const number2 = docmentu.getElementById('number').value;
        if(number1 == number2){
            alert("인증되었습니다.");
        }else{
            alert("번호가 다릅니다.");
        }
    };
    
});