package platform.game.service.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int memTotalGameCnt;
    private int memGameWinCnt;
    private int memGameLoseCnt;
    private String loginKindCd;
    private String memCertified;
    private String memCreatedAt;
    private String memDeletedAt;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<MemberBetting> memBettingList = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<PointHistory> memPointHistoryList = new ArrayList<>();

    public String getRole(String code) {
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