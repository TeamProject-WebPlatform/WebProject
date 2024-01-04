package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import platform.game.service.entity.Comment;

<<<<<<< HEAD
public interface CommentInfoRepository extends JpaRepository<Comment, Integer>{
    // Optional<Comment> findByPost(int postId);

    ArrayList<Comment> findByPost_PostId(int postId);
    Comment save(Comment comment);
=======
public interface CommentInfoRepository extends JpaRepository<Comment, Integer> {
    ArrayList<Comment> findByPost_PostId(int postId);

    void deleteByPost_PostId(int postId);

    int countByPost_PostId(int postId);

    void deleteByCommentId(int commentId);
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
}