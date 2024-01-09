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
            await setSwiperWrapper();
            setSwiper();
            break;
        case "notice": boardCd = "20001";
        case "board": if(!boardCd) boardCd = "20004";
            // 자유 게시판
            //DOM + 백엔드에서 변수받음 
            await fetch('/getBoardListFragment?board_cd='+boardCd)
                    .then(response => response.text())
                    .then(html => {
                        document.getElementById('center_main').innerHTML = html;
                    })       
                    .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;

        case 'fight':
            // 대전 게시판
            
            break;
        case 'team':
            // 협동 게시판
            break;
        case 4:
            // 게시판
            break;
        case "rank": boardCd = "20005";
            // 랭킹
            await fetch('/getRankFragment?board_cd=' + boardCd)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                    WinRateChart();
                    PointChart();
                    LevelChart();
                })
                .catch(error => console.log('컨트롤러 랭킹 에러'));
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

const setBoardListPage = async function (page, boardCd){
    switch (page) {
        case "board_list":
            await fetch('/getBoardListFragment?board_cd='+ boardCd)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardViewPage = async function (page, postId){
    switch (page) {
        case "board_view":
            await fetch('/getBoardViewFragment?post_id='+ postId)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardWritePage = async function (page, boardCd){
    switch (page) {
        case "board_write":
            await fetch('/getBoardWriteFragment?board_cd='+ boardCd)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardWriteOkPage = async function (page, boardCd, WboardCd, subject, tags, content){
    switch (page) {
        case "board_write_ok":
            await fetch('/getBoardWrite_okFragment?board_cd=' + boardCd + '&Wboard_cd=' + WboardCd + '&subject=' + subject + '&tags=' + tags + '&content=' + content)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardModifyPage = async function (page, postId){
    switch (page) {
        case "board_modify":
            await fetch('/getBoardModifyFragment?post_id='+ postId)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardModifyOkPage = async function (page, postId, subject, tags, content){
    switch (page) {
        case "board_modify_ok":
            await fetch('/getBoardModify_okFragment?&post_id=' + postId + '&subject=' + subject + '&tags=' + tags + '&content=' + content)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardDeletePage = async function (page, postId){
    switch (page) {
        case "board_delete":
            await fetch('/getBoardDeleteFragment?post_id='+ postId)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardDeleteOkPage = async function (page, boardCd, postId){
    switch (page) {
        case "board_delete_ok":
            await fetch('/getBoardDelete_okFragment?board_cd='+ boardCd +'&post_id=' + postId)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardCommentOkPage = async function (page, postId, content){
    switch (page) {
        case "board_comment_ok":
            await fetch('/getBoardComment_okFragment?post_id='+ postId + '&content=' + content)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}

const setBoardCommentDeleteOkPage = async function (page, postId, commentId){
    switch (page) {
        case "board_comment_delete_ok":
            await fetch('/getBoardCommentDelete_okFragment?post_id='+ postId + '&comment_id=' + commentId)
                .then(response => response.text())
                .then(html => {
                    document.getElementById('center_main').innerHTML = html;
                })       
                .catch(error => console.error('컨트롤러 공지사항 에러:', error));
            break;
    }
}