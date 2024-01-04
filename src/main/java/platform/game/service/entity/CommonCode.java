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
    private String cd; // 5자리 숫자
    private String jobGbn;
    private String cdNm; // 3자리 숫자
    private String cdDetails;
    private String remark1;
    private String remark2;
    private String remark3;
    private String remark4;
    private String remark5;
}
