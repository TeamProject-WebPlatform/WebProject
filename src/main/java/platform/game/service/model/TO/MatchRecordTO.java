package platform.game.service.model.TO;

import lombok.Data;

@Data
public class MatchRecordTO {
    private String gameCd;
    private long memId;
    private int totalMatchCnt;
    private int matchWinCnt;
    private int matchLoseCnt;
}
