package platform.game.service.model.TO;

import lombok.Data;

@Data
public class UserSignTO {
    private String id;
    private String password;
    private String nickname;
    private String email;
    private int number;
    // 이메일 인증 요청

    public void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }
}
