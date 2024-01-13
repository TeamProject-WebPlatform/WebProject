package platform.game.service.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isTokenExpired = false;

        String token = null;
        String userid = null;
        String pw = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtTokenCookie")) {
                    // String cookieValue = cookie.getValue();
                    try {
                        token = cookie.getValue();
                        userid = jwtService.extractUsername(token);
                        pw = jwtService.extractPassword(token);
                        cookie.setMaxAge(JwtService.JWT_EXPIRY_TIME); // 쿠키 시간 갱신
                    } catch (ExpiredJwtException expiredJwtException) {
                        // JWT가 만료된 경우 예외 처리
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        // response.getWriter().write("JWT is expired");
                        isTokenExpired = true;
                        // authentication 객체 지우기.
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        if (auth != null) {
                            new SecurityContextLogoutHandler().logout(request, response, auth);
                        }
                        response.sendRedirect(request.getContextPath() + "/logout");
                        return;
                    }

                }
            }
        }
        if (!isTokenExpired && userid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // System.out.println("jwt 필터 / 2 : 유저 디테일 설정");
            UserDetails userDetails = userDetailsService.loadUserByUsername(userid);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}