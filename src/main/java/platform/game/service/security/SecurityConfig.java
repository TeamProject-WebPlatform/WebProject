package platform.game.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import platform.game.service.filter.JwtAuthFilter;
import platform.game.service.service.MemberInfoService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter; 
    // User Creation 
    @Bean
    public UserDetailsService userDetailsService() { 
        return new MemberInfoService(); 
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
            .requestMatchers("/").permitAll()
            .requestMatchers("/login/**").permitAll()
            .requestMatchers("/board/**").permitAll()
            .requestMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken").permitAll()
            .requestMatchers("/auth/user/**").authenticated()
            .requestMatchers("/auth/admin/**").authenticated()
            .requestMatchers("/mypage/**").authenticated()
        );
        
        http.formLogin(form -> form
            .loginPage("/login")  // 사용자가 인증되지 않은 경우 /login 페이지로 이동
            .permitAll()
        );

        http.sessionManagement(management -> management
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Password Encoding 
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    } 
  
    @Bean
    public AuthenticationProvider authenticationProvider() { 
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
        authenticationProvider.setUserDetailsService(userDetailsService()); 
        authenticationProvider.setPasswordEncoder(passwordEncoder()); 
        return authenticationProvider; 
    } 
  
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
        return config.getAuthenticationManager(); 
    } 

}
