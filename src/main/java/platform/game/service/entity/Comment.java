package platform.game.service.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String commentContent;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int commentLikeCnt;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int commentDislikeCnt;
    
    private Date createdAt;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int commentGrp;

    private Date updatedAt;
    private Date deletedAt;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int commentRepotCnt;
}
