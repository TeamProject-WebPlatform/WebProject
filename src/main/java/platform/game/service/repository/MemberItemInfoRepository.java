package platform.game.service.repository;

import java.util.ArrayList;

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

    // 보유한 아이템 확인
    ArrayList<MemberItem> findByItemCd(long mem_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into member_item values(0,:mem_id,:item_cd, default, now())", nativeQuery = true)
    Integer PurchaseItem(long mem_id, String item_cd);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update member set mem_cur_point = mem_cur_point-:point where mem_id=:mem_id", nativeQuery = true)
    Integer UpdatePoint(int point, long mem_id);
}
