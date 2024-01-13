package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import platform.game.service.entity.Member;

@Embeddable
public class MemberItemId implements Serializable{

    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member memId;

    @Column(name = "memItemId")
    private int memItemId;

}
