package platform.game.service.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Battle;
import platform.game.service.entity.BattlePost;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;

@Repository
public class BattleCustomRepositoryImpl implements BattleCustomRepository{
    
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PostInfoRepository postInfoRepository;
    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentInfoRepository commentInfoRepository;


    @Transactional
    @Override
    public int insertComment(int postId,String content, Member member){
        Post post = postInfoRepository.findByPostId(postId);
        Comment comment = Comment.builder()
                            .post(post)
                            .commentContent(content)
                            .createdAt(new Date())
                            .member(member)
                            .build();

        commentInfoRepository.save(comment);
        
        // 댓글 카운트 추가
        post.setPostCommentCnt(post.getPostCommentCnt()+1);
        postInfoRepository.save(post);

        return 0;
    }

    @Transactional
    @Override
    public int insertComment(int postId,String content, int parentCommentId, Member member){
        Post post = postInfoRepository.findByPostId(postId);
        Comment comment = Comment.builder()
                            .post(post)
                            .commentContent(content)
                            .commentGrp(parentCommentId)
                            .createdAt(new Date())
                            .member(member)
                            .build();

        commentInfoRepository.save(comment);
        
        // 댓글 카운트 추가
        post.setPostCommentCnt(post.getPostCommentCnt()+1);
        postInfoRepository.save(post);
        
        return 0;
    }
    @Transactional
    @Override
    public int deleteComment(int commentId){

        Optional<Comment> opc = commentInfoRepository.findById(commentId);
        Comment comment = null;
        if(opc.isPresent()) comment = opc.get();
        else return 0;
        comment.setCommentContent("삭제된 댓글 입니다.");
        comment.setDeletedAt(new Date());
        commentInfoRepository.save(comment);
        return 1;
    }
    @Transactional
    @Override
    public int reqeustBattle(long memId, int btId, int postId) {
        Optional<Battle> optionalBattle = battleRepository.findById(btId);

        BattlePost btp = null;
        if(optionalBattle.isPresent()) {
            btp = optionalBattle.get().getBtPost();
        }else return 0;
               
        // 데드라인 체크       
        if(btp.getBtPostDeadLine().getTime()<new Date().getTime()){
            // 데드라인 지남.
            return -1;
        }
        // 신청 체크
        String applicantsStr = btp.getBtPostApplicants();
        String[] applicants = applicantsStr.split("/");
        for(int i = 0; i< applicants.length;i++){
            long applicantId = Long.parseLong(applicants[i].split(",")[0]);
            if(applicantId == memId){
                // 이미 신청
                return -2;
            }
        }
        // 신청자 업데이트
        applicantsStr += String.format("%d,%d,%s/",memId,0,new Date());
        Query query = entityManager.createNativeQuery(
                "UPDATE battle_post " +
                        "SET bt_post_applicants = :applicantsStr " +
                        "WHERE post_id = :postId");
        query.setParameter("applicantsStr", applicantsStr);
        query.setParameter("postId", postId);

        int flag = query.executeUpdate();
        if (flag != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        
        return flag;
    }

    @Transactional
    @Override
    public Object[] like(long memId, String type, int postId,int commentId, int like) {
        Object[] flag = new Object[]{0,0};

        Query query = entityManager.createNativeQuery(
                "INSERT INTO `like` " +
                        "(mem_id, post_id, comment_id, like_yn, created_at) " +
                        "VALUES(:memId, :postId, :commentId, :likeYN, :now)");
        query.setParameter("memId", memId);
        query.setParameter("postId", postId);
        query.setParameter("commentId", commentId);
        query.setParameter("likeYN", like==1?"Y":"N");
        query.setParameter("now", new Date());

        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        
        int dislike = 1;
        if(like==1) {
            like=1; dislike=0;
        }else if(like==-1) {
            like=0; dislike =1;
        }

        // like, dislike 수 구하기
        if(type.equals("P")){
            // 추천수 갱신
            query = entityManager.createNativeQuery(
                "UPDATE post SET post_like_cnt = post_like_cnt + :like, post_dislike_cnt = post_dislike_cnt + :dislike WHERE post_id = :postId"
            );            
            query.setParameter("postId", postId);
            query.setParameter("like", like);
            query.setParameter("dislike", dislike);
            if (query.executeUpdate() != 1)
                throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

            // 추천수 받아오기
            query = entityManager.createNativeQuery(
                "SELECT post_like_cnt, post_dislike_cnt FROM post WHERE post_id = :postId"
            );
            query.setParameter("postId", postId);

            flag = (Object[])query.getSingleResult();
        }else if(type.equals("C")){
            // 추천수 갱신
            query = entityManager.createNativeQuery(
                "UPDATE comment SET comment_like_cnt = comment_like_cnt + :like, comment_dislike_cnt = comment_dislike_cnt + :dislike WHERE comment_id = :commentId"
            );
            query.setParameter("commentId", commentId);
            query.setParameter("like", like);
            query.setParameter("dislike", dislike);
            if (query.executeUpdate() != 1)
                throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

            // 추천수 받아오기
            query = entityManager.createNativeQuery(
                "SELECT comment_like_cnt, comment_dislike_cnt FROM comment WHERE comment_id = :commentId"
            );
            query.setParameter("commentId", commentId);

            flag = (Object[])query.getSingleResult();
        }

        return flag;
    }
}
