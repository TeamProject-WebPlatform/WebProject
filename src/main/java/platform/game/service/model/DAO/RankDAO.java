package platform.game.service.model.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.service.repository.RankingRepository;

@Repository
public class RankDAO {

    @Autowired
    RankingRepository rankingRepository;

    public List<Integer> getLevelList() {
        return rankingRepository.GetLevelRank();
    }

    public List<Integer> getWinList() {
        return rankingRepository.GetWinRank();
    }

    public List<Integer> getPointList() {
        return rankingRepository.GetPointRank();
    }

}
