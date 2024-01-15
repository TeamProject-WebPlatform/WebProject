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
public class MemberCard {

    @Id
    private long memId;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String profileImage;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String profileHeader;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String profileCard;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String ProfileBadges1;
    
    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String ProfileBadges2;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String ProfileBadges3;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String ProfileBadges4;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT ''")
    private String ProfileBadges5;
}
