package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.MemberFavoriteGamePrimary;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MemberFavoriteGamePrimary.class)
public class MemberFavoriteGame {

    @Id
    @Column(name = "mem_fav_game_id")
    private int favGameId;

    @Id
    @Column(name = "memId")
    private long memId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    private String gameCd;
}
