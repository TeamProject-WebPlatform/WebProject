package platform.game.service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signinHistoryId;
    

    @Column(length = 15) // IPv4 주소는 최대 15자리까지입니다.
    private String memIp;
    // 날짜 저장
    private LocalDateTime createdAt;

    
    @Override
    public String toString() {
        return "SigninHistory{" +
                "member=" + member +
                ", signinHistoryId=" + signinHistoryId +
                ", memIp='" + memIp + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}   