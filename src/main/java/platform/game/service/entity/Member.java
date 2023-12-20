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
    private int memCurPoint;
    private int memTotalPoint;
    private int memLvl;
    private int memAttend;
    private int memGameCount;
    private int memWinCount;
    private int memLoseCount;
    private int memPointRanking;
    private int memLvlRanking;
    private int memAttendRanking;
    private String loginKindCd;
    private String memCertified;
    private String memCreatedAt;
    private String memDeletedAt;
    
    public String getRole(String code){
        switch (code) {
            case "10001":
                return "ROLE_SUPERADMIN";
            case "10002":
                return "ROLE_ADMIN";
            case "10003":
                return "ROLE_USER";
            default:
                return "ROLE_USER";
        }
    }
}