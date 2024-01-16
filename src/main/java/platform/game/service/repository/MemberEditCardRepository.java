package platform.game.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import platform.game.service.entity.MemberCard;


@Repository
public interface MemberEditCardRepository extends JpaRepository<MemberCard, Long>{

    // 모든 카드 요소 불러오기
    MemberCard findAllByMemId(long mem_id);

    // 회원 가입 시 카드 꾸미기 db 추가
    @Query (value="insert into member_card values (:mem_id,default,default,default,default,default,default,default,default)", nativeQuery = true)
    void SetMemberCard(long mem_id);

    // 프로필사진만 불러오기
    @Query(value="select profile_image from member_card where mem_id=:mem_id", nativeQuery = true)
    String findByProfileImage(long mem_id);

    @Query(value="select i.item_nm, i.item_info from item i join member_item mi on i.item_cd = mi.item_cd and i.item_kind_cd like '801%' and mi.mem_id=:mem_id",nativeQuery = true)
    List<Object[]> HaveHeaderList(long mem_id);

    @Query(value="select i.item_nm, i.item_info from item i join member_item mi on i.item_cd = mi.item_cd and i.item_kind_cd like '802%' and mi.mem_id=:mem_id",nativeQuery = true)
    List<Object[]> HaveCardList(long mem_id);

    @Query(value="select i.item_nm, i.item_info from item i join member_item mi on i.item_cd = mi.item_cd and i.item_kind_cd like '803%' and mi.mem_id=:mem_id",nativeQuery = true)
    List<Object[]> HaveBadgeList(long mem_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_card set profile_header=:header where mem_id=:mem_id",nativeQuery = true)
    Integer UpdateProfileHeader(String header, long mem_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_card set profile_card=:card where mem_id=:mem_id",nativeQuery = true)
    Integer UpdateProfileCard(String card, long mem_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member_card set profile_badges1=:badge where mem_id=:mem_id",nativeQuery = true)
    Integer UpdateProfileRepBadge(String badge, long mem_id);
}
