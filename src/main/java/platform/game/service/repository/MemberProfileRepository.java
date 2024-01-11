package platform.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.MemberProfile;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    MemberProfile findProfileIntroByMemId(Long memId);
}
