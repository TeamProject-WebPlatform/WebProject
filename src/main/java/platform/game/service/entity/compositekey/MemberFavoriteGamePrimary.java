package platform.game.service.entity.compositekey;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class MemberFavoriteGamePrimary implements Serializable {
    private int favGameId;
    private long memId;
}
