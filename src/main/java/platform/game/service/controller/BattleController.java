package platform.game.service.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;
import platform.game.service.action.BattleCardAction;
import platform.game.service.entity.Battle;
import platform.game.service.entity.BattlePost;
import platform.game.service.entity.Comment;
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberProfile;
import platform.game.service.entity.Post;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.model.TO.BattleTO;
import platform.game.service.model.TO.BettingInfoTO;
import platform.game.service.model.TO.CommentTO;
import platform.game.service.repository.BattleCustomRepositoryImpl;
import platform.game.service.repository.BattleRepository;
import platform.game.service.repository.CommentInfoRepository;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.repository.MemberProfileRepository;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.repository.UpdatePointHistoryImpl;
import platform.game.service.service.MemberInfoDetails;

@EnableAsync
@Controller
@ComponentScan(basePackages = { "platform.game.service.action", "platform.game.service.repository",
        "platform.game.service.model" })
@RequestMapping("/battle")
public class BattleController {

    @Autowired
    BattleCardAction battleCardAction;
    @Autowired
    PostInfoRepository postInfoRepository;
    @Autowired
    CommentInfoRepository commentInfoRepository;
    @Autowired
    MemberInfoRepository memberInfoRepository;
    @Autowired
    BattleRepository battleRepository;
    @Autowired
    BattleCustomRepositoryImpl battleCustomRepositoryImpl;
    @Autowired
    private UpdatePointHistoryImpl updatePointHistoryImpl;
    @Autowired
    MemberProfileRepository profileRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @RequestMapping("")
    public ModelAndView battle(@RequestParam("page") int page, 
        @RequestParam(value = "selectedListCnt", defaultValue = "10") int selectedListCnt,
        @RequestParam(value = "selectedGame", defaultValue = "30000") String selectedGame,
        @RequestParam(value = "selectedState", defaultValue = "ALL") String selectedState,
        @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
        @RequestParam(value = "mybattle", defaultValue = "false") Boolean myBattle) {
        
        long id = 0;
        ModelAndView mav = new ModelAndView("battle");
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                id = member.getMemId();
                mav.addObject("memId", id);
                mav.addObject("memberProfile", memberProfile);
            }
        }
        // 리스트 정보취득
        mav.addObject("page", page);
        mav.addObject("startPage",((page-1)/6)*6 + 1);
        mav.addObject("selectedListCnt", selectedListCnt);
        mav.addObject("selectedGame", selectedGame);
        mav.addObject("selectedState", selectedState);
        mav.addObject("searchValue", searchValue);
        // 리스트 생성
        Object[] o = battleCardAction.getBattleList(id,page,selectedListCnt,selectedGame,selectedState,searchValue,myBattle);
        List[] battleList = (List[])o[0];
        int lastPage = (int)o[1];
        List<BattleTO> battleTOList = battleList[0];
        List<BattlePointTO> battlePointTOList = battleList[1];

        // battleTO에 있는 호스트와 클라이언트 유저들 프로필 가져오기
        List<String> battleTOHostProfileImage = new ArrayList<String>();
        List<String> battleTOHostProfileBorder = new ArrayList<String>();
        List<String[]> battleTOHostProfileBadge = new ArrayList<String[]>();
        List<String> battleTOHostProfileRep = new ArrayList<String>();

        List<String> battleTOClientProfileImage = new ArrayList<String>();
        List<String> battleTOClientProfileBorder = new ArrayList<String>();
        List<String[]> battleTOClientProfileBadge = new ArrayList<String[]>();
        List<String> battleTOClientProfileRep = new ArrayList<String>();


        for(int i=0; i<battleTOList.size();i++){
            BattleTO to = battleTOList.get(i);

            String HostProfileImage = profileRepository.BattleProfileImage(to.getHostNick());
            String HostProfileBorder = profileRepository.BattleProfileBorder(to.getHostNick());
            String HostProfileBadgeList = profileRepository.BattleProfileBadgeList(to.getHostNick());
            String HostProfileRep = profileRepository.BattleProfileRepBadge(to.getHostNick());
            String[] HostProfileBadge = HostProfileBadgeList.split(", ");

            System.out.println(to.getHostNick() + " : " + to.getClientNick());


            battleTOHostProfileImage.add(HostProfileImage);
            battleTOHostProfileBorder.add(HostProfileBorder);
            battleTOHostProfileBadge.add(HostProfileBadge);
            battleTOHostProfileRep.add(HostProfileRep);

            if (to.getClientNick()!=null){
                String ClientProfileImage = profileRepository.BattleProfileImage(to.getClientNick());
                String ClientProfileBorder = profileRepository.BattleProfileBorder(to.getClientNick());
                String ClientProfileBadgeList = profileRepository.BattleProfileBadgeList(to.getClientNick());
                String ClientProfileRep = profileRepository.BattleProfileRepBadge(to.getClientNick());
                String[] ClientProfileBadge = ClientProfileBadgeList.split(", ");

                battleTOClientProfileImage.add(ClientProfileImage);
                battleTOClientProfileBorder.add(ClientProfileBorder);
                battleTOClientProfileBadge.add(ClientProfileBadge);
                battleTOClientProfileRep.add(ClientProfileRep);
            } else {
                String ClientProfileImage = null;
                String ClientProfileBorder = null;
                String ClientProfileRep = null;
                String ClientProfileBadgeList = "x, x, x, x, x, x, x, x, x";
                String[] ClientProfileBadge = ClientProfileBadgeList.split(", ");

                battleTOClientProfileImage.add(ClientProfileImage);
                battleTOClientProfileBorder.add(ClientProfileBorder);
                battleTOClientProfileBadge.add(ClientProfileBadge);
                battleTOClientProfileRep.add(ClientProfileRep);
            }

        }
        
        mav.addObject("lastPage", lastPage);
        mav.addObject("battleTOList", battleTOList);
        mav.addObject("battlePointTOList", battlePointTOList);

        mav.addObject("HostProfileImage", battleTOHostProfileImage);
        mav.addObject("HostProfileBorder", battleTOHostProfileBorder);
        mav.addObject("HostProfileBadge", battleTOHostProfileBadge);
        mav.addObject("HostProfileRep", battleTOHostProfileRep);

        mav.addObject("ClientProfileImage", battleTOClientProfileImage);
        mav.addObject("ClientProfileBorder", battleTOClientProfileBorder);
        mav.addObject("ClientProfileBadge", battleTOClientProfileBadge);
        mav.addObject("ClientProfileRep", battleTOClientProfileRep);
        
        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        mav.addObject("totalCount", visitCount.getRemark1());
        mav.addObject("todayCount", visitCount.getRemark3());

        return mav;
    }

    @RequestMapping("/view")
    public ModelAndView listView(@RequestParam("postId") int postId, @RequestParam("btId") int btId) {
        ModelAndView mav = new ModelAndView("battle_view");
        long id = 0;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                id = member.getMemId();
                mav.addObject("memId", id);
                mav.addObject("memberProfile", memberProfile);
            }
        }
        Post post = new Post();
        post = postInfoRepository.findByPostId(postId);
        if(post!=null) {
            post.setPostHit(post.getPostHit()+1);
            postInfoRepository.save(post);
        }
        Object[] battleTOs = battleCardAction.getBattleTO(id, postId, btId);

        BattleTO bto = (BattleTO) battleTOs[0];
        BattlePointTO pto = (BattlePointTO) battleTOs[1];

        // 글쓴이(호스트) 프로필 가져오기
        MemberProfile HostProfile = profileRepository.BattleProfile(bto.getHostNick());
        String HostProfileBadgeList = HostProfile.getProfileBadgeList();
        String[] HostProfileBadges = HostProfileBadgeList.split(", ");


        ArrayList<Comment> comments = commentInfoRepository.findByPost_PostId(postId);
        ArrayList<CommentTO> commentTree = buildCommentTree(comments);

        commentTree.sort(Comparator.comparing(commentTO -> commentTO.getComment().getCreatedAt().getTime())); // 최신이 아래로
                                                                                                              // 내려감.
        // 시간 비교
        Long startDate = bto.getStartDt().getTime();
        Long currnetDate = new Date().getTime();
        if(startDate > currnetDate) mav.addObject("isAfterStartDt", false);
        else mav.addObject("isAfterStartDt", true);

        Long startDeadline = startDate + 1000 * 60 * 30;
        mav.addObject("startDeadlineDt", new Date(startDeadline));
        
        String[][] tmp = bto.getApplicants() != null ? bto.getApplicants() : null;
        if (tmp == null) {
            mav.addObject("applicants", "");
        } else {
            String[][] applicants = new String[tmp.length][5];
            for (int i = 0; i < tmp.length; i++) {
                String[] s = tmp[i];
                Member member = memberInfoRepository.findById(Long.parseLong(s[0])).isPresent()
                        ? memberInfoRepository.findById(Long.parseLong(s[0])).get()
                        : null;
                applicants[i][0] = s[0]; // memId 
                applicants[i][1] = s[1]; // 보류 상태
                applicants[i][2] = s[2]; // 신청 시간
                applicants[i][3] = member != null ? member.getMemNick() : null; // 닉네임
                applicants[i][4] = member != null ? String.valueOf(member.getMemLvl()) : null; // 레벨
            }
            mav.addObject("applicants", applicants);
        }

        mav.addObject("bto", bto);
        mav.addObject("pto", pto);
        mav.addObject("post", post);
        mav.addObject("commentTree", commentTree);

        // 호스트 프로필 전달
        mav.addObject("profile", HostProfile);
        mav.addObject("badgelist", HostProfileBadges);

        // 사이드바에 방문자 수 보여주기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        mav.addObject("totalCount", visitCount.getRemark1());
        mav.addObject("todayCount", visitCount.getRemark3());
        return mav;
    }

    @RequestMapping("/write")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView writePage(@RequestParam("postId") int postId, @RequestParam("btId") int btId) {
        ModelAndView mav = new ModelAndView("battle_write");
        long id = 0;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                String memberProfileBadgeList = profileRepository.BattleProfileBadgeList(member.getMemNick());
                String[] memberProfileBadge = memberProfileBadgeList.split(", ");

                mav.addObject("nickname", member.getMemNick());
                mav.addObject("currentPoint", member.getMemCurPoint());
                id = member.getMemId();
                mav.addObject("memId", id);
                mav.addObject("level", member.getMemLvl());
                mav.addObject("memberProfile", memberProfile);
                mav.addObject("badgelist", memberProfileBadge);
            }
        }
        if (postId != -1 && btId != -1) {
            // modify
            Post post = postInfoRepository.findById(postId).get();
            Battle battle = battleRepository.findById(btId).get();
            BattlePost battlePost = battle.getBtPost();
            if (battle.getClientMember() != null || post.getMember().getMemId() != id) {
                // 클라이언트가 있으므로 수정 불가
                ModelAndView failMav = new ModelAndView(new RedirectView("/view?postId=" + postId + "&btId=" + btId));
                return failMav;
            }
            LocalDateTime ddDate = battlePost.getBtPostDeadLine().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime stDate = battlePost.getBtStartDt().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            mav.addObject("isModify", true);
            mav.addObject("postId", postId);
            mav.addObject("btId", btId);
            mav.addObject("title", post.getPostTitle());
            mav.addObject("content", post.getPostContent());
            mav.addObject("point", battlePost.getBtPostPoint());
            mav.addObject("game", battlePost.getGameCd());
            mav.addObject("etcGameNm",battlePost.getEtcGameNm()==null?" ":battlePost.getEtcGameNm());
            // ddDate의 연, 월, 일, 시간, 분
            mav.addObject("ddYear", ddDate.getYear());
            mav.addObject("ddMonth", ddDate.getMonthValue());
            mav.addObject("ddDay", ddDate.getDayOfMonth());
            mav.addObject("ddHour", ddDate.getHour());
            mav.addObject("ddMinute", ddDate.getMinute());

            // stDate의 연, 월, 일, 시간, 분
            mav.addObject("stYear", stDate.getYear());
            mav.addObject("stMonth", stDate.getMonthValue());
            mav.addObject("stDay", stDate.getDayOfMonth());
            mav.addObject("stHour", stDate.getHour());
            mav.addObject("stMinute", stDate.getMinute());
        }
        return mav;
    }

    @PostMapping("/write_ok")
    public String writePost(HttpServletRequest request) {
        String isModify = request.getParameter("isModify");
        long memId = Long.parseLong(request.getParameter("memId"));
        String title = request.getParameter("title");
        String game = request.getParameter("selectedGame");
        String etcGame = request.getParameter("selectedGameInput");
        if(etcGame==null) etcGame="";
        String point = request.getParameter("point");
        String content = request.getParameter("content");

        String ddyear = request.getParameter("ddyear");
        String ddmonth = request.getParameter("ddmonth");
        String ddday = request.getParameter("ddday");
        String ddhour = request.getParameter("ddhour");
        String ddminute = request.getParameter("ddminute");
        String ddDateString = ddyear + "-" + ddmonth + "-" + ddday + " " + ddhour + ":" + ddminute;

        String styear = request.getParameter("styear");
        String stmonth = request.getParameter("stmonth");
        String stday = request.getParameter("stday");
        String sthour = request.getParameter("sthour");
        String stminute = request.getParameter("stminute");
        String stDateString = styear + "-" + stmonth + "-" + stday + " " + sthour + ":" + stminute;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date ddDate = null;
        Date stDate = null;
        try {
            ddDate = dateFormat.parse(ddDateString);
            stDate = dateFormat.parse(stDateString);
        } catch (Exception e) {
            e.printStackTrace();
            // 아마 에러가 안뜰 것으로 예상해서 롤백 구현x
        }
        int[] data = new int[2];
        try {
            if (isModify.equals("true")) {
                int dPoint = Integer.parseInt(request.getParameter("dPoint"));
                int postId = Integer.parseInt(request.getParameter("postId"));
                int btId = Integer.parseInt(request.getParameter("btId"));
                data = battleCustomRepositoryImpl.modifyPost(postId, btId, memId, title, game,etcGame, point, content, ddDate,
                        stDate);
                if (dPoint < 0) {
                    // 포인트를 더썼으니까
                    updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50204", dPoint);
                } else if (dPoint > 0) {
                    // 포인트를 줄였으니까
                    updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50203", dPoint);
                }
            } else {
                data = battleCustomRepositoryImpl.writePost(memId, title, game,etcGame, point, content, ddDate, stDate);
                updatePointHistoryImpl.insertPointHistoryByMemId(memId, "50201", -Integer.parseInt(point));
            }
        } catch (Exception e) {
            System.out.println("글쓰기 에러");
            System.out.println(e.getMessage());
        }
        // return "redirect:/battle/view?postId=" + data[0] + "&btId=" + data[1];
        return "redirect:/battle?page=1";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deletPost(@RequestParam("postId") int postId, @RequestParam("btId") int btId) {
        int flag = 0;
        // battlePost 삭제 -> battle 삭제, post 삭제
        try {
            flag = battleCustomRepositoryImpl.deletePost(postId, btId);

        } catch (Exception e) {
            return "-1";
        }
        // flag = 1 정상
        // flag = -1 삭제 불가(클라이언트 존재)
        return String.valueOf(flag);
    }

    @RequestMapping("/comment")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String writeComment(@RequestParam("postId") int postId, @RequestParam("btId") int btId,
            @RequestParam("content") String content) {
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        } else
            return "redirect:./battle/view?postId=" + postId + "&btId=" + btId;

        try {
            battleCustomRepositoryImpl.insertComment(postId, content, member);
        } catch (Exception e) {
        }

        return "redirect:./view?postId=" + postId + "&btId=" + btId;
    }

    @RequestMapping("/recomment")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String writeReplyComment(@RequestParam("postId") int postId, @RequestParam("btId") int btId,
            @RequestParam("parent_comment_id") int commentId,
            @RequestParam("content") String content) {
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
        }
        try {
            battleCustomRepositoryImpl.insertComment(postId, content, commentId, member);
        } catch (Exception e) {
        }

        // 댓글이 등록된 후에 해당 게시물로 이동
        return "redirect:./view?postId=" + postId + "&btId=" + btId;
    }

    @PostMapping("/delcomment")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String deleteComment(@RequestParam("commentId") int commentId) {
        int flag = 0;
        try {
            flag = battleCustomRepositoryImpl.deleteComment(commentId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = -1;
        }

        return String.valueOf(flag);
    }

    @PostMapping("/reqeust")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String reqeustBattle(@RequestParam("memId") long memId,
            @RequestParam("btId") int btId,
            @RequestParam("postId") int postId) {
        int flag = 0;
        try {
            flag = battleCustomRepositoryImpl.reqeustBattle(memId, btId, postId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = -1;
        }
        return String.valueOf(flag);
    }

    @PostMapping("/reqeustManage")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String manageRequest(@RequestParam("requester") long memId, @RequestParam("isAccept") int isAccept,
            @RequestParam("btId") int btId) {
        int flag = 0;
        try {
            flag = battleCustomRepositoryImpl.manageRequest(memId, isAccept, btId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = -1;
        }

        return String.valueOf(flag);
    }
    @PostMapping("/controlBattle")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String controlBattle(@RequestParam("type") int type,@RequestParam("memId") long memId, @RequestParam("btId") int btId,
            @RequestParam("postId") int postId) {
        int flag = 0;
        try {
            battleCustomRepositoryImpl.controlBattle(type,btId,postId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return String.valueOf(flag);
    }
    @PostMapping("/like")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public Object[] like(@RequestParam("type") String type, @RequestParam("postId") int postId,
            @RequestParam("commentId") int commentId, @RequestParam("like") int like) {
        Object[] flag = new Object[] { 0, 0 };
        long memId = 0;
        Member member = null;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                memId = member.getMemId();
            }
        }

        try {
            flag = battleCustomRepositoryImpl.like(memId, type, postId, commentId, like);
        } catch (DataIntegrityViolationException e) {
            // 이미 한 투표
            flag = new Object[] { -1, -1 };
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = new Object[] { -2, -2 };
        }
        return flag;
    }
    @PostMapping("/receivePoint")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String receivePoint(@RequestParam("btId") int btId,@RequestParam("postId") int postId) {
        Member member = null;
        long memId=0;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                memId = member.getMemId();
            }
        }
        int flag = 0;
        try {
            flag = battleCustomRepositoryImpl.receivePoint(memId,btId,postId);
        } catch (Exception e) {
            flag = -1;
        }
        return String.valueOf(flag);
    }
    @PostMapping("/receiveBettingPoint")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String receiveBettingPoint(@RequestParam("btId") int btId,@RequestParam("postId") int postId) {
        Member member = null;
        long memId=0;
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                memId = member.getMemId();
            }
        }
        int flag = 0;
        try {
            flag = battleCustomRepositoryImpl.receiveBettingPoint(memId,btId,postId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = -1;
        }
        return String.valueOf(flag);
    }
    private ArrayList<CommentTO> buildCommentTree(ArrayList<Comment> comments) {
        Map<Integer, CommentTO> commentNodeMap = new HashMap<>();

        // 댓글을 CommentNode로 변환하여 Map에 저장
        for (Comment comment : comments) {
            CommentTO commentTO = new CommentTO(comment);
            commentNodeMap.put(comment.getCommentId(), commentTO);
        }

        // 부모-자식 관계를 설정하여 계층 구조 생성
        for (Comment comment : comments) {
            CommentTO commentNode = commentNodeMap.get(comment.getCommentId());

            if (comment.getCommentGrp() != 0) {
                CommentTO parentCommentNode = commentNodeMap.get(comment.getCommentGrp());
                parentCommentNode.getReplies().add(commentNode);
            }
        }

        // 최상위 댓글 노드를 찾아 반환
        ArrayList<CommentTO> rootNodes = new ArrayList<>();
        for (CommentTO commentNode : commentNodeMap.values()) {
            if (commentNode.getComment().getCommentGrp() == 0) {
                rootNodes.add(commentNode);
            }
        }

        return rootNodes;
    }

}
