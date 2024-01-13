package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
