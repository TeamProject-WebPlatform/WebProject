package platform.game.service.entity.compositekey;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberMatchRecordPrimary implements Serializable {

    private String gameCd;
    private long memId;
}
