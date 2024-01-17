package platform.game.service.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Post;

@Repository
public interface PostInfoRepository extends JpaRepository<Post, Integer> {
        // 게시물 전체 가져오기
        // 게시판 별 게시물 가져오기 추가하기
        ArrayList<Post> findAll();

        // 게시물 찾기
        Post findByPostId(int postId);

        // 게시판에 맞는 리스트 찾기 찾기
        ArrayList<Post> findByBoardCdOrderByPostIdDesc(String boardCd);

        // 제목 검색
        ArrayList<Post> findByBoardCdAndPostTitleContainingOrderByPostIdDesc(String boardCd, String postTitle);

        // 내용 검색
        ArrayList<Post> findByBoardCdAndPostContentContainingOrderByPostIdDesc(String boardCd, String postContent);

        // 태그 검색
        ArrayList<Post> findByBoardCdAndPostTagsContainingOrderByPostIdDesc(String boardCd, String postTags);

        // 글쓴이 검색
        ArrayList<Post> findByBoardCdAndMember_MemIdOrderByPostIdDesc(String boardCd, int memId);

        // 제목 + 태그 검색
        ArrayList<Post> findByBoardCdAndPostTitleContainingAndPostTagsContainingOrderByPostIdDesc(String boardCd,
                        String postTitle,
                        String postTags);

        // 내용 + 태그 검색
        ArrayList<Post> findByBoardCdAndPostContentContainingAndPostTagsContainingOrderByPostIdDesc(String boardCd,
                        String postContent,
                        String postTags);

        // 글쓴이 + 태그 검색
        ArrayList<Post> findByBoardCdAndMember_MemIdAndPostTagsContainingOrderByPostIdDesc(String boardCd, int memId,
                        String postTags);

        // 조건 모두 검색
        ArrayList<Post> findByBoardCdAndPostTitleAndPostContentAndPostTagsOrderByPostIdDesc(String boardCd,
                        String postTitle, String postContent, String postTags);

        // 게시물 삭제하기
        Post deleteByPostId(int postId);

}
