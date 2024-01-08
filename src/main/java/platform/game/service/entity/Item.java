package platform.game.service.entity;

// Item 클래스
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    private String itemCd;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String itemNm;

    @Column(columnDefinition = "CHAR(7) NOT NULL DEFAULT ''")
    private String itemKindCd;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String itemEffect;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String itemInfo;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int itemCost;
}
