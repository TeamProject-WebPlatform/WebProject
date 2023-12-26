package platform.game.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import platform.game.service.entity.RankList;

@Repository
public interface RankListRepository extends JpaRepository<RankList, Integer> {

    List<RankList> findAll();

    List<RankList> findTop50ByOrderByRankCodeAsc();

    List<RankList> findTop50ByOrderByRankDesc();
}
