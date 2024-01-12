package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.MemberMatchRecordPrimary;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MemberMatchRecordPrimary.class)
public class MemberMatchRecord {

    @Id
    @Column(name = "game_cd")
    private String gameCd;
    @Id
    @Column(name = "mem_id")
    private long memId;

    @Column(name = "mem_bt_total_game_cnt")
    private int matchCnt;

    @Column(name = "mem_bt_win_cnt")
    private int winCnt;

    @Column(name = "mem_bt_lose_cnt")
    private int loseCnt;
}
