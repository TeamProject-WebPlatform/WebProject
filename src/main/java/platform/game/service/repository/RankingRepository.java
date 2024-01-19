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

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_lvl / 5) * 5 AS levelRange FROM member GROUP BY levelRange ORDER BY levelRange")
    List<Integer> GetLevelRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange, ifnull(FLOOR((mem_game_win_cnt / mem_total_game_cnt * 100) / 5) * 5,0) AS winRange FROM member GROUP BY winRange ORDER BY winRange;")
    List<Integer> GetWinRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_total_point / 150) * 150 as point FROM member GROUP BY point ORDER BY point")
    List<Integer> GetPointRank();

    @Query(value="select mp.profile_intro, mp.profile_image, mp.profile_header, mp.profile_card, mp.profile_rep_badge from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='level' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<Object[]> getLevelRankerProfile(String game_cd);

    @Query(value="select mp.profile_intro, mp.profile_image, mp.profile_header, mp.profile_card, mp.profile_rep_badge from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='Point' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<Object[]> getPointRankerProfile(String game_cd);

    @Query(value="select mp.profile_intro, mp.profile_image, mp.profile_header, mp.profile_card, mp.profile_rep_badge from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='WinRate' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<Object[]> getWinRateRankerProfile(String game_cd);
}
