package platform.game.service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import platform.game.service.entity.Ranking;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Integer> {

    List<Ranking> findAll();

    List<Ranking> findTop50ByOrderByRankCodeAsc();

    List<Ranking> findTop50ByOrderByRankDesc();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_lvl / 10) * 10 AS levelRange FROM member GROUP BY levelRange ORDER BY levelRange")
    List<Integer> GetLevelRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , ifnull(floor(mem_game_win_cnt/mem_total_game_cnt*10) * 10,0) AS win FROM member GROUP BY win ORDER BY win")
    List<Integer> GetWinRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_total_point / 400) * 400 as point FROM member GROUP BY point ORDER BY point")
    List<Integer> GetPointRank();
}
