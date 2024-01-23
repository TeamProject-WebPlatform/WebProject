package platform.game.service.repository;

import platform.game.service.entity.MemberBetting;
import platform.game.service.entity.compositeKey.MemberBettingId;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBettingRepository extends JpaRepository<MemberBetting, MemberBettingId>{
    public MemberBetting findByBattle_BtIdAndMember_MemId(int btId,long memId);
}
