package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import platform.game.service.entity.Comment;

public interface CommentInfoRepository extends JpaRepository<Comment, Integer> {
    ArrayList<Comment> findByPost_PostId(int postId);
    ArrayList<Comment> findByMember_MemId(long memId);

    void deleteByPost_PostId(int postId);

    int countByPost_PostId(int postId);

    void deleteByCommentGrp(int commentGrp);
    void deleteByCommentId(int commentId);
}