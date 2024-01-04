package platform.game.service.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import platform.game.service.entity.Member;

public class MemberInfoDetails implements UserDetails { 

    private Member member;
    private String mem_userid; 
    private String mem_pw; 
    private String mem_role_cd;
    private List<GrantedAuthority> authorities; 
  
    public MemberInfoDetails(Member member) { 
        this.member = member;
        
        mem_userid = member.getMemUserid(); 
        mem_pw = member.getMemPw(); 
        mem_role_cd = member.getMemRoleCd();
        /*
         * 10001 : ROLE_SUPERADMIN
         * 10002 : ROLE_ADMIN
         * 10003 : ROLE_USER
         */
        String role = member.getRole(mem_role_cd);
        authorities = Arrays.stream(role.split(",")) 
                .map(SimpleGrantedAuthority::new) 
                .collect(Collectors.toList()); 
    } 
  
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { 
        return authorities; 
    } 

    @Override
    public String getPassword() { 
        return mem_pw; 
    } 
  
    @Override
    public String getUsername() { 
        return mem_userid; 
    } 
  
    @Override
    public boolean isAccountNonExpired() { 
        return true; 
    } 
  
    @Override
    public boolean isAccountNonLocked() { 
        return true; 
    } 
  
    @Override
    public boolean isCredentialsNonExpired() { 
        return true; 
    } 
  
    @Override
    public boolean isEnabled() { 
        return true; 
    } 
    public Member getMember() {
        return member;
    }
}