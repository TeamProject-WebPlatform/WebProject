package platform.game.service.model.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.RankList;
import platform.game.service.model.TO.RollingRankTO;
import platform.game.service.repository.RankListRepository;

@Repository
public class RankDAO {

    @Autowired
    RankListRepository rankListRepository;

    // public List<RankList> getLevelRankLists() {
    // System.out.println("호출");
    // List<RankList> list = rankListRepository.findTop50ByOrderByRankCodeAsc();
    // return list;
    // }

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
