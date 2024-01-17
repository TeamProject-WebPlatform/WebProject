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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    // 변경: 특정 사용자가 작성한 글의 개수 조회 (인스턴스 메서드로 변경)
    public int getPostCountByMember(List<Post> posts) {
        int count = 0;
        for (Post p : posts) {
            if (p.getMember() != null && p.getMember().getMemId() == this.member.getMemId()) {
                count++;
            }
        }
        System.out.println("사용자 " + this.member.getMemId() + "의 글 개수: " + (count + 1));
        return count;
    }

    // 변경: 첫 번째 글 작성 여부 확인 (인스턴스 메서드로 변경)
    public boolean isFirstPost(List<Post> posts) {
        return getPostCountByMember(posts) == 0;
    }

    // 변경: 5개 단위로 작성 여부 확인 (인스턴스 메서드로 변경)
    public boolean isMultipleOfFivePosts(List<Post> posts) {
        int postCount = getPostCountByMember(posts) + 1;
        return postCount > 0 && (postCount % 5 == 0);
    }
}
