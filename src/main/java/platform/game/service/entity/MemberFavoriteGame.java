package platform.game.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.composite.MemberFavoriteGamePrimary;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MemberFavoriteGamePrimary.class)
public class MemberFavoriteGame {

    @Id
    private int mem_fav_game_id;
    @Id
    private long mem_id;
    private String game_cd;
}
