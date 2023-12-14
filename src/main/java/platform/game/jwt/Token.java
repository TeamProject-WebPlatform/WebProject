package platform.game.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class Token {

    @Value("${SecretKey}")
    private String secretKey;

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

        Long expiredTime = 1000 * 60L * 60L; // 토큰 유효시간 1시간

        String keyBase64encoded = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        SecretKey secretkey = Keys.hmacShaKeyFor(keyBase64encoded.getBytes(StandardCharsets.UTF_8));

        // 토큰 builder
        String jwt = Jwts.builder()
                .setHeader(header)
                .setClaims(payload)
                .setSubject("test")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    // jwt 특정 값 리턴 테스트용 코드
    public String extractToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("password").toString();
    }

    // 토큰 만료 여부 확인 ( true = 만료 )
    public boolean isValidToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}