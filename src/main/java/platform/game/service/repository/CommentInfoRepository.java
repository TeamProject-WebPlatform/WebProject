package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import platform.game.service.entity.Comment;

public interface CommentInfoRepository extends JpaRepository<Comment, Integer>{
    ArrayList<Comment> findByPost_PostId(int postId);
    
    void deleteByPost_PostId(int postId);
}