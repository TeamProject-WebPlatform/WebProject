package platform.game.service.model.TO;

import lombok.Data;

@Data
public class HeaderInfoTO {
    long memId;
    String pointKindCd;
    int pointChange;
    int currentPoint;
    int flag = 0; // -1은 실패 0은 default 1은 성공 2는 포인트 부족
}
