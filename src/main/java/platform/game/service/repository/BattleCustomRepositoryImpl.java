package platform.game.service.repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Battle;
import platform.game.service.entity.BattlePost;
import platform.game.service.entity.Comment;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberBetting;
import platform.game.service.entity.Post;
import platform.game.service.service.BattleScheduleService;
import platform.game.service.service.BettingService;
import platform.game.service.service.SendMessageService;
import platform.game.service.model.TO.BattleMemberTO;
import platform.game.service.model.TO.MemberBettingTO;

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
    private BattleScheduleService battleScheduleService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private UpdatePointHistoryImpl updatePointHistoryImpl;
    @Autowired
    private MemberInfoRepository memberInfoRepository;
    @Autowired
    private MemberBettingRepository memberBettingRepository;

    @Transactional
    @Override
    public int updateHostBetPoint(int btId, int point) {
        Query query = entityManager.createNativeQuery(
                "UPDATE battle " +
                        "SET bt_host_mem_bet_point = bt_host_mem_bet_point + :point, bt_host_mem_bet_cnt = bt_host_mem_bet_cnt + 1 "
                        +
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
                        "SET bt_client_mem_bet_point = bt_client_mem_bet_point + :point, bt_client_mem_bet_cnt = bt_client_mem_bet_cnt + 1 "
                        +
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
        } else
            return 0;

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

                long startTime = btp.getBtStartDt().getTime();
                long curTime = System.currentTimeMillis();
                long waiting = TimeUnit.MINUTES.toMillis(30);
                long targetTime = startTime + waiting;
                long delay = targetTime - curTime;

                bettingService.bettingSchedule(btId, btp.getBtStartDt());
                btp.setBettingFinTime(targetTime);
                try {
                    sendMessageService.sendMessageToChangeState(btId, "A", delay, new BattleMemberTO(client));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                // 다른 신청자들 포인트 반환 작업
                String[] applicants = updatedApplicantsStr.split("/");
                for (int i = 0; i < applicants.length; i++) {
                    String[] info = applicants[i].split(",");
                    if (info[1].equals("0")) {
                        long memId = Long.parseLong(info[0]);
                        updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50107", btp.getBtPostPoint());
                    }
                }
                // 배틀 시작안할시 자동으로 패배 처리
                try {
                    battleScheduleService.battleStartSchedule(btId, btp.getPostId(), btp.getBtStartDt());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백", e);
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
    public int controlBattle(int type, int btId, int postId) {
        Battle battle = battleRepository.findById(btId).orElse(null);
        BattlePost battlePost = battlePostRepository.findByPost_PostId(postId);
        if (battle == null) {
            return -1;
        }
        if (type == 1) {
            battle.setBtState("P");
            battleRepository.save(battle);
            try {
                sendMessageService.sendMessageToChangeState(btId, "P");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백1", e);
            }
        } else if (type == 2 || type == 3) {
            if (battle.getBtState().equals("P")) {
                battle.setBtState("H");
                battle.setBtEndDt(new Date());
                if (type == 2) {
                    battle.setBtResult("0");
                } else if (type == 3) {
                    battle.setBtResult("1");
                }
                battleRepository.save(battle);

                // 웹 소켓 메세지
                try {
                    sendMessageService.sendMessageToChangeState(btId, "H");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백2", e);
                }
            } else if (battle.getBtState().equals("C")) {
                battle.setBtEndDt(new Date());
                String hostChoice = "";
                String clientChoice = battle.getBtResult();
                if (type == 2) {
                    hostChoice = "0";
                } else if (type == 3) {
                    hostChoice = "1";
                }

                if (hostChoice.equals(clientChoice)) {
                    // 호스트와 클라이언트의 선택이 동일
                    battle.setBtState("T");
                    battleRepository.save(battle);

                    if (battleTerminate(battle) == -1) {
                        throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백3");
                    }

                    try {
                        sendMessageService.sendMessageToChangeState(btId, "T", battle.getBtResult(),
                                battlePost.getBtPostPoint());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백4", e);
                    }
                } else {
                    battle.setBtState("E");
                }
            }
        } else if (type == 4 || type == 5) {
            if (battle.getBtState().equals("P")) {
                battle.setBtState("C");
                battle.setBtEndDt(new Date());
                if (type == 4) {
                    battle.setBtResult("0");
                } else if (type == 5) {
                    battle.setBtResult("1");
                }
                battleRepository.save(battle);

                try {
                    sendMessageService.sendMessageToChangeState(btId, "C");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백5", e);
                }
            } else if (battle.getBtState().equals("H")) {
                battle.setBtEndDt(new Date());
                String hostChoice = battle.getBtResult();
                String clientChoice = "";
                if (type == 4) {
                    clientChoice = "0";
                } else if (type == 5) {
                    clientChoice = "1";
                }

                if (hostChoice.equals(clientChoice)) {
                    // 호스트와 클라이언트의 선택이 동일
                    battle.setBtState("T");
                    battleRepository.save(battle);
                    if (battleTerminate(battle) == -1) {
                        throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백6");
                    }

                    try {
                        sendMessageService.sendMessageToChangeState(btId, "T", battle.getBtResult(),
                                battlePost.getBtPostPoint());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백7", e);
                    }
                } else {
                    battle.setBtState("E");
                }
            }
        }
        return 1;
    }

    @Transactional
    @Override
    public int battleTerminate(Battle battle) {
        try {
            // 호스트 클라이언트 승패 증가
            String result = battle.getBtResult();
            Member host = battle.getHostMember();
            Member client = battle.getClientMember();
            host.setMemTotalGameCnt(host.getMemTotalGameCnt());
            client.setMemTotalGameCnt(client.getMemTotalGameCnt());

            if (result.equals("0")) {
                host.setMemGameWinCnt(host.getMemGameWinCnt() + 1);
                client.setMemGameLoseCnt(client.getMemGameLoseCnt() + 1);
            } else if (result.equals("1")) {
                host.setMemGameLoseCnt(host.getMemGameLoseCnt() + 1);
                client.setMemGameWinCnt(client.getMemGameWinCnt() + 1);
            }

            memberInfoRepository.save(host);
            memberInfoRepository.save(client);

            // 포인트 분배
            if (distributePoint(battle.getBtId()) == -1)
                return -1;
        } catch (Exception e) {
            System.out.println("battleTerminate() " + e.getMessage());
            return -1;
        }
        return 1;
    }

    @Transactional
    @Override
    public int distributePoint(int btId) {

        Battle battle = battleRepository.findById(btId).orElse(null);
        if (battle == null)
            return -1;
        BattlePost battlePost = battlePostRepository.findById(btId).orElse(null);
        if (battlePost == null)
            return -1;
        List<MemberBetting> betMemberList = battle.getMemBettingList();
        if (betMemberList == null)
            return -1;
        List<MemberBettingTO> betToList = new ArrayList<>();
        long hostPoint = battle.getBtHostMemBetPoint();
        long clientPoint = battle.getBtClientMemBetPoint();
        long totalPoint = hostPoint + clientPoint;
        double hostMul = totalPoint * 1.0 / hostPoint;
        double clientMul = totalPoint * 1.0 / clientPoint;
        // 호스트에 3000 클라에 1000 달려이씅면 총 4000 호스트MUL : 4/3, 클라 MUL : 4
        // 클라에 300 700 두명이 걸었으면 1200 2800

        if (battle.getBtResult().equals("0")) {
            // 호스트 승
            for (int i = 0; i < betMemberList.size(); i++) {
                MemberBetting mb = betMemberList.get(i);
                if (mb.getBetFlag() == 0) {
                    // 예측 성공
                    mb.setPointReceived("0");
                    mb.setPointDstb((long) Math.ceil(hostMul * mb.getBetPoint()));
                } else {
                    // 예측 실패
                    mb.setPointReceived("-1");
                    mb.setPointDstb(0);
                }
                betToList.add(new MemberBettingTO(battle, null, mb));
                memberBettingRepository.save(mb);
            }
        } else if (battle.getBtResult().equals("1")) {
            // 클라이언트 승
            for (int i = 0; i < betMemberList.size(); i++) {
                MemberBetting mb = betMemberList.get(i);
                if (mb.getBetFlag() == 1) {
                    // 예측 성공
                    mb.setPointReceived("0");
                    mb.setPointDstb((long) Math.ceil(clientMul * mb.getBetPoint()));

                } else {
                    // 예측 실패
                    mb.setPointReceived("-1");
                    mb.setPointDstb(0);
                }
                betToList.add(new MemberBettingTO(battle, null, mb));
                memberBettingRepository.save(mb);
            }
        }
        // successMemberBettingList를 웹소켓으로 보내줘야함.
        try {
            sendMessageService.sendMessageToDistributePoint(btId, betToList);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return 1;
    }

    @Transactional
    @Override
    public void terminateBetting(int btId) {
        Battle battle = battleRepository.findById(btId).orElse(null);
        if (battle == null) {
            return;
        }
        battle.setBtState("B");
        battleRepository.save(battle);

        try {
            sendMessageService.sendMessageToChangeState(btId, "B");
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
    public int[] writePost(long memId, String title, String game, String etcGame, String point, String content,
            Date ddDate,
            Date stDate) {
        // post에 추가
        // battle에 추가
        // battle_post에 추가
        // 날짜 2개, point, game_cd,post_id(post에 추가하고 확인),bt_id(battle에 추가하고 확인)
        int[] data = new int[2];
        Date now = new Date();
        Query query = null;
        System.out.println();
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
        System.out.println(1);
        long postId = (Long) entityManager.createNativeQuery("SELECT LAST_INSERT_ID() FROM post limit 1")
                .getSingleResult();
        data[0] = (int) postId;
        System.out.println(2);
        // BATTLE INSERT
        query = entityManager.createNativeQuery(
                "INSERT INTO battle " +
                        "(bt_id, bt_client_mem_bet_point, bt_end_dt, bt_host_mem_bet_point, bt_result, bt_client_mem_id, bt_host_mem_id, bt_client_mem_bet_cnt, bt_host_mem_bet_cnt, bt_state) "
                        +
                        "VALUES(0, 0, NULL, 0, NULL, NULL, :memId, 0, 0, 'N')");
        query.setParameter("memId", memId);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        System.out.println(3);
        long btId = (Long) entityManager.createNativeQuery("SELECT LAST_INSERT_ID() FROM battle limit 1")
                .getSingleResult();
        data[1] = (int) btId;
        System.out.println(4);
        // BATTLE_POST INSERT
        query = entityManager.createNativeQuery(
                "INSERT INTO battle_post " +
                        "(bt_post_applicants, bt_post_dead_line, bt_post_point, game_cd, etc_game_nm, post_id, bt_id, bt_start_dt) "
                        +
                        "VALUES('', :ddDate , :point, :gameCd,:etcGame, :postId, :btId , :stDate)");
        query.setParameter("ddDate", ddDate);
        query.setParameter("point", point);
        query.setParameter("gameCd", game);
        query.setParameter("etcGame", etcGame);
        query.setParameter("postId", postId);
        query.setParameter("btId", btId);
        query.setParameter("stDate", stDate);
        if (query.executeUpdate() != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        System.out.println(6);
        return data;
    }

    @Transactional
    @Override
    public int[] modifyPost(int postId, int btId, long memId, String title, String game, String etcGame, String point,
            String content,
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
                        "SET bt_post_dead_line=:ddDate, bt_post_point=:point, game_cd=:gameCd,etc_game_nm=:etc_game_nm, bt_start_dt=:stDate "
                        +
                        "WHERE post_id=:postId");
        query.setParameter("ddDate", ddDate);
        query.setParameter("point", point);
        query.setParameter("gameCd", game);
        query.setParameter("etc_game_nm", etcGame);
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
        // like DELETE
        query = entityManager.createNativeQuery(
                "DELETE FROM `like` " +
                        "WHERE post_id=:postId");
        query.setParameter("postId", postId);
        query.executeUpdate();
        // comment DELETE
        query = entityManager.createNativeQuery(
                "DELETE FROM comment " +
                        "WHERE post_id=:postId");
        query.setParameter("postId", postId);
        query.executeUpdate();

        System.out.println(4);
        // 배틀 신청 보류자들 포인트 반환
        if (!bp.getBtPostApplicants().equals("")) {
            String[] applicants = bp.getBtPostApplicants().split("/");
            for (int i = 0; i < applicants.length; i++) {
                String[] info = applicants[i].split(",");
                if (info[1].equals("0")) {
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

    @Override
    public List<Battle> getBattleListByCondition(String selectedGame, String selectedState, String searchValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Battle> query = cb.createQuery(Battle.class);

        Root<Battle> root = query.from(Battle.class);
        Join<Battle, BattlePost> bpJoin = root.join("btPost", JoinType.INNER); // INNER JOIN으로 설정
        Join<BattlePost, Post> postJoin = bpJoin.join("post", JoinType.INNER); // INNER JOIN으로 설정

        List<Predicate> predicates = new ArrayList<>();

        if (!selectedState.equals("ALL")) {
            predicates.add(cb.equal(root.get("btState"), selectedState));
        }

        if (!selectedGame.equals("30000")) {
            predicates.add(cb.equal(bpJoin.get("gameCd"), selectedGame));
        }
        if (!searchValue.isEmpty()) {
            predicates.add(cb.like(postJoin.get("postTitle"), "%" + searchValue + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("btId"))); // 내림차순

        List<Battle> result = entityManager.createQuery(query).getResultList();
        return result;
    }

    @Override
    public List<Battle> getBattleListByCondition(String selectedGame, String selectedState, long memId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Battle> query = cb.createQuery(Battle.class);

        Root<Battle> root = query.from(Battle.class);
        Join<Battle, BattlePost> bpJoin = root.join("btPost", JoinType.INNER); // INNER JOIN으로 설정

        List<Predicate> predicates = new ArrayList<>();

        if (!selectedState.equals("ALL")) {
            predicates.add(cb.equal(root.get("btState"), selectedState));
        }

        if (!selectedGame.equals("30000")) {
            predicates.add(cb.equal(bpJoin.get("gameCd"), selectedGame));
        }
        Predicate hostMemberPredicate = cb.equal(root.get("hostMember").get("memId"), memId);
        Predicate clientMemberPredicate = cb.equal(root.get("clientMember").get("memId"), memId);
        predicates.add(cb.or(hostMemberPredicate, clientMemberPredicate));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("btId"))); // 내림차순

        List<Battle> result = entityManager.createQuery(query).getResultList();
        return result;
    }

    @Override
    @Transactional
    public int receivePoint(long memId, int btId, int postId) {
        BattlePost bp = battlePostRepository.findById(postId).orElse(null);
        Battle bt = battleRepository.findByBtId(btId).orElse(null);
        if (bp == null || bt == null)
            return -1;
        int point = bp.getBtPostPoint() * 2;
        if (bt.getBtResult().equals("0")) {
            // 호스트 승
            if (bt.getHostMember().getMemId() != memId)
                return -1;
            if (bp.getBtPostPointReceived().equals("1"))
                return -1;
        } else if (bt.getBtResult().equals("1")) {
            // 클라 승
            if (bt.getClientMember().getMemId() != memId)
                return -1;
            if (bp.getBtPostPointReceived().equals("1"))
                return -1;
        }
        System.out.println(1);
        try {
            int currentPoint = updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50102", point);
            bp.setBtPostPointReceived("1");
            sendMessageService.sendMessageToChagePoint(memId, currentPoint, "50102");
        } catch (Exception e) {
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        }

        return 1;
    }

    @Override
    @Transactional
    public int receiveBettingPoint(long memId, int btId, int postId) {
        Query query = null;
        query = entityManager.createNativeQuery(
                "SELECT * FROM member_betting WHERE bt_id=:btId and mem_id=:memId", MemberBetting.class);
        query.setParameter("btId", btId);
        query.setParameter("memId", memId);
        MemberBetting mb = (MemberBetting) query.getSingleResult();

        try {
            int currentPoint = updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50004",
                    (int) mb.getPointDstb());
            mb.setPointReceived("1");
            sendMessageService.sendMessageToChagePoint(memId, currentPoint, "50004");
        } catch (Exception e) {
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");
        }

        return 1;
    }
}
