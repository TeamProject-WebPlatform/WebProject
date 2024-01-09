package platform.game.service.model.TO;

import lombok.Data;

@Data
public class MemberRankingTO {
    private String rankCode;
    private long memId;
    private int rank;
}
