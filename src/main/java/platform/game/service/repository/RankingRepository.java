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

    // 랭킹 상위 10명
    List<Ranking> findTop10ByOrderByRankDesc();


    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_lvl / 5) * 5 AS levelRange FROM member GROUP BY levelRange ORDER BY levelRange")
    List<Integer> GetLevelRank();

    @Query(nativeQuery = true, value = "SELECT \r\n" + //
            "  COUNT(m.mem_game_win_cnt) AS countInRange\r\n" + //
            "FROM\r\n" + //
            "  (SELECT 0 AS winRange UNION SELECT 5 UNION SELECT 10 UNION SELECT 15 UNION SELECT 20\r\n" + //
            "   UNION SELECT 25 UNION SELECT 30 UNION SELECT 35 UNION SELECT 40 UNION SELECT 45\r\n" + //
            "   UNION SELECT 50 UNION SELECT 55 UNION SELECT 60 UNION SELECT 65 UNION SELECT 70\r\n" + //
            "   UNION SELECT 75 UNION SELECT 80 UNION SELECT 85 UNION SELECT 90 UNION SELECT 95\r\n" + //
            "   UNION SELECT 100) AS ranges\r\n" + //
            "LEFT JOIN\r\n" + //
            "  member AS m ON FLOOR((m.mem_game_win_cnt / m.mem_total_game_cnt * 100) / 5) * 5 = ranges.winRange\r\n" + //
            "GROUP BY\r\n" + //
            "  winRange\r\n" + //
            "ORDER BY\r\n" + //
            "  winRange;")
    List<Integer> GetWinRank();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS countInRange , floor(mem_total_point / 150) * 150 as point FROM member GROUP BY point ORDER BY point")
    List<Integer> GetPointRank();

    @Query(value="select mp.profile_intro, mp.profile_image, mp.profile_header, mp.profile_card, mp.profile_rep_badge from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='level' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<Object[]> getLevelRankerProfile(String game_cd);

    @Query(value="select mp.profile_intro, mp.profile_image, mp.profile_header, mp.profile_card, mp.profile_rep_badge from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='Point' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<Object[]> getPointRankerProfile(String game_cd);

    @Query(value="select mp.profile_intro, mp.profile_image, mp.profile_header, mp.profile_card, mp.profile_rep_badge from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='WinRate' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<Object[]> getWinRateRankerProfile(String game_cd);

    // 랭커 뱃지 리스트 따로
    @Query(value="select mp.profile_badge_list from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='Level' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<String> getLevelRankerBadgeList(String game_cd);

    @Query(value="select mp.profile_badge_list from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='Point' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<String> getPointRankerBadgeList(String game_cd);

    @Query(value="select mp.profile_badge_list from member_profile mp join member m on mp.mem_id = m.mem_id join ranking r on r.rank_code='WinRate' and mp.mem_id=r.mem_id and r.game_cd=:game_cd order by r.rank limit 5", nativeQuery = true)
    List<String> getWinRateRankerBadgeList(String game_cd);
}
