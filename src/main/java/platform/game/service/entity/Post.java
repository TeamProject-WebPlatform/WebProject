package platform.game.service.entity;

import java.util.ArrayList;
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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    private String postTitle;
    private String postContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;

    private int postHit;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private int postLikeCnt;
    private int postDislikeCnt;
    private int postCommentCnt;
    private int postReportCnt;
    private String postTags;
    private String boardCd;



    // 추가: 특정 사용자가 작성한 글의 개수 조회
    public static int getPostCountByMember(Member member, ArrayList<Post> posts) {
        int count = 0;
        for (Post post : posts) {
            if (post.getMember().equals(member)) {
                count++;
            }
        }
        return count;
    }

    // 추가: 특정 사용자가 작성한 글 목록 조회
    public static ArrayList<Post> getPostsByMember(Member member, ArrayList<Post> posts) {
        ArrayList<Post> userPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getMember().equals(member)) {
                userPosts.add(post);
            }
        }
        return userPosts;
    }
}
