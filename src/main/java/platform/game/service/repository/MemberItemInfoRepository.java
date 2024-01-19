package platform.game.service.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import platform.game.service.entity.Item;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberItem;
import platform.game.service.entity.compositeKey.MemberItemId;

@Repository
public interface MemberItemInfoRepository extends JpaRepository<MemberItem, MemberItemId> {
    // 전부 불러오기
    ArrayList<MemberItem> findAll();

    // // 맴버 아이템 코드 확인
    ArrayList<MemberItem> findByMemIdAndItemCd(Member memId, int itemCd);

    // 아이템샵 아이템 코드 확인
    ArrayList<Item> findByItemCd(int itemCd);

    // 갖고 있는 아이템 확인
    @Query(value = "select item_cd from member_item where mem_id=:mem_id", nativeQuery = true)
    List<Integer> HaveItemCheck(long mem_id);

    // 아이템 구매
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into member_item values(0,:mem_id,:item_cd, default, now())", nativeQuery = true)
    Integer PurchaseItem(long mem_id, String item_cd);

    // 아이템 구매 후 포인트 차감
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member set mem_cur_point = mem_cur_point-:point where mem_id=:mem_id", nativeQuery = true)
    Integer UpdatePoint(int point, long mem_id);

    // 갖고 있는 뱃지들
    @Query(value="select i.item_nm from item i join member_item mi on mi.item_cd = i.item_cd and i.item_cd like '803%' join member m on mi.mem_id = m.mem_id and m.mem_id=:mem_id",nativeQuery = true)
    List<String> getHaveBadges(long mem_id);

    // 아이템 이름 불러오기
    @Query(value = "select i.item_info from item i join member_item m on i.item_cd=m.item_cd where i.item_cd like '803%' and m.mem_id=:mem_id", nativeQuery = true)
    List<String> getItemName(long mem_id);

}
