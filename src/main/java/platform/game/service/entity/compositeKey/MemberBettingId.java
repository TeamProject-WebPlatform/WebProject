package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import platform.game.service.entity.Battle;
import platform.game.service.entity.Member;
@Embeddable
public class MemberBettingId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @OneToOne
    @JoinColumn(name = "btId", referencedColumnName = "btId")
    private Battle battle;
}