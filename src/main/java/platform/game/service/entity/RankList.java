package platform.game.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ranklist")
@IdClass(RankPrimary.class)
public class RankList {

    @Id
    private int rank;
    @Id
    private char rank_code;
    private int mem_id;
    private String rank_update;
}
