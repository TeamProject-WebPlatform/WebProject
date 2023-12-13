package platform.game.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class CreateToken {

    public void createToken(String id, String password, String nickname) {
        // header
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        // payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("password", password);
        payload.put("nickname", nickname);

        Long expiredTime = 1000 * 60L * 60L; // 토큰 유효시간 1시간 임시 설정

        Date date = new Date();
        date.setTime(date.getTime() + expiredTime); // 토큰 만료시간 설정

        Key key = Keys
                .hmacShaKeyFor("asdlfkjsalkdfjlskadjflksjdlksajdflksajdlkfajsldkfj".getBytes(StandardCharsets.UTF_8));

        // 토큰 builder
        String jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .setSubject("test")
                .setExpiration(date)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // return
        System.out.println(jwt);
    }
}