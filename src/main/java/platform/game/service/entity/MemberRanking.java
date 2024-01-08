package platform.game.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.composite.MemberRankingPrimary;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MemberRankingPrimary.class)
public class MemberRanking {

    @Id
    private String rank_code;
    @Id
    private long mem_id;
    private int mem_rank;
}
