package platform.game.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import platform.game.model.Authorization.LoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private LoginService loginService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll()
                .requestMatchers("/login_admin").permitAll()
                .requestMatchers("/superadmin","/admin").hasRole("SUPERADMIN")
                .requestMatchers("/admin").hasRole("ADMIN")
        );
        http.formLogin(login -> login
                .loginPage("/login_admin")
                .defaultSuccessUrl("/admin")
                .usernameParameter("id"));
                
        http.logout(logout-> logout
                .logoutSuccessUrl("/login_admin")
                .invalidateHttpSession(true)); // 세션 날리기

        http.exceptionHandling(handling -> handling
                .accessDeniedPage("/accessDenied"));

        http.userDetailsService(loginService);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
