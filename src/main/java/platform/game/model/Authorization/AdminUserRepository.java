package platform.game.model.Authorization;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser,Long>{
    Optional<AdminUser> findById(String id);
}
