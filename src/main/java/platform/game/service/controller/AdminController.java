package platform.game.service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberFavoriteGame;
import platform.game.service.model.TO.AdminFavoriteGameTO;
import platform.game.service.model.TO.FavoriteGameTO;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberFavoriteGameRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.repository.PostInfoRepository;
import platform.game.service.service.MemberInfoDetails;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.service.repository",
        "platform.game.service.model" })
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @Autowired
    private PostInfoRepository postInfoRepository;

    @Autowired
    private MemberFavoriteGameRepository memberFavoriteGameRepository;

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @GetMapping("/list")
    public ModelAndView admin() {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("관리자 페이지 호출");

        // 현재 작성되어 있는 게시판 글 수(각각의 게시판 글 갯수 보여주기)
        long postCount = postInfoRepository.count();
        // 게시판 종류별 게시판 글 수
        String[] noticePostCount = new String[6];
        int j = 0;
        for (int i = 20001; i < 20007; i++) {
            String z = Integer.toString(i); // 단순 int를 string으로 변환
            noticePostCount[j] = postInfoRepository.countByBoardCd(z);
            j++;
        }

        // 현재 회원가입이 되어있는 사용자 인원수 가져오기
        long userNum = memberInfoRepository.count();

        // 현재 회원가입이 되어있는 사용자 인원수 가져오기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        System.out.println("총 방문자 수: " + visitCount.getRemark1());
        System.out.println("어제 방문자 수: " + visitCount.getRemark2());
        System.out.println("오늘 방문자 수: " + visitCount.getRemark3());

        // 가장 많은 사람이 선호하는 게임 분류하기
        List<String> favoriteGame = memberFavoriteGameRepository.findMostCommonGameCd();
        int rows = favoriteGame.size();
        int cols = 2;
        String[][] mostFavoriteGame = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            String entry = favoriteGame.get(i);
            String[] parts = entry.split(",");
            mostFavoriteGame[i][0] = parts[0]; // game_cd
            mostFavoriteGame[i][1] = parts[1]; // count

            // 케이스 수정필요
            switch (mostFavoriteGame[i][0]) {
                case "30001":
                    mostFavoriteGame[i][0] = "LeagueofLegends";
                    break;
                case "30002":
                    mostFavoriteGame[i][0] = "BattleGround";
                    break;
                case "30003":
                    mostFavoriteGame[i][0] = "Steam";
                    break;
                case "30004":
                    mostFavoriteGame[i][0] = "LeagueofLegends";
                    break;
                case "30005":
                    mostFavoriteGame[i][0] = "BattleGround";
                    break;
                default:
                    mostFavoriteGame[i][0] = "Steam";
                    break;
            }
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("userNum", userNum);
        modelAndView.addObject("mostFavoriteGame", mostFavoriteGame);
        modelAndView.addObject("noticePostCount", noticePostCount);
        modelAndView.addObject("postCount", postCount);
        modelAndView.addObject("visitCount", visitCount);
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

        return modelAndView;
    }

    // 사용자 검색
    @GetMapping("/list_search")
    public ModelAndView findMember(@RequestParam String memInfo_type,
            @RequestParam String memInfo_value) {

        // 현재 작성되어 있는 게시판 글 수(각각의 게시판 글 갯수 보여주기)
        long postCount = postInfoRepository.count();
        // 게시판 종류별 게시판 글 수
        String[] noticePostCount = new String[6];
        int j = 0;
        for (int i = 20001; i < 20007; i++) {
            String z = Integer.toString(i); // 단순 int를 string으로 변환
            noticePostCount[j] = postInfoRepository.countByBoardCd(z);
            j++;
        }

        // 현재 회원가입이 되어있는 사용자 인원수 가져오기
        long userNum = memberInfoRepository.count();

        // 현재 회원가입이 되어있는 사용자 인원수 가져오기
        CommonCode visitCount = commonCodeRepository.findByCdOrderByCd("99001");
        System.out.println("총 방문자 수: " + visitCount.getRemark1());
        System.out.println("어제 방문자 수: " + visitCount.getRemark2());
        System.out.println("오늘 방문자 수: " + visitCount.getRemark3());

        // 가장 많은 사람이 선호하는 게임 분류하기
        List<String> favoriteGame = memberFavoriteGameRepository.findMostCommonGameCd();
        int rows = favoriteGame.size();
        int cols = 2;
        String[][] mostFavoriteGame = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            String entry = favoriteGame.get(i);
            String[] parts = entry.split(",");
            mostFavoriteGame[i][0] = parts[0]; // game_cd
            mostFavoriteGame[i][1] = parts[1]; // count

            // 케이스 수정필요
            switch (mostFavoriteGame[i][0]) {
                case "30001":
                    mostFavoriteGame[i][0] = "LeagueofLegends";
                    break;
                case "30002":
                    mostFavoriteGame[i][0] = "BattleGround";
                    break;
                case "30003":
                    mostFavoriteGame[i][0] = "Overwatch";
                    break;
                case "30004":
                    mostFavoriteGame[i][0] = "Valorant";
                    break;
                case "30005":
                    mostFavoriteGame[i][0] = "FIFA";
                    break;
                default:
                    mostFavoriteGame[i][0] = "Steam";
                    break;
            }
        }

        // 여기서부터 검색 시작
        Optional<Member> member = Optional.empty();

        // 사용자 검색
        switch (memInfo_type) {
            case "memId":
                long memId = Long.parseLong(memInfo_value);
                member = memberInfoRepository.findByMemId(memId);
                // System.out.println(member.get().getMemId());
                break;

            case "memUserId":
                member = memberInfoRepository.findByMemUserid(memInfo_value);
                // System.out.println(member.get().getMemId());
                break;

            case "memNick":
                member = memberInfoRepository.findByMemNick(memInfo_value);
                // System.out.println(member.get().getMemId());
                break;

            default:
                break;
        }

        System.out.println("검색한 사용자 정보 호출");
        // 회원 권한 코드에 따른 이름
        Optional<CommonCode> commonCode = commonCodeRepository.findByCd(member.get().getMemRoleCd());

        // 선호 게임 이름
        MemberFavoriteGame[] memFavGame = memberFavoriteGameRepository.findByMemId(member.get().getMemId());
        String[] memFG = new String[3];
        for (int i = 0; i < 3; i++) {
            commonCode = commonCodeRepository.findByCd(memFavGame[i].getGameCd());
            memFG[i] = commonCode.get().getJobGbn();
            // System.out.println("우선순위 : " + memFavGame[i].getFavGameId() + "게임 번호: " +
            // memFavGame[i].getGameCd() + "게임 이름 : " + memFG[i]);
        }

        commonCode = commonCodeRepository.findByCd(member.get().getMemRoleCd());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin_search");
        modelAndView.addObject("member", member.get());
        modelAndView.addObject("userNum", userNum);
        modelAndView.addObject("commonCode", commonCode.get());
        modelAndView.addObject("mostFavoriteGame", mostFavoriteGame);
        modelAndView.addObject("noticePostCount", noticePostCount);
        modelAndView.addObject("memFG", memFG);
        modelAndView.addObject("postCount", postCount);
        modelAndView.addObject("visitCount", visitCount);
        modelAndView.addObject("totalCount", visitCount.getRemark1());
        modelAndView.addObject("todayCount", visitCount.getRemark3());

        return modelAndView;
    }

    // 회원 권한 관리
    @GetMapping("modify_role_cd")
    public String modifyMemRoleCd(@RequestParam long memId,
            @RequestParam String memRoleCd) {
        System.out.println("사용자 권한 수정 호출");

        Member member = memberInfoRepository.findByMemIdOrderByMemIdDesc(memId);
        member.setMemRoleCd(memRoleCd);

        memberInfoRepository.save(member);

        System.out.println("저장완료");

        return "redirect:../admin/list";
    }

    @PostMapping("favoritegame")
    public ResponseEntity<Integer> FavoriteGameRegister(@RequestBody AdminFavoriteGameTO to,
            HttpServletResponse response) {
        System.out.println("선호게임 수정 호출");

        int flag = 0;
        int count = memberFavoriteGameRepository.countByMemId(to.getSelectMemId());
        // System.out.println(count);

        if (count != 0) {
            memberFavoriteGameRepository.GameModify(to.getFirstgame(), to.getSelectMemId(), 1);
            memberFavoriteGameRepository.GameModify(to.getSecondgame(), to.getSelectMemId(), 2);
            memberFavoriteGameRepository.GameModify(to.getThirdgame(), to.getSelectMemId(), 3);
        } else {
            memberFavoriteGameRepository.RegisterGame(1, to.getSelectMemId(), to.getFirstgame());
            memberFavoriteGameRepository.RegisterGame(2, to.getSelectMemId(), to.getSecondgame());
            memberFavoriteGameRepository.RegisterGame(3, to.getSelectMemId(), to.getThirdgame());
            flag = 1;
        }

        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

}