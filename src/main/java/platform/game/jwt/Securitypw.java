package platform.game.jwt;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Assert;

@Service
public class Securitypw implements PasswordEncoder {

    // password를 받아 암호화 하는 메소드
    @Override
    public String encode(CharSequence rawPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }

    // 암호화된 password와 입력된 password를 비교하는 메소드 / 추후 추가 예정
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
