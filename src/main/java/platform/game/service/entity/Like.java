package platform.game.service.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.entity.compositeKey.LikeId;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(LikeId.class)
public class Like {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    private Post post;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commentId")
    private Comment comment;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memId")
    private Member member;
    
    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT Y")
    private String likeYn;
    private Date createdAt;
}
