// document.addEventListener('DOMContentLoaded', function(){
//     const inputNickname = document.getElementById('nickname');
//     const inputEmail = document.getElementById('email');
//     const inputId = document.getElementById('id');
//     const duplicateId = document.getElementById('duplicate-id');
//     const duplicateNickname = document.getElementById('duplicate-nickname');
//     const duplicateMail = document.getElementById('duplicate-mail');
//     var d_id;
//     var d_nickname;
//     var d_mail;

//     document.getElementById( 'btn-email' ).addEventListener('click', async function (e) {
//         if( document.frmLogin.email.value.trim() == '' ) {
//         // if( inputEmail.value.trim() == '' ) {
//             alert( '이메일을 입력해주세요.' );
//             return;
//         }else{
//             alert('인증번호를 전송하였습니다.');
//             inputEmail.readOnly=true;
//             inputEmail.style.backgroundColor = 'rgb(192, 192, 192)';
//             document.getElementById('mail_number').style.display="block";
            
//             e.preventDefault();
//             try {
//                 const response = await fetch(`/login/mail_ok`,{
//                     method:'POST',
//                     headers:{
//                         'Content-Type': 'application/json',
//                     },
//                     body: JSON.stringify({
//                         memNick: inputNickname.value,
//                         memEmail: inputEmail.value,
//                     })
//                 });
//                 if (!response.ok) {
//                     throw new Error('서버 응답이 실패했습니다.');
//                 }
//                 const number = await response.json();
//                 document.getElementById('confirm').value = number;
//                 switch (number){
//                     case 1:
//                         console.log("닉네임 없음");
//                         break;
//                     case 2:
//                         console.log("이메일 없음");
//                         break;
//                     case 0:
//                         window.location.href = "/";
//                         break;
//                     default:
//                         break;
//                 }
//             } catch (error) {
//                 console.error('DB 호출 중 오류가 발생했습니다.', error);
//             };
//         }
//     });
    
//     document.getElementById( 'confirmBtn' ).addEventListener('click', function () {
//         if( document.frmLogin.number.value.trim() == '' ) {
//             // if( inputEmail.value.trim() == '' ) {
//                 alert( '인증번호를 입력하세요.' );
//                 return;
//         }else if(document.frmLogin.number.value.trim() == document.frmLogin.confirm.value.trim()){
//             alert('인증되었습니다.');
//             document.getElementById('btn-signup').disabled = false;
//             return;
//         }else{
//             alert('번호가 다릅니다.');
//             return;
//         }
//     });

//     // ID 중복 체크
//     async function duplicateIdCheck(){
//         var checkId = false;
//         try {
//             const response = await fetch(`/login/DuplicateIdCheck`,{
//                 method:'POST',
//                 body: inputId.value
//                 });
//             if (!response.ok) {
//                 throw new Error('서버 응답이 실패했습니다.');
//             }
//             const flag = await response.json();
//             if (flag){
//                 alert("이미 존재하는 아이디입니다.");
//                 checkId = false;
//                 return checkId;
//             } else {
//                 alert("사용 가능한 아이디입니다.");
//                 checkId = true;
//                 return checkId;
//             }
//         } catch (error) {
//             console.error('DB 호출 중 오류가 발생했습니다.', error);
//         }
//     }

//     // 닉네임 중복 체크
//     async function duplicateNicknameCheck(){
//         var checkNick = false;
//         try {
//             const response = await fetch(`/login/DuplicateNickCheck`,{
//                 method:'POST',
//                 body: inputNickname.value
//                 });
//             if (!response.ok) {
//                 throw new Error('서버 응답이 실패했습니다.');
//             }
//             const flag = await response.json();
//             if (flag){
//                 alert("이미 존재하는 닉네임입니다.");
//                 checkNick = false;
//                 return checkNick;
//             } else {
//                 alert("사용 가능한 닉네임입니다.");
//                 checkNick = true;
//                 return checkNick;
//             }
//         } catch (error) {
//             console.error('DB 호출 중 오류가 발생했습니다.', error);
//         }
//     }

//     // 메일 중복 체크
//     async function duplicateMailCheck(){
//         var checkMail = false;
//         try {
//             const response = await fetch(`/login/DuplicateMailCheck`,{
//                 method:'POST',
//                 body: inputEmail.value
//                 });
//             if (!response.ok) {
//                 throw new Error('서버 응답이 실패했습니다.');
//             }
//             const flag = await response.json();
//             if (flag){
//                 alert("이미 가입된 메일주소 입니다.");
//                 checkMail = false;
//                 return checkMail;
//             } else {
//                 alert("사용 가능한 메일주소 입니다.");
//                 checkMail = true;
//                 return checkMail;
//             }
//         } catch (error) {
//             console.error('DB 호출 중 오류가 발생했습니다.', error);
//         }
//     };

//     duplicateId.onclick = async function(){
//         duplicateIdCheck().then(checkId => {
//             d_id = checkId;
//         });
//     }

//     duplicateNickname.onclick = async function(){
//         duplicateNicknameCheck().then(checkNick => {
//             d_nickname = checkNick;
//         });
//     }

//     duplicateMail.onclick = async function(){
//         duplicateMailCheck().then(checkMail => {
//             d_mail = checkMail;
//             if(d_mail){
//                 document.getElementById( 'btn-email' ).disabled = false;
//             } else {
//                 document.getElementById( 'btn-email' ).disabled = true;
//             }
//         });
//     }
// });


