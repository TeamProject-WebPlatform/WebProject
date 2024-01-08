package platform.game.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.composite.MemberMatchRecordPrimary;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MemberMatchRecordPrimary.class)
public class MemberMatchRecord {

    @Id
    private String game_cd;
    @Id
    private long mem_id;
    private int mem_game_totalmatch_cnt;
    private int mem_game_match_win_cnt;
    private int mem_game_match_lost_cnt;
}
