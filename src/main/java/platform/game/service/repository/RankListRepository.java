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

    @Query(value = "select count(*) from member where mem_lvl>90", nativeQuery = true)
    int LevelRank90over();

    @Query(value = "select count(*) from member where mem_lvl>=80 and mem_lvl<90", nativeQuery = true)
    int LevelRank80over();

    @Query(value = "select count(*) from member where mem_lvl>=70 and mem_lvl<80", nativeQuery = true)
    int LevelRank70over();

    @Query(value = "select count(*) from member where mem_lvl>=60 and mem_lvl<70", nativeQuery = true)
    int LevelRank60over();

    @Query(value = "select count(*) from member where mem_lvl>=50 and mem_lvl<60", nativeQuery = true)
    int LevelRank50over();

    @Query(value = "select count(*) from member where mem_lvl>=40 and mem_lvl<50", nativeQuery = true)
    int LevelRank40over();

    @Query(value = "select count(*) from member where mem_lvl>=30 and mem_lvl<40", nativeQuery = true)
    int LevelRank30over();

    @Query(value = "select count(*) from member where mem_lvl>=20 and mem_lvl<30", nativeQuery = true)
    int LevelRank20over();

    @Query(value = "select count(*) from member where mem_lvl>=10 and mem_lvl<20", nativeQuery = true)
    int LevelRank10over();

    @Query(value = "select count(*) from member where mem_lvl>=0 and mem_lvl<10", nativeQuery = true)
    int LevelRank0over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 >= 90;", nativeQuery = true)
    int WinRank90over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <90 and mem_win_count/mem_game_count*100 >=80 ", nativeQuery = true)
    int WinRank80over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <80 and mem_win_count/mem_game_count*100 >=70", nativeQuery = true)
    int WinRank70over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <70 and mem_win_count/mem_game_count*100 >=60", nativeQuery = true)
    int WinRank60over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <60 and mem_win_count/mem_game_count*100 >=50", nativeQuery = true)
    int WinRank50over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <50 and mem_win_count/mem_game_count*100 >=40", nativeQuery = true)
    int WinRank40over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <40 and mem_win_count/mem_game_count*100 >=30", nativeQuery = true)
    int WinRank30over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <30 and mem_win_count/mem_game_count*100 >=20", nativeQuery = true)
    int WinRank20over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <20 and mem_win_count/mem_game_count*100 >=10", nativeQuery = true)
    int WinRank10over();

    @Query(value = "select count(*) from member where mem_win_count/mem_game_count*100 <10 and mem_win_count/mem_game_count*100 >=0", nativeQuery = true)
    int WinRank0over();

    @Query(value = "select count(*) from member where mem_attend >=300", nativeQuery = true)
    int AttendRank300over();

    @Query(value = "select count(*) from member where mem_attend >=250 and mem_attend <300", nativeQuery = true)
    int AttendRank250over();

    @Query(value = "select count(*) from member where mem_attend >=200 and mem_attend <250", nativeQuery = true)
    int AttendRank200over();

    @Query(value = "select count(*) from member where mem_attend >=150 and mem_attend <200", nativeQuery = true)
    int AttendRank150over();

    @Query(value = "select count(*) from member where mem_attend >=100 and mem_attend <150", nativeQuery = true)
    int AttendRank100over();

    @Query(value = "select count(*) from member where mem_attend >=50 and mem_attend <100", nativeQuery = true)
    int AttendRank50over();

    @Query(value = "select count(*) from member where mem_attend >=0 and mem_attend <50", nativeQuery = true)
    int AttendRank0over();
}
