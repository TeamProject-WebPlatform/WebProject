package platform.game.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class Token {

    // 테스트용 jwt 생성 코드
    public String createToken(String id, String password, String nickname) {
        // header
        Claims header = Jwts.claims();
        header.put("alg", "HS256");

        // payload
        Claims payload = Jwts.claims();
        payload.put("id", id);
        payload.put("password", password);
        payload.put("nickname", nickname);

        Long expiredTime = 1000 * 60L * 60L; // 토큰 유효시간 1시간 임시 설정

        Date date = new Date();
        date.setTime(date.getTime() + expiredTime); // 토큰 만료시간 설정

        Key key = Keys // 시크릿 키 추후 env.properties에 설정 필요
                .hmacShaKeyFor("asdlfkjsalkdfjlskadjflksjdlksajdflksajdlkfajsldkfj".getBytes(StandardCharsets.UTF_8));

        // 토큰 builder
        String jwt = Jwts.builder()
                .setHeader(header)
                .setClaims(payload)
                .setSubject("test")
                .setExpiration(date)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // return
        return jwt;
    }

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode("asdlfkjsalkdfjlskadjflksjdlksajdflksajdlkfajsldkfj");
        return Keys.hmacShaKeyFor(key);
    }

    // jwt 특정 값 리턴 테스트용 코드
    public void extractToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        System.out.println(decodedJWT.getClaim("password").toString());
    }
}