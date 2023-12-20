package platform.game.service.entity;

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
public class CommonCode {
    @Id
    private String comCd;  // 5자리 숫자
    private String comCdCat; // 3자리 숫자
    private String comCdDesc;
    private String comCdParam1; 
    private String comCdParam2; 
    private String comCdParam3; 
    private String comCdParam4; 
    private String comCdParam5; 
}
