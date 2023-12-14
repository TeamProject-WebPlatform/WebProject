package platform.game.model.TO;

import lombok.Data;

@Data
public class UserSignTO {
    private String id;
    private String password;
    private String nickname;
    private String email = "test1@navet.com";   //이메일 주소 받아오기
}
