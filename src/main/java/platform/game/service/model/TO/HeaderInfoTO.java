package platform.game.service.model.TO;

import lombok.Data;

@Data
public class HeaderInfoTO {
    long memId;
    int pointChange;
    int currentPoint;
    int flag = 0; // -1은 실패 0은 성공
}
