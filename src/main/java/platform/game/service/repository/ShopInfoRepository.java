package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Shop;

@Repository
public interface ShopInfoRepository extends JpaRepository<Shop, Integer> {
    
    ArrayList<Shop> findAll();
    
    long count();
}    
