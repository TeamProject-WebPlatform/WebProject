package platform.game.service.model.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.service.repository.RankListRepository;

@Repository
public class RankDAO {

    @Autowired
    RankListRepository rankListRepository;

    public List<Integer> getLevelList() {
        return rankListRepository.GetLevelRank();
    }

    public List<Integer> getWinList() {
        return rankListRepository.GetWinRank();
    }

    public List<Integer> getPointList() {
        return rankListRepository.GetPointRank();
    }

}
