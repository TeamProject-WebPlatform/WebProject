package platform.game.service.model.TO;

import lombok.Data;

@Data
public class PointRankTO {
    private int rank;
    private String mem_nick;
    private int mem_lvl;
    private int mem_total_point;
}
