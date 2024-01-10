package platform.game.service.model.TO;

import java.util.ArrayList;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import platform.game.service.entity.Comment;

@Data
@Getter
@Setter
public class CommentTO {
    private Comment comment;
    private ArrayList<CommentTO> replies;

    public CommentTO(Comment comment) {
        this.comment = comment;
        this.replies = new ArrayList<>();
    }
}
