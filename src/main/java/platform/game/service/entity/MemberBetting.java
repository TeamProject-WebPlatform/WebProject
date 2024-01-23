package platform.game.service.entity;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(MemberBettingId.class)
public class MemberBetting {

    @Id
    @MapsId("memId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Id
    @MapsId("btId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Battle battle;

    private int betFlag; //'0': 호스트 / '1': 클라이언트
    private int betPoint;
    private Date betAt;

    @ColumnDefault("-1")
    private String pointReceived;

    @ColumnDefault("0")
    private long pointDstb;
}
