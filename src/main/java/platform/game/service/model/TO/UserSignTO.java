package platform.game.service.model.TO;

import lombok.Data;

@Data
public class UserSignTO {
    private String id;
    private String password;
    private String nickname;
    private String email;

}
