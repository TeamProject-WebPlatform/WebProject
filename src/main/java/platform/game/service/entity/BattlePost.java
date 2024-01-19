package platform.game.service.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BattlePost {

    @Id
    private int postId;  // 외래키로 사용할 postId

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // postId를 기본키로 사용
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "btId", referencedColumnName = "btId")
    private Battle battle;

    private String btPostApplicants;
    private Date btStartDt;
    private String gameCd;
    private Date btPostDeadLine;
    private int btPostPoint;
    private long bettingFinTime;

}