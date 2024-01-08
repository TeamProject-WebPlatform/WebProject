package platform.game.service.entity.composite;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberMatchRecordPrimary implements Serializable {

    private String game_cd;
    private long mem_id;
}
