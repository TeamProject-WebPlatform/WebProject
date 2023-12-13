package platform.game.jwt.interfaces;

// 패스워드 암호화 인터페이스
public interface PasswordEncoder {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence var1, String var2);
}
