package platform.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.MemberProfile;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    MemberProfile findProfileIntroByMemId(Long memId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update member_profile set profile_intro=:intro where mem_id=:mem_id", nativeQuery = true)
    Integer IntroduceModify(String intro, long mem_id);
}
