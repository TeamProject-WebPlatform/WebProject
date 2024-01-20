package platform.game.service.model.TO;

import lombok.Data;

@Data
public class UserSignTO {
    private String memUserid;
    private Long memId;
    private String memPw;
    private String memNick;
    private String memEmail;
    private String memSteamid;
    private String memKakaoid;
}
