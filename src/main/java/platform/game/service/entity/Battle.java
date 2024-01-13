package platform.game.service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Battle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int btId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "btHostMemId", referencedColumnName = "memId")
    private Member hostMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "btClientMemId", referencedColumnName = "memId")
    private Member clientMember;

    private long btHostMemBetPoint;
    private long btClientMemBetPoint;
    private int btHostMemBetCnt;
    private int btClientMemBetCnt;
    private Date btEndDt;
    private String btResult;
    private String btState;

    @OneToOne(fetch = FetchType.LAZY,mappedBy="battle")
    private BattlePost btPost;
    @OneToMany(fetch = FetchType.LAZY,mappedBy="battle")
    private List<MemberBetting> memBettingList = new ArrayList<>();
}
