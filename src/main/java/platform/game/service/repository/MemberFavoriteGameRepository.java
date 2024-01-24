package platform.game.service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import platform.game.service.entity.MemberFavoriteGame;

@Repository
public interface MemberFavoriteGameRepository extends JpaRepository<MemberFavoriteGame, Integer> {

    // 선호하는 게임 있는지 확인
    boolean existsByMemId(long mem_id);

    // 멤버 ID 기준으로 선호하는 게임 1,2,3순위 가져오기
    @Query(value = "select game_cd from member_favorite_game m where m.mem_id=:mem_id", nativeQuery = true)
    List<String> FavoriteGameCode(@Param("mem_id") long mem_id);

    MemberFavoriteGame[] findByMemId(long memId);

    @Query(value = "insert into member_favorite_game values (:rank,:mem_id,:game_cd)", nativeQuery = true)
    void RegisterGame(int rank, long mem_id, String game_cd);

    int countByMemId(long mem_id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update member_favorite_game set game_cd=:game_cd where mem_id=:mem_id and mem_fav_game_id=:rank", nativeQuery = true)
    void GameModify(String game_cd, long mem_id, int rank);

    @Query(value = "SELECT game_cd, COUNT(*) as count " +
            "FROM member_favorite_game " +
            "GROUP BY game_cd " +
            "ORDER BY count DESC", nativeQuery = true)
    List<String> findMostCommonGameCd();

    // 1,2,3 순위 게임 코드 가져오기
    @Query(value = "select game_cd from member_favorite_game where mem_id=:mem_id and mem_fav_game_id=:fav_id", nativeQuery = true)
    String getGameCd(long mem_id, int fav_id);
}
