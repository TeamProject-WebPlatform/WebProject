package platform.game.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Item;

import java.util.ArrayList;

@Repository
public interface ItemInfoRepository extends JpaRepository<Item, Integer> {
    
    // 아이템 찾기
    Item findByItemNm(String itemNm);

    // 아이템 종류별로 찾기
    ArrayList<Item> findByItemKindCd(String itemKindCd);

    // 전체 아이템 목록
    ArrayList<Item> findAll();
    
    // 아이템 갯수 조회
    long count();
    
}
