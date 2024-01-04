package platform.game.service.service.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
@ComponentScan(basePackages = { "platform.game.env.config" })
public class JwtManager {
    Key key = null;
    Long lifetime = 1000 * 60 * 60L; // 토큰 유효시간 1시간

    public JwtManager(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String id, String password) {
        // header
        Claims header = Jwts.claims();
        header.put("alg", "HS256");
        // payload
        Claims payload = Jwts.claims();
        payload.put("id", id);
        payload.put("password", password);
        payload.put("role", "USER");
        // 유효기간
        Long expiredTime = lifetime;

        Date date = new Date();
        date.setTime(date.getTime() + expiredTime); // 토큰 만료시간 설정

        // 토큰 builder
        String jwt = Jwts.builder()
                .setHeader(header)
                .setClaims(payload)
                .setSubject("test")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }

    // jwt 특정 값 리턴 테스트용 코드
    public String extractToken(String tag, String token) {
        // DecodedJWT decodedJWT = JWT.decode(token);
        // return decodedJWT.getClaim("password").toString();
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(tag).toString();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;

    }
}