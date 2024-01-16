package platform.game.service.repository;

import java.util.ArrayList;
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

        // 날짜 기준 찾기
        ArrayList<Post> findByBoardCdOrderByCreatedAtDesc(String boardCd);

        // 제목 검색
        ArrayList<Post> findByBoardCdAndPostTitleContainingOrderByPostIdDesc(String boardCd, String postTitle);

        // 날짜별로 제목 검색
        ArrayList<Post> findByBoardCdAndPostTitleContainingOrderByCreatedAtDesc(String boardCd, String postTitle);

        // 내용 검색
        ArrayList<Post> findByBoardCdAndPostContentContainingOrderByCreatedAtDesc(String boardCd, String postContent);

        // 태그 검색
        ArrayList<Post> findByBoardCdAndPostTagsContainingOrderByCreatedAtDesc(String boardCd, String postTags);

        // 글쓴이 검색
        ArrayList<Post> findByBoardCdAndMember_MemIdOrderByCreatedAtDesc(String boardCd, long memId);

        // 제목 + 태그 검색
        ArrayList<Post> findByBoardCdAndPostTitleContainingAndPostTagsContainingOrderByCreatedAtDesc(String boardCd,
                        String postTitle,
                        String postTags);

        // 내용 + 태그 검색
        ArrayList<Post> findByBoardCdAndPostContentContainingAndPostTagsContainingOrderByCreatedAtDesc(String boardCd,
                        String postContent,
                        String postTags);

        // 글쓴이 + 태그 검색
        ArrayList<Post> findByBoardCdAndMember_MemIdAndPostTagsContainingOrderByCreatedAtDesc(String boardCd,
                        long memId,
                        String postTags);

        // 조건 모두 검색
        ArrayList<Post> findByBoardCdAndPostTitleAndPostContentAndPostTagsOrderByCreatedAtDesc(String boardCd,
                        String postTitle, String postContent, String postTags);

        // 게시물 삭제하기
        void deleteByPostId(int postId);

        long count();
}
