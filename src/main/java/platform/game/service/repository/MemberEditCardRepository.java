package platform.game.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.MemberCard;


@Repository
public interface MemberEditCardRepository extends JpaRepository<MemberCard, Long>{

    // 모든 카드 요소 불러오기
    MemberCard findAllByMemId(long mem_id);

    // 프로필사진만 불러오기
    @Query(value="select profile_image from member_card where mem_id=:mem_id", nativeQuery = true)
    String findByProfileImage(long mem_id);

    @Query(value="select mi.item_cd, i.item_info from member_item mi join item i on mi.item_cd = i.item_cd and i.item_kind_cd like '801%' and mi.mem_id=:mem_id",nativeQuery = true)
    List<Object[]> HaveHeaderList(long mem_id);

    @Query(value="select mi.item_cd, i.item_info from member_item mi join item i on mi.item_cd = i.item_cd and i.item_kind_cd like '802%' and mi.mem_id=:mem_id",nativeQuery = true)
    List<Object[]> HaveCardList(long mem_id);

    @Query(value="select mi.item_cd, i.item_info from member_item mi join item i on mi.item_cd = i.item_cd and i.item_kind_cd like '803%' and mi.mem_id=:mem_id",nativeQuery = true)
    List<Object[]> HaveBadgeList(long mem_id);
}
