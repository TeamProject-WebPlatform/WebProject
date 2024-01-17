package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberFavoriteGamePrimary implements Serializable {
    private int favGameId;
    private long memId;
}
