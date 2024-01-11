package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.RankPrimary;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ranking")
@IdClass(RankPrimary.class)
public class RankList {

    @Id
    private int rank;
    @Id
    @Column(name = "rank_code")
    private String rankCode;
    @Column(name = "mem_id")
    private int memId;
    @Column(name = "rank_update")
    private String rankUpdate;
    @Id
    @Column(name = "game_cd")
    private String gameCd;
}
