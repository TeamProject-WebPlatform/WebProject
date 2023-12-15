package platform.game.model.Authorization;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService{
    
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        System.out.println("11111111111111111111111111실행");
        Optional<AdminUser> adminUser = adminUserRepository.findById(id);
        System.out.println("2222222222222222222222222실행");
        if(adminUser.isPresent()){
            AdminUser admin = adminUser.get();

            AdminUser authAdmin = AdminUser.builder()
                    .admin_id(admin.getAdmin_id())
                    .id(admin.getId())
                    .password(admin.getPassword())
                    .role(admin.getRole())
                    .build();
            return authAdmin;
        }
        return null;
    }

}
