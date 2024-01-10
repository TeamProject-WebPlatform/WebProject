package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberRankingPrimary implements Serializable {
    private String rankCode;
    private long memId;
}
