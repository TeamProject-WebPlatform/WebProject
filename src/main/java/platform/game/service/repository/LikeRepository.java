package platform.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Like;
import platform.game.service.entity.PointHistory;
import platform.game.service.entity.compositeKey.LikeId;
import platform.game.service.entity.compositeKey.PointHistoryId;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId>{
}
