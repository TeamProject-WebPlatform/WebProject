package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import platform.game.service.entity.Comment;

public interface CommentInfoRepository extends JpaRepository<Comment, Integer>{
    // Optional<Comment> findByPost(int postId);

    ArrayList<Comment> findByPost_PostId(int postId);
}