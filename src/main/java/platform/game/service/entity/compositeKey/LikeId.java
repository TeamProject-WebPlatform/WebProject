package platform.game.service.entity.compositeKey;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;

@Embeddable
@Data
public class LikeId implements Serializable {
    
    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "commentId", referencedColumnName = "commentId")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;


}