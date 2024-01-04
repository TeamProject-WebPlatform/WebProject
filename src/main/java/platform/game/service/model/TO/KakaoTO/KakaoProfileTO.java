package platform.game.service.model.TO.KakaoTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KakaoProfileTO {

    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public class Properties {
        public String nickname;
    }

    @Data
    public static class KakaoAccount{

        @JsonProperty("profile_nickname_needs_agreement")
        public Boolean profileNicknameNeedsAgreement;
        public Profile profile;
        public Boolean hasEmail;
        public Boolean emailNeedsAgreement;
        public Boolean isEmailValid;
        public Boolean isEmailVerified;
        public String email;

        class Profile {
            public String nickname;
        }
    };
    
    public KakaoAccount getKakaoAccount() {
        return this.kakao_account;
    };
}