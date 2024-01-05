package platform.game.service.entity;

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
public class Shop {

    @Id
    private int ItemId;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String ItemCd;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String ItemNm;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String ItemInfo;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int ItemPrice;
}