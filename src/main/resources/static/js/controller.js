const setPage = async function (page) {
    let boardCd="" 
    switch (page) {
        case "main":
            // 메인 페이지

            //DOM + 백엔드에서 변수받음 
            await fetch('/getMainFragment')
                    .then(response => response.text())
                    .then(html => {
                        document.getElementById('center_main').innerHTML = html;
                    })       
                    .catch(error => console.error('컨트롤러 메인 에러:', error));
            // DOM에 받은 변수로 세팅
            setSwiperWrapper();
            setSwiper();
            
            break;
        case "notice": boardCd = "20001";
        case "board": if(!boardCd) boardCd = "20004";
            // 공지사항 게시판
            //DOM + 백엔드에서 변수받음 
            await fetch('/getBoardListFragment?board_cd='+boardCd)
                    .then(response => response.text())
                    .then(html => {
                        document.getElementById('center_main').innerHTML = html;
                    })       
                    .catch(error => console.error('컨트롤러 공지사항 에러:', error));
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