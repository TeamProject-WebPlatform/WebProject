package platform.game.service.repository;

import platform.game.service.entity.MemberBetting;
import platform.game.service.entity.compositeKey.MemberBettingId;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBettingRepository extends JpaRepository<MemberBetting, MemberBettingId>{
        
    // @Async
    // @Modifying
    // @Query(value = "INSERT INTO member_betting values(now(),:point,:memId,:btId,:flag)", nativeQuery = true)
    // void insertData(@Param("point") int point,@Param("memId") long memId, @Param("btId") int btId,@Param("flag") int flag);
    // bet_at, bet_point, mem_id, bt_id, flag
}
