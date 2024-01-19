package platform.game.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfile {

    @Id
    @Column(name = "memId")
    private long memId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @Column(name = "profile_intro")
    private String profileIntro;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT 'x'")
    private String profileImage;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT 'x'")
    private String profileHeader;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT 'x'")
    private String profileCard;

    @Column(columnDefinition = "varchar(100) NOT NULL DEFAULT 'x'")
    private String ProfileRepBadge;

    @Column(columnDefinition = "varchar(255) NOT NULL DEFAULT 'x, x, x, x, x, x, x, x, x'")
    private String ProfileBadgeList;
}
