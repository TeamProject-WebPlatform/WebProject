document.addEventListener('DOMContentLoaded', function(){
    const editor = new toastui.Editor({
        el: document.querySelector('#context'),      // 에디터를 적용할 요소 (컨테이너)
        height: '500px',                             // 에디터 영역의 높이 값 (OOOpx || auto)
        initialEditType: 'markdown',                 // 최초로 보여줄 에디터 타입 (markdown || wysiwyg)
        initialValue: '',                            // 내용의 초기 값으로, 반드시 마크다운 문자열 형태여야 함
        previewStyle: 'vertical',                    // 마크다운 프리뷰 스타일 (tab || vertical)
        placeholder: '내용을 입력해 주세요.',
        /* start of hooks */
        hooks: {
            async addImageBlobHook(blob, callback) { // 이미지 업로드 로직 커스텀
                try {
                    /*
                    * 1. 에디터에 업로드한 이미지를 FormData 객체에 저장
                    *    (이때, 컨트롤러 uploadEditorImage 메서드의 파라미터인 'image'와 formData에 append 하는 key('image')값은 동일해야 함)
                    */
                    const formData = new FormData();
                    formData.append('image', blob);
                    
                    // 2. FileApiController - uploadEditorImage 메서드 호출
                    const response = await fetch('/mypage/tui-editor/image-upload', {
                        method : 'POST',
                        body : formData,
                    });

                        // 3. 컨트롤러에서 전달받은 디스크에 저장된 파일명
                        const filename = await response.text();
                        console.log('서버에 저장된 파일명 : ', filename);
                        
                        // 4. addImageBlobHook의 callback 함수를 통해, 디스크에 저장된 이미지를 에디터에 렌더링
                        const imageUrl = `/mypage/tui-editor/image-print?filename=${filename}`;
                        callback(imageUrl, 'image alt attribute');
                        
                    } catch (error) {
                        console.error('업로드 실패 : ', error);
                    }
                }
            }
            /* end of hooks */
        });

    document.getElementById('data').addEventListener('click', function () {
        if (confirm("정보를 수정하시겠습니까?") == true){    //확인
            document.getElementById("qrcode").style.display = "none";
            document.getElementById("content").style.display = "block";
        }else{   //취소
            return false;
        }
    });
    document.getElementById('data_check').addEventListener('click', function () {
        if (confirm("정보를 수정하시겠습니까?") == true){    //확인
            document.getElementById("qrcode").style.display = "block";
            document.getElementById("content").style.display = "none";
            location.reload();
        }else{   //취소
            return false;
        }
    });

    document.getElementById("btn_searchRiotID").addEventListener("click", function() {
        var summonerName = document.getElementById("riotID").value;

        if (summonerName.trim() !== "") {
            callSummonerByName(summonerName);
        } else {
            alert("소환사 이름을 입력하세요.");
        }
    });

    function callSummonerByName(summonerName) {
        summonerName = summonerName.replaceAll(" ","%20");

        $.ajax({
            type: "GET",
            url: "/mypage/summonerByName",
            data: {
                summonerName: summonerName
            },
            success: function (result) {
                // 성공 시 처리
                console.log(result);
            },
            error: function (error) {
                // 실패 시 처리
                console.error(error);
            }
        });
    }
});