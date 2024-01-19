package platform.game.service.repository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Battle;
import platform.game.service.entity.BattlePost;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.Post;
import platform.game.service.service.BettingService;
import platform.game.service.service.BettingStateChangeService;
import platform.game.service.model.TO.BattleMemberTO;

@EnableAsync
@Repository
public class BattleCustomRepositoryImpl implements BattleCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PostInfoRepository postInfoRepository;
    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private CommentInfoRepository commentInfoRepository;
    @Autowired
    private BattlePostRepository battlePostRepository;
    @Autowired
    private BettingService bettingService;
    @Autowired
    private BettingStateChangeService bettingStateChangeService;
    @Autowired
    private UpdatePointHistoryImpl updatePointHistoryImpl;

    @Transactional
    @Override
    public int updateHostBetPoint(int btId, int point) {
        Query query = entityManager.createNativeQuery(
                "UPDATE battle " +
                "SET bt_host_mem_bet_point = bt_host_mem_bet_point + :point, bt_host_mem_bet_cnt = bt_host_mem_bet_cnt + 1 " +
                "WHERE bt_id = :btId");
        query.setParameter("point", point);
        query.setParameter("btId", btId);
        int flag = query.executeUpdate();
        if (flag != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        return flag;
    }
    
    @Transactional
    @Override
    public int updateClientBetPoint(int btId, int point) {
        Query query = entityManager.createNativeQuery(
            "UPDATE battle " +
            "SET bt_client_mem_bet_point = bt_client_mem_bet_point + :point, bt_client_mem_bet_cnt = bt_client_mem_bet_cnt + 1 " +
            "WHERE bt_id = :btId");
        query.setParameter("point", point);
        query.setParameter("btId", btId);
        int flag = query.executeUpdate();
        if (flag != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        return flag;
    }
    
    @Transactional
    @Override
    public int insertComment(int postId, String content, Member member) {
        Post post = postInfoRepository.findByPostId(postId);
        Comment comment = Comment.builder()
                .post(post)
                .commentContent(content)
                .createdAt(new Date())
                .member(member)
                .build();

        commentInfoRepository.save(comment);

        // 댓글 카운트 추가
        post.setPostCommentCnt(post.getPostCommentCnt() + 1);
        postInfoRepository.save(post);

        return 0;
    }

    @Transactional
    @Override
    public int insertComment(int postId, String content, int parentCommentId, Member member) {
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
        post.setPostCommentCnt(post.getPostCommentCnt() + 1);
        postInfoRepository.save(post);

        return 0;
    }

    @Transactional
    @Override
    public int deleteComment(int commentId) {

        Optional<Comment> opc = commentInfoRepository.findById(commentId);
        Comment comment = null;
        if (opc.isPresent())
            comment = opc.get();
        else
            return 0;
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
        if (optionalBattle.isPresent()) {
            btp = optionalBattle.get().getBtPost();
        } else return 0;

        // 데드라인 체크
        if (btp.getBtPostDeadLine().before(new Date())) {
            // 데드라인 지남.
            return -1;
        }
        // 신청 체크
        String applicantsStr = btp.getBtPostApplicants();
        String[] applicants = applicantsStr.split("/");
        if (applicants == null || applicants[0].equals("")) {

        } else {
            for (int i = 0; i < applicants.length; i++) {
                long applicantId = Long.parseLong(applicants[i].split(",")[0]);
                if (applicantId == memId) {
                    // 이미 신청
                    return -2;
                }
            }
        }
        // 신청자 업데이트
        applicantsStr += String.format("%d,%d,%s/", memId, 0, new Date());
        Query query = entityManager.createNativeQuery(
                "UPDATE battle_post " +
                        "SET bt_post_applicants = :applicantsStr " +
                        "WHERE post_id = :postId");
        query.setParameter("applicantsStr", applicantsStr);
        query.setParameter("postId", postId);

        int flag = query.executeUpdate();
        if (flag != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        // 신청자 포인트 차감
        int point = btp.getBtPostPoint();
        updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50106", -point);
        return flag;
    }

    @Transactional
    @Override
    public int manageRequest(long requester, int isAccept, int btId) {
        try {
            Optional<Battle> optionalBattle = battleRepository.findById(btId);
            BattlePost btp = optionalBattle.map(Battle::getBtPost).orElse(null);
            if (btp == null) {
                return 0;
            }
            String applicantsStr = btp.getBtPostApplicants();
            String requesterStr = String.valueOf(requester);
            int index = applicantsStr.indexOf(requesterStr);
            StringBuilder temp = new StringBuilder();

            temp.append(applicantsStr, 0, index + requesterStr.length() + 1)
                    .append(isAccept == 0 ? "1" : "2")
                    .append(applicantsStr, index + requesterStr.length() + 2, applicantsStr.length());
            String updatedApplicantsStr = temp.toString();
            btp.setBtPostApplicants(updatedApplicantsStr);
            
            // 거절한 사람 베팅 포인트 반환
            updatePointHistoryImpl.insertPointHistoryByMemId(requester, "50107", btp.getBtPostPoint());
            
            if (isAccept == 0) {
                // 신청자 수락
                Battle battle = optionalBattle.get();
                Member client = entityManager.getReference(Member.class, requester);
                battle.setClientMember(client);
                battle.setBtState("A");
                CompletableFuture<Long[]> fData = bettingService.bettingSchedule(btId,btp.getBtStartDt());
                Long[] data = fData.get();
                long targetTime = data[0];
                long delay = data[1];
                btp.setBettingFinTime(targetTime);
                try{
                    bettingStateChangeService.sendMessageToChangeState(btId, "A",delay,new BattleMemberTO(client));
                }catch(Exception e){
                    System.out.println(e.getMessage());
                    throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백", e);
                }
                // 다른 신청자들 포인트 반환 작업
                String[] applicants = updatedApplicantsStr.split("/");
                for(int i =0;i<applicants.length;i++){
                    String[] info = applicants[i].split(",");
                    if(info[1].equals("0")){
                        long memId = Long.parseLong(info[0]);
                        updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50107", btp.getBtPostPoint());
                    }
                }
            }
            battlePostRepository.save(btp);
        } catch (Exception e) {
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백", e);
        }
        return 1;
    }
    @Transactional
    @Override
    public void terminateBetting(int btId){
        Battle battle = battleRepository.findById(btId).orElse(null);
        if(battle == null){
            return;
        }
        battle.setBtState("B");
        battleRepository.save(battle);

        try {
            bettingStateChangeService.sendMessageToChangeState(btId, "B");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public Object[] like(long memId, String type, int postId, int commentId, int like) {
        Object[] flag = new Object[] { 0, 0 };

        Query query = entityManager.createNativeQuery(
                "INSERT INTO `like` " +
                        "(mem_id, post_id, comment_id, like_yn, created_at) " +
                        "VALUES(:memId, :postId, :commentId, :likeYN, :now)");
        query.setParameter("memId", memId);
        query.setParameter("postId", postId);
        query.setParameter("commentId", commentId);
        query.setParameter("likeYN", like == 1 ? "Y" : "N");
        query.setParameter("now", new Date());

        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        int dislike = 1;
        if (like == 1) {
            like = 1;
            dislike = 0;
        } else if (like == -1) {
            like = 0;
            dislike = 1;
        }

        // like, dislike 수 구하기
        if (type.equals("P")) {
            // 추천수 갱신
            query = entityManager.createNativeQuery(
                    "UPDATE post SET post_like_cnt = post_like_cnt + :like, post_dislike_cnt = post_dislike_cnt + :dislike WHERE post_id = :postId");
            query.setParameter("postId", postId);
            query.setParameter("like", like);
            query.setParameter("dislike", dislike);
            if (query.executeUpdate() != 1)
                throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

            // 추천수 받아오기
            query = entityManager.createNativeQuery(
                    "SELECT post_like_cnt, post_dislike_cnt FROM post WHERE post_id = :postId");
            query.setParameter("postId", postId);

            flag = (Object[]) query.getSingleResult();
        } else if (type.equals("C")) {
            // 추천수 갱신
            query = entityManager.createNativeQuery(
                    "UPDATE comment SET comment_like_cnt = comment_like_cnt + :like, comment_dislike_cnt = comment_dislike_cnt + :dislike WHERE comment_id = :commentId");
            query.setParameter("commentId", commentId);
            query.setParameter("like", like);
            query.setParameter("dislike", dislike);
            if (query.executeUpdate() != 1)
                throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

            // 추천수 받아오기
            query = entityManager.createNativeQuery(
                    "SELECT comment_like_cnt, comment_dislike_cnt FROM comment WHERE comment_id = :commentId");
            query.setParameter("commentId", commentId);

            flag = (Object[]) query.getSingleResult();
        }

        return flag;
    }

    @Transactional
    @Override
    public int[] writePost(long memId, String title, String game, String point, String content, Date ddDate,
            Date stDate) {
        // post에 추가
        // battle에 추가
        // battle_post에 추가
        // 날짜 2개, point, game_cd,post_id(post에 추가하고 확인),bt_id(battle에 추가하고 확인)
        int[] data = new int[2];
        Date now = new Date();
        Query query = null;

        // POST INSERT
        query = entityManager.createNativeQuery(
                "INSERT INTO post " +
                        "(post_id, board_cd, created_at, deleted_at, post_content, post_hit, post_tags, post_title, updated_at, mem_id, post_comment_cnt, post_dislike_cnt, post_like_cnt, post_report_cnt) "
                        +
                        "VALUES(0, :board_cd, :created_at, NULL, :post_content, 0, NULL, :post_title, NULL, :memId, 0, 0, 0, 0)");
        query.setParameter("board_cd", "20101");
        query.setParameter("created_at", now);
        query.setParameter("post_content", content);
        query.setParameter("post_title", title);
        query.setParameter("memId", memId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        long postId = (Long) entityManager.createNativeQuery("SELECT LAST_INSERT_ID() FROM post").getSingleResult();
        data[0] = (int) postId;

        // BATTLE INSERT
        query = entityManager.createNativeQuery(
                "INSERT INTO battle " +
                        "(bt_id, bt_client_mem_bet_point, bt_end_dt, bt_host_mem_bet_point, bt_result, bt_client_mem_id, bt_host_mem_id, bt_client_mem_bet_cnt, bt_host_mem_bet_cnt, bt_state) "
                        +
                        "VALUES(0, 0, NULL, 0, NULL, NULL, :memId, 0, 0, 'N')");
        query.setParameter("memId", memId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        long btId = (Long) entityManager.createNativeQuery("SELECT LAST_INSERT_ID() FROM battle").getSingleResult();
        data[1] = (int) btId;

        // BATTLE_POST INSERT
        query = entityManager.createNativeQuery(
                "INSERT INTO battle_post " +
                        "(bt_post_applicants, bt_post_dead_line, bt_post_point, game_cd, post_id, bt_id, bt_start_dt) "
                        +
                        "VALUES('', :ddDate , :point, :gameCd, :postId, :btId , :stDate)");
        query.setParameter("ddDate", ddDate);
        query.setParameter("point", point);
        query.setParameter("gameCd", game);
        query.setParameter("postId", postId);
        query.setParameter("btId", btId);
        query.setParameter("stDate", stDate);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        return data;
    }

    @Transactional
    @Override
    public int[] modifyPost(int postId, int btId, long memId, String title, String game, String point, String content,
            Date ddDate, Date stDate) {
        // post에 추가
        // battle에 추가
        // battle_post에 추가
        // 날짜 2개, point, game_cd,post_id(post에 추가하고 확인),bt_id(battle에 추가하고 확인)
        int[] data = new int[2];
        Date now = new Date();
        Query query = null;

        // POST INSERT
        query = entityManager.createNativeQuery(
                "UPDATE post " +
                        "SET post_content=:content, post_title=:title, updated_at=:now, mem_id=:memId " +
                        "WHERE post_id=:postId");
        query.setParameter("content", content);
        query.setParameter("title", title);
        query.setParameter("now", new Date());
        query.setParameter("memId", memId);
        query.setParameter("postId", postId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        query.setParameter("memId", memId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        // bp 업데이트

        // BATTLE_POST INSERT
        query = entityManager.createNativeQuery(
                "UPDATE battle_post " +
                        "SET bt_post_dead_line=:ddDate, bt_post_point=:point, game_cd=:gameCd, bt_start_dt=:stDate " +
                        "WHERE post_id=:postId");
        query.setParameter("ddDate", ddDate);
        query.setParameter("point", point);
        query.setParameter("gameCd", game);
        query.setParameter("postId", postId);
        query.setParameter("stDate", stDate);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        data[0] = postId;
        data[1] = btId;
        return data;
    }

    @Transactional
    @Override
    public int deletePost(int postId, int btId) {
        Query query = null;
        query = entityManager.createNativeQuery(
                "SELECT bt_client_mem_id FROM battle WHERE bt_id=:btId");
        query.setParameter("btId", btId);
        List<?> resultList = query.getResultList();
        // System.out.println(resultList.size());
        if (resultList.isEmpty()) {
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        }
        // 포인트 반환
        BattlePost bp = battlePostRepository.findById(postId).orElse(null);
        Battle bt = battleRepository.findByBtId(btId).orElse(null);
        int point = bp.getBtPostPoint();
        long memId = bt.getHostMember().getMemId();
        updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50105", point);

        // comment DELETE
        query = entityManager.createNativeQuery(
                "DELETE FROM comment " +
                        "WHERE post_id=:postId");
        query.setParameter("postId", postId);
        query.executeUpdate();

        System.out.println(4);
        // 배틀 신청 보류자들 포인트 반환
        if(!bp.getBtPostApplicants().equals("")){
            String[] applicants = bp.getBtPostApplicants().split("/");
            for(int i =0;i<applicants.length;i++){
                String[] info = applicants[i].split(",");
                if(info[1].equals("0")){
                    long requester = Long.parseLong(info[0]);
                    updatePointHistoryImpl.insertPointHistoryByMemId(requester, "50107", bp.getBtPostPoint());
                }
            }
        }
        

        // bp DELETE
        query = entityManager.createNativeQuery(
                "DELETE FROM battle_post " +
                        "WHERE bt_id=:btId");
        query.setParameter("btId", btId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");


        // POST DELETE
        query = entityManager.createNativeQuery(
                "DELETE FROM post " +
                        "WHERE post_id=:postId");
        query.setParameter("postId", postId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        query = entityManager.createNativeQuery(
                "DELETE FROM battle " +
                        "WHERE bt_id=:btId");
        query.setParameter("btId", btId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        

        return 1;
    }
}
