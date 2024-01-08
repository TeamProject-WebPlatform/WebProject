package platform.game.service.entity.composite;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberFavoriteGamePrimary implements Serializable {
    private int mem_fav_game_id;
    private long mem_id;
}
