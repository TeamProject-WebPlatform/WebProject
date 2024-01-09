package platform.game.service.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.MemberBettingId;
import platform.game.service.entity.compositeKey.RankPrimary;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(MemberBettingId.class)
public class MemberBetting {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memId")
    private Member member;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("btId")
    private Battle battle;

    private String betTarget; //'0': 호스트 / '1': 클라이언트
    private int betPoint;
    private Date betAt; 
    
}
