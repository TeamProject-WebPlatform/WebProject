package platform.game.service.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Battle;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Integer> {
    List<Battle> findAll();
    Optional<Battle> findByBtId(int btId);
    
    // @Async    
    // @Modifying
    // @Query(value = "UPDATE battle SET bt_host_mem_bet_point = bt_host_mem_bet_point + :point, bt_host_mem_bet_cnt = bt_host_mem_bet_cnt + 1 WHERE bt_id = :btId", nativeQuery = true)
    // void updateHostBetPoint(@Param("btId") int btId, @Param("point") int point);
    
    // @Async
    // @Modifying
    // @Query(value = "UPDATE battle SET bt_client_mem_bet_point = bt_client_mem_bet_point + :point, bt_client_mem_bet_cnt = bt_client_mem_bet_cnt + 1 WHERE bt_id = :btId", nativeQuery = true)
    // void updateClientBetPoint(@Param("btId") int btId, @Param("point") int point);
}
