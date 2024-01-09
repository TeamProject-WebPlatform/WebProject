package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositekey.MemberRankingPrimary;

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
    @Column(name = "mem_id")
    private long memId;

    @Column(name = "mem_rank")
    private int rank;
}
