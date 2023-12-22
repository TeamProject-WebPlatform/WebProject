package platform.game.service.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import platform.game.service.service.MemberInfoService;
import platform.game.service.service.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService; 
    @Autowired
    private MemberInfoService userDetailsService; 

    private String userid = null; 
    private String pw = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); 
        String token = null; 
        // MEMBER 엔티티와의 일관성을 위해 userid, pw로 변수명 변경
        // String userid = null; 
        // String pw = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtTokenCookie".equals(cookie.getName())) {
                    // String cookieValue = cookie.getValue();
                    token = cookie.getValue();
                    this.userid = jwtService.extractUsername(token); 
                    this.pw = jwtService.extractPassword(token);

                    // Cookie에서 ID, PW 불러오기
                    // System.out.println("ID: " + username);
                    // System.out.println("PW: " + password);
                }
            }
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) { 
            //System.out.println("jwt 필터 / 1 : 토큰 있음 추출");
            token = authHeader.substring(7); 
            userid = jwtService.extractUsername(token); 
        } 
  
        if (userid != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
            //System.out.println("jwt 필터 / 2 : 유저 디테일 설정");
            UserDetails userDetails = userDetailsService.loadUserByUsername(userid); 
            if (jwtService.validateToken(token, userDetails)) { 
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 
                SecurityContextHolder.getContext().setAuthentication(authToken); 
            } 
        } 

        filterChain.doFilter(request, response);   
    }

    public String getUserID() {
        return this.userid;
    }

    public String getPW() {
        return this.pw;
    }
    
}