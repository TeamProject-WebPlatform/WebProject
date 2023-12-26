package platform.game.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import platform.game.service.entity.RankList;

@Repository
public interface RankListRepository extends JpaRepository<RankList, String> {

    List<RankList> findAll();
}
