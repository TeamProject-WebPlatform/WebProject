package platform.game.service.model.TO;

import lombok.Data;
import platform.game.service.entity.Member;

@Data
public class BettingStateInfoTO {
    long delay;
    int endpoint = 1;
    int btId;
    String state;
    BattleMemberTO client;
    String result;
    int point;
}
