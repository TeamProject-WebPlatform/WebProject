package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import platform.game.service.entity.Battle;
import platform.game.service.entity.Member;

@Embeddable
@Data
public class MemberBettingId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "btId", referencedColumnName = "btId")
    private Battle battle;
}