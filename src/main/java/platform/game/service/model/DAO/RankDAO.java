package platform.game.service.model.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nimbusds.jose.shaded.gson.JsonObject;

import platform.game.service.entity.RankList;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.repository.RankListRepository;

@Repository
public class RankDAO {

    @Autowired
    RankListRepository rankListRepository;

    public List<RankList> getLevelRankLists() {
        System.out.println("호출");
        List<RankList> list = rankListRepository.findTop50ByOrderByRankCodeAsc();
        return list;
    }

    public ArrayList<Integer> getLevelList() {
        int over90 = rankListRepository.LevelRank90over();
        int over80 = rankListRepository.LevelRank80over();
        int over70 = rankListRepository.LevelRank70over();
        int over60 = rankListRepository.LevelRank60over();
        int over50 = rankListRepository.LevelRank50over();
        int over40 = rankListRepository.LevelRank40over();
        int over30 = rankListRepository.LevelRank30over();
        int over20 = rankListRepository.LevelRank20over();
        int over10 = rankListRepository.LevelRank10over();
        int over0 = rankListRepository.LevelRank0over();

        ArrayList<Integer> rankList = new ArrayList<Integer>();
        rankList.add(over90);
        rankList.add(over80);
        rankList.add(over70);
        rankList.add(over60);
        rankList.add(over50);
        rankList.add(over40);
        rankList.add(over30);
        rankList.add(over20);
        rankList.add(over10);
        rankList.add(over0);
        return rankList;
    }
}
