package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.MemberRankingPrimary;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MemberRankingPrimary.class)
public class MemberRanking {

    @Id
    @Column(name = "rank_code")
    private String rankCode;

    @Id
    @Column(name = "memId")
    private long memId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @Column(name = "mem_rank")
    private int rank;
}
