package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Crawling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CrawlingId;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String CrawlingTitle;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String CrawlingDate;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String CrawlingImageUrl;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String CrawlingBoardLing;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String CrawlingContent;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT 'N'")
    private String InterfaceYn;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT 'N'")
    private String CrawlingBoardCd;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT 'N'")
    private String CrawlingPostTag;
}
