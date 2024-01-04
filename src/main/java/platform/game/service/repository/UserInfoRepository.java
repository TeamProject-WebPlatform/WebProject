package platform.game.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.UserInfo;

@Repository
public interface  UserInfoRepository extends JpaRepository<UserInfo, Integer>{
    Optional<UserInfo> findByName(String username);
}
