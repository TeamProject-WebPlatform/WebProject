package platform.game.service.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.idGenerator.IdGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member { 

    @Id
    private long memId; 
    private String memUserid; 
    private String memPw; 
    private String memNick; 
    private String memRoleCd;
    private String memEmail;
    private String memSteamid;
    private String memKakaoid;
    @ColumnDefault("0")
    private int memCurPoint;
    @ColumnDefault("0")
    private int memTotalPoint;
    @ColumnDefault("1")
    private int memLvl;
    @ColumnDefault("0")
    private int memAttend;
    @ColumnDefault("0")
    private int memGameCount;
    @ColumnDefault("0")
    private int memWinCount;
    @ColumnDefault("0")
    private int memLoseCount;
    @ColumnDefault("0")
    private int memPointRanking;
    @ColumnDefault("0")
    private int memLvlRanking;
    @ColumnDefault("0")
    private int memAttendRanking;
    @ColumnDefault("0")
    private String loginKindCd;
    private String memCertified;
    private String memCreatedAt;
    private String memDeletedAt;

}