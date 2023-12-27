package platform.game.service.model.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import platform.game.service.entity.RankList;
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

    public ArrayList<Integer> getAttendList() {
        int over300 = rankListRepository.AttendRank300over();
        int over250 = rankListRepository.AttendRank250over();
        int over200 = rankListRepository.AttendRank200over();
        int over150 = rankListRepository.AttendRank150over();
        int over100 = rankListRepository.AttendRank100over();
        int over50 = rankListRepository.AttendRank50over();
        int over0 = rankListRepository.AttendRank0over();

        ArrayList<Integer> rankList = new ArrayList<Integer>();

        rankList.add(over300);
        rankList.add(over250);
        rankList.add(over200);
        rankList.add(over150);
        rankList.add(over100);
        rankList.add(over50);
        rankList.add(over0);

        return rankList;
    }

    public ArrayList<Integer> getWinList() {
        int over90 = rankListRepository.WinRank90over();
        int over80 = rankListRepository.WinRank80over();
        int over70 = rankListRepository.WinRank70over();
        int over60 = rankListRepository.WinRank60over();
        int over50 = rankListRepository.WinRank50over();
        int over40 = rankListRepository.WinRank40over();
        int over30 = rankListRepository.WinRank30over();
        int over20 = rankListRepository.WinRank20over();
        int over10 = rankListRepository.WinRank10over();
        int over0 = rankListRepository.WinRank0over();

        ArrayList<Integer> rankList = new ArrayList<Integer>();
        rankList.add(over0);
        rankList.add(over10);
        rankList.add(over20);
        rankList.add(over30);
        rankList.add(over40);
        rankList.add(over50);
        rankList.add(over60);
        rankList.add(over70);
        rankList.add(over80);
        rankList.add(over90);
        return rankList;
    }

    public ArrayList<Integer> getPointList(){
        int over1600 = rankListRepository.PointRank1600over();
        int over1200 = rankListRepository.PointRank1200over();
        int over800 = rankListRepository.PointRank800over();
        int over400 = rankListRepository.PointRank400over();
        int over0 = rankListRepository.PointRank0over();

        ArrayList<Integer> rankList = new ArrayList<Integer>();
        rankList.add(over0);
        rankList.add(over400);
        rankList.add(over800);
        rankList.add(over1200);
        rankList.add(over1600);

        return rankList;
    }
}
