package platform.game.service.entity;

import java.sql.Date;

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
    private int CommentId;

    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    @Column(columnDefinition = "TEXT NOT NULL DEFAULT ''")
    private String CommentContent;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int CommentLikeCnt;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int CommentDislikeCnt;
    private Date CreatedAt;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int CommentGrp;

    private Date UpdatedAt;
    private Date DeletedAt;

    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int CommentRepotCnt;
}
