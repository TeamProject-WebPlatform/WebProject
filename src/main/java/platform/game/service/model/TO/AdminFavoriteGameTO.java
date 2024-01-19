package platform.game.service.model.TO;

import lombok.Data;

@Data
public class AdminFavoriteGameTO {
    private String firstgame;
    private String secondgame;
    private String thirdgame;
    private long selectMemId;
}
