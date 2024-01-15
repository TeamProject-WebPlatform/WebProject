package platform.game.service.entity;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.SigninHistoryId;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(SigninHistoryId.class)
public class SigninHistory {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memId")
    private Member member;
    
    @Id
    private Integer signinHistory;

    @Column(length = 15) // IPv4 주소는 최대 15자리까지입니다.
    private String memIp;
    // 날짜 저장
    private LocalDateTime createdAt;
    
}