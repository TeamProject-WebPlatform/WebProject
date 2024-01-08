package platform.game.service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import platform.game.service.entity.MemberFavoriteGame;

public interface MemberFavoriteGameRepository extends JpaRepository<MemberFavoriteGame, Integer> {

    // 멤버 ID 기준으로 선호하는 게임 1,2,3순위 가져오기
    @Query(value = "select * from member_favorite_game m where m.mem_id=:mem_id", nativeQuery = true)
    List<MemberFavoriteGame> findByMemID(@Param("mem_id") long mem_id);
}
