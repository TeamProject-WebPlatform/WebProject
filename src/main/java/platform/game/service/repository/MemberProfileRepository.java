package platform.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.MemberProfile;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    MemberProfile findProfileIntroByMemId(Long memId);

    @Query(value = "insert into member_profile values (:mem_id,default)", nativeQuery = true)
    Integer setAddUserProfile(long mem_id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update member_profile set profile_intro=:intro where mem_id=:mem_id", nativeQuery = true)
    Integer IntroduceModify(String intro, long mem_id);

    @Modifying
    @Query(value = "update member set mem_pw=:mem_pw where mem_id=:mem_id", nativeQuery = true)
    Integer UpdatePassword(String mem_pw, long mem_id);

    @Modifying
    @Query(value = "update member set mem_nick=:mem_nick where mem_id=:mem_id", nativeQuery = true)
    Integer UpdateNick(String mem_nick, long mem_id);
}
