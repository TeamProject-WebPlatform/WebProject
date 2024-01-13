package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@IdClass(RankPrimary.class)
public class Ranking {

    @Id
    private int rank;

    @Id
    @Column(name = "rank_code")
    private String rankCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_code", referencedColumnName = "rank_code")
    private MemberRanking memberRanking;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @Column(name = "rank_update")
    private String rankUpdate;
    @Id
    @Column(name = "game_cd")
    private String gameCd;
}
