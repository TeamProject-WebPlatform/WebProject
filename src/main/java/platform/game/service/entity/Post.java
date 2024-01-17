package platform.game.service.entity;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import platform.game.service.service.MemberInfoDetails;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    private int postId;
    private String boardCd;
    private String postTitle;
    private String postContent;
    private int postHit;
    private int postLikeCnt;
    private int postDislikeCnt;
    private int postCommentCnt;
    private int postReportCnt;
    private String postTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
