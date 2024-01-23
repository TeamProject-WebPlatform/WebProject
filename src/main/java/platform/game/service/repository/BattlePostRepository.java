package platform.game.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import platform.game.service.entity.BattlePost;

public interface BattlePostRepository extends JpaRepository<BattlePost, Integer> {
    // 추가적으로 필요한 메서드가 있으면 여기에 선언할 수 있습니다.

    // 예시: postId를 이용하여 BattlePost를 찾는 메서드
    BattlePost findByPost_PostId(int postId);
}
