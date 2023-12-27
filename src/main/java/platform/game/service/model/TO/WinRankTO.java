package platform.game.service.model.TO;

import lombok.Data;

@Data
public class WinRankTO {
    private int rank;
    private String mem_userid;
    private long winrate;
}
