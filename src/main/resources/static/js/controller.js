const setPage = async function (page) {
    switch (page) {
        case "main":
            // 메인 페이지
            await fetch('/getMainFragment')
                    .then(response => response.text())
                    .then(html => {
                        document.getElementById('content').innerHTML = html;
                    })       
                    .catch(error => console.error('컨트롤러 main 에러:', error));
            setSwiperWrapper();
            setSwiper();
            
            break;
        case 1:
            // 공지사항 게시판
            break;
        case 2:
            // 대전 게시판
            break;
        case 3:
            // 협동 게시판
            break;
        case 4:
            // 게시판
            break;
        case 5:
            // 랭킹
            break;
        case 6:
            // 회원 정보 관리
            break;
        case 7:
            // 프로필
            break;
        case 8:
            // 아이템 샵
            break;
    }
}