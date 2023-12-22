document.addEventListener('DOMContentLoaded', function(){
    // 쿠키에서 토큰 값을 가져오는 함수
    function getCookie(cookieName) {
        const name = cookieName + "=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const cookieArray = decodedCookie.split(';');
    
        for (let i = 0; i < cookieArray.length; i++) {
            let cookie = cookieArray[i].trim();
    
            if (cookie.indexOf(name) === 0) {
                return cookie.substring(name.length, cookie.length);
            }
        }   
        return null;
    }

    // 토큰 값을 가져와서 사용하는 예제
    const token = getCookie('jwtTokenCookie');

    if (token) {
        // 토큰이 존재하면 원하는 작업을 수행
        console.log('Token:', token);
        document.getElementsByClassName("log")[0].style.display="none";
        document.getElementsByClassName("logto")[0].style.display = "block";
    } else {
        // 토큰이 존재하지 않으면 로그인이 필요한 상태로 처리
        console.log('Token not found. User not logged in.');
    }

    document.getElementsByClassName('logout')[0].addEventListener('click', function () {
        alert('로그아웃 되었습니다.');
        console.log('Deleting cookie...');
        document.cookie = 'jwtTokenCookie=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly';
        console.log('Cookie deleted.');
        document.getElementsByClassName("log")[0].style.display = "block";
        document.getElementsByClassName("logto")[0].style.display = "none";
    });
});
