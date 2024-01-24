package platform.game.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import platform.game.service.entity.MemberProfile;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    MemberProfile findProfileIntroByMemId(Long memId);

    // 가입 시 프로필 정보 추가
    @Query(value = "insert into member_profile values (:mem_id,default,default,default,default,default,default)", nativeQuery = true)
    Integer setAddUserProfile(long mem_id);

    // 자기소개 변경
    @Modifying(clearAutomatically = true)
    @Query(value = "update member_profile set profile_intro=:intro where mem_id=:mem_id", nativeQuery = true)
    Integer IntroduceModify(String intro, long mem_id);

    // 비밀번호 변경
    @Modifying
    @Query(value = "update member set mem_pw=:mem_pw where mem_id=:mem_id", nativeQuery = true)
    Integer UpdatePassword(String mem_pw, long mem_id);

    // 닉네임 변경
    @Modifying
    @Query(value = "update member set mem_nick=:mem_nick where mem_id=:mem_id", nativeQuery = true)
    Integer UpdateNick(String mem_nick, long mem_id);

    // 프로필 사진 불러오기
    @Query(value = "select profile_image from member_profile where mem_id=:mem_id", nativeQuery = true)
    String findByProfileImage(long mem_id);

    // 뱃지 리스트 불러오기
    @Query(value = "select profile_badge_list from member_profile where mem_id=:mem_id", nativeQuery = true)
    String findByBadgeList(long mem_id);

    // 갖고 있는 헤더 아이템 불러오기
    @Query(value = "select i.item_nm, i.item_info from item i join member_item mi on i.item_cd = mi.item_cd and i.item_kind_cd like '801%' and mi.mem_id=:mem_id", nativeQuery = true)
    List<Object[]> HaveHeaderList(long mem_id);

    // 갖고 있는 카드 아이템 불러오기
    @Query(value = "select i.item_nm, i.item_info from item i join member_item mi on i.item_cd = mi.item_cd and i.item_kind_cd like '802%' and mi.mem_id=:mem_id", nativeQuery = true)
    List<Object[]> HaveCardList(long mem_id);

    // 갖고 있는 뱃지 아이템 불러오기기
    @Query(value = "select i.item_nm, i.item_info from item i join member_item mi on i.item_cd = mi.item_cd and i.item_kind_cd like '803%' and mi.mem_id=:mem_id", nativeQuery = true)
    List<Object[]> HaveBadgeList(long mem_id);

    // 헤더 아이템 변경
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_profile set profile_header=:header where mem_id=:mem_id", nativeQuery = true)
    Integer UpdateProfileHeader(String header, long mem_id);

    // 카드 아이템 변경
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_profile set profile_card=:card where mem_id=:mem_id", nativeQuery = true)
    Integer UpdateProfileCard(String card, long mem_id);

    // 대표 뱃지 아이템 변경
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_profile set profile_rep_badge=:badge where mem_id=:mem_id", nativeQuery = true)
    Integer UpdateProfileRepBadge(String badge, long mem_id);

    // 프로필 사진 변경
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_profile set profile_image=:image where mem_id=:mem_id", nativeQuery = true)
    Integer UpdateProfileImage(String image, long mem_id);

    // 뱃지 리스트 변경
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_profile set profile_badge_list=:BadgeList where mem_id=:mem_id", nativeQuery = true)
    Integer UpdateProfileBadgeList(String BadgeList, long mem_id);

    // 배틀컨트롤러에서 mem_nick을 받아서 정보를 출력.. 
    @Query(value="select distinct mp.* from member_profile mp join member m on mp.mem_id=m.mem_id where m.mem_nick=:mem_nick",nativeQuery = true)
    MemberProfile BattleProfile(String mem_nick);

    @Query(value="select distinct mp.profile_image from member_profile mp join member m on mp.mem_id=m.mem_id where m.mem_nick=:mem_nick",nativeQuery = true)
    String BattleProfileImage(String mem_nick);

    @Query(value="select distinct mp.profile_badge_list from member_profile mp join member m on mp.mem_id=m.mem_id where m.mem_nick=:mem_nick",nativeQuery = true)
    String BattleProfileBadgeList(String mem_nick);

    @Query(value="select distinct mp.profile_header from member_profile mp join member m on mp.mem_id=m.mem_id where m.mem_nick=:mem_nick",nativeQuery = true)
    String BattleProfileBorder(String mem_nick);

    @Query(value="select distinct mp.profile_intro from member_profile mp join member m on mp.mem_id=m.mem_id where m.mem_nick=:mem_nick", nativeQuery = true)
    String BattleProfileIntro(String mem_nick);
    
    @Query(value="select distinct mp.profile_rep_badge from member_profile mp join member m on mp.mem_id=m.mem_id where m.mem_nick=:mem_nick", nativeQuery = true)
    String BattleProfileRepBadge(String mem_nick);
}
