package platform.game.service.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Post;

@Repository
public interface PostInfoRepository extends JpaRepository<Post, Integer>{
    //게시물 전체 가져오기
    //게시판 별 게시물 가져오기 추가하기
    ArrayList<Post> findAll();
    //게시물 찾기
    Post findByPostId(int postId);
    //게시물 삭제하기
    Post deleteByPostId(int postId);
}
