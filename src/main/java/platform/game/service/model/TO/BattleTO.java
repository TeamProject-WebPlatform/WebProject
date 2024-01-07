package platform.game.service.model.TO;

import lombok.AllArgsConstructor;
import lombok.Data;
import platform.game.service.entity.Member;

@Data
@AllArgsConstructor
public class BattleTO {
    
    Member host; // 배틀 주최자
    Member client; // 배틀 참가자

    String title;
    int point;
    
    int postId; // 해당 배틀게시글 id
}
