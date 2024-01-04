package platform.game.service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import platform.game.service.entity.Member;
import platform.game.service.repository.MemberInfoRepository;

@Service
public class MemberInfoService implements UserDetailsService{
    @Autowired
    private MemberInfoRepository repository; 
  
    @Autowired
    private PasswordEncoder encoder; 

    @Override
    public UserDetails loadUserByUsername(String mem_userid) throws UsernameNotFoundException { 
  
        Optional<Member> memberDetail = repository.findByMemUserid(mem_userid); 
  
        // Converting userDetail to UserDetails 
        return memberDetail.map(MemberInfoDetails::new) 
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + mem_userid)); 
    } 
  
    public boolean addUser(Member member) { 
        try{
            member.setMemPw(encoder.encode(member.getMemPw())); 
            repository.save(member); 
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
