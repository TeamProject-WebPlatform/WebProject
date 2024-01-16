package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import platform.game.service.entity.Member;

@Embeddable
public class PointHistoryId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @Column(name = "pointHistoryId")
    private int pointHistoryId;
}
