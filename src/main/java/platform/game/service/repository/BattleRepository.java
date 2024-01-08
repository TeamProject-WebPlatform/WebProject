package platform.game.service.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Battle;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Integer> {
    List<Battle> findAll();
}
