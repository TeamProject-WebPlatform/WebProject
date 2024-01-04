package platform.game.service.model.TO.KakaoTO;

import lombok.Data;

//카카오톡 토큰 사용을 위한 TO
@Data
public class KakaoOAuthTokenTO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String id_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

}
