document.getElementById( 'btn-email' ).onclick = function() {
    if( document.getElementById('number').value === '' ) {
        alert( '비밀번호를 입력하셔야 합니다.' );
        return;
    }else{
        alert( '비밀번호를 입력하셔야 합니다.' );
        document.getElementById('mail_number').style.display = "block";
    }
};