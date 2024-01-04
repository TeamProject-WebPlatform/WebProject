package platform.game.service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import platform.game.service.entity.RankList;

@Repository
public interface RankListRepository extends JpaRepository<RankList, Integer> {

    List<RankList> findAll();

    List<RankList> findTop50ByOrderByRankCodeAsc();

    List<RankList> findTop50ByOrderByRankDesc();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_lvl / 10) * 10 AS levelRange FROM member GROUP BY levelRange ORDER BY levelRange")
    List<Integer> GetLevelRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , ifnull(floor(mem_win_count/mem_game_count*10) * 10,0) AS win FROM member GROUP BY win ORDER BY win")
    List<Integer> GetWinRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_total_point / 400) * 400 as point FROM member GROUP BY point ORDER BY point")
    List<Integer> GetPointRank();
}
