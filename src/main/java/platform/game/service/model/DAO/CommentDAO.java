package platform.game.service.model.DAO;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Comment;
import platform.game.service.repository.CommentInfoRepository;

@Repository
public class CommentDAO {

    @Autowired
    private CommentInfoRepository commentInfoRepository;
    
    public ArrayList<Comment> commentList(int postId){
        System.out.println("CommentList 호출");
        System.out.println("postId : " + postId);

        ArrayList<Comment> lists = commentInfoRepository.findByPost_PostId(postId);

        return lists;
    }
}
