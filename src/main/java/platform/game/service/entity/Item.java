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
    @Column(columnDefinition = "CHAR(5) NOT NULL DEFAULT ''")
    private int itemCd;
    
    @Column(columnDefinition = "CHAR(3) NOT NULL DEFAULT ''")
    private String itemKindCd;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String itemNm;
    
    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String itemInfo;
    
    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String itemEffect;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int itemCost;
}
