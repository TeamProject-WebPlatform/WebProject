package platform.game.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import platform.game.service.mapper.SqlMapperInter;
import platform.game.service.model.DAO.RankDAO;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.RollingRankTO;
import platform.game.service.model.TO.WinRankTO;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.security" })
public class RankController {
    @Autowired
    SqlMapperInter sqlMapperInter;

    @Autowired
    RankDAO rankDAO;

    private List<RollingRankTO> rollingRankList;

    public RankController(SqlMapperInter sqlMapperInter) {
        this.sqlMapperInter = sqlMapperInter;
        rollingRankList = sqlMapperInter.getRol();
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void RollingTimer() {
        rollingRankList = sqlMapperInter.getRol();
    }

    @PostMapping("/roll") // 메인화면 Rolling RandomList
    @ResponseBody
    public List<RollingRankTO> roll() {
        return rollingRankList;
    }

    @GetMapping("/getRankFragment")
    public String getRankFragment(@RequestParam("board_cd") String boardCd, Model model) {
        return "fragments/content/rank";
    }

    @PostMapping("/getLevelChart")
    @ResponseBody
    public List<Integer> getLevelChart() {
        List<Integer> LevelChart = rankDAO.getLevelList();
        return LevelChart;
    }

    @PostMapping("/getWinRateChart")
    @ResponseBody
    public List<Integer> getWinRateChart() {
        List<Integer> WinRateChart = rankDAO.getWinList();
        return WinRateChart;
    }

    @PostMapping("/getPointChart")
    @ResponseBody
    public List<Integer> getPointChart() {
        List<Integer> PointChart = rankDAO.getPointList();
        return PointChart;
    }

    @PostMapping("/getLevelTable")
    @ResponseBody
    public List<LevelRankTO> getLevelTable() {
        List<LevelRankTO> getLevelTable = sqlMapperInter.getLevelrank();
        return getLevelTable;
    }

    @PostMapping("/getWinRateTable")
    @ResponseBody
    public List<WinRankTO> getWinRateTable() {
        List<WinRankTO> getWinRateTable = sqlMapperInter.getWinrank();
        return getWinRateTable;
    }

    @PostMapping("/getPointTable")
    @ResponseBody
    public List<PointRankTO> getPointTable() {
        List<PointRankTO> getPointTable = sqlMapperInter.getPointrank();
        return getPointTable;
    }
}
