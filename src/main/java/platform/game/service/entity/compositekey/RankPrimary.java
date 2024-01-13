package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import lombok.Data;

// 랭크 테이블 기본키 멀티 지정 클래스
@Data
public class RankPrimary implements Serializable {

    private int rank;
    private int rankCode;
    private String gameCd;
}