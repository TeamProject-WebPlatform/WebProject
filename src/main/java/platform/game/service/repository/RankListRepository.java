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
}
