package platform.game.service.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
}
