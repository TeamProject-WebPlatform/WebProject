package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.MemberItemId;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MemberItemId.class)
public class MemberItem {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memId")
    private Member memId;

    @Id
    // @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private Integer memItemId;

    @Column(columnDefinition = "CHAR(5) NOT NULL DEFAULT ''")
    private int itemCd;

    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT 'N'")
    private String itemApplyYN;

    @Column(columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private String boughtAt;

}
