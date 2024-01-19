package platform.game.service.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Post;
import platform.game.service.entity.Ranking;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Integer> {

    List<Ranking> findAll();

    List<Ranking> findTop50ByOrderByRankCodeAsc();

    List<Ranking> findTop50ByOrderByRankDesc();

    // 랭킹 상위 10명
    List<Ranking> findTop10ByOrderByRankDesc();


    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_lvl / 5) * 5 AS levelRange FROM member GROUP BY levelRange ORDER BY levelRange")
    List<Integer> GetLevelRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange, ifnull(FLOOR((mem_game_win_cnt / mem_total_game_cnt * 100) / 5) * 5,0) AS winRange FROM member GROUP BY winRange ORDER BY winRange;")
    List<Integer> GetWinRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_total_point / 150) * 150 as point FROM member GROUP BY point ORDER BY point")
    List<Integer> GetPointRank();
}
