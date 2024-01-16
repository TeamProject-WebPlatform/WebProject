package platform.game.service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import platform.game.service.entity.Member;
import platform.game.service.entity.MemberFavoriteGame;
import platform.game.service.repository.MemberFavoriteGameRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.repository.PostInfoRepository;

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

    @GetMapping("/list")
    public ModelAndView admin() {
        // 모델에 필요한 데이터를 추가하고, 템플릿 이름을 반환
        System.out.println("관리자 페이지 호출");

        // 현재 작성되어 있는 게시판 글 수(각각의 게시판 글 갯수 보여주기)
        long postCount = postInfoRepository.count();
        // 게시판 종류별 게시판 글 수
        long noticePostCount = postInfoRepository.count();

        // 현재 회원가입이 되어있는 사용자 인원수 가져오기
        long userNum = memberInfoRepository.count();

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
                default:
                    mostFavoriteGame[i][0] = "All";
                    break;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print("배열: " + mostFavoriteGame[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("총 회원수: " + userNum);
        System.out.println("총 게시판 글 수: " + postCount);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("userNum", userNum);

        return modelAndView;
    }

    // 사용자 검색
    @GetMapping("/list_search")
    public ModelAndView findMember(@RequestParam String memInfo_type,
            @RequestParam String memInfo_value) {

        Optional<Member> member = Optional.empty();

        // 사용자 검색
        switch (memInfo_type) {
            case "memId":
                long memId = Long.parseLong(memInfo_value);
                member = memberInfoRepository.findByMemId(memId);
                System.out.println(member.get().getMemId());
                break;

            case "memUserId":
                member = memberInfoRepository.findByMemUserid(memInfo_value);
                System.out.println(member.get().getMemId());
                break;

            case "memNick":
                member = memberInfoRepository.findByMemNick(memInfo_value);
                System.out.println(member.get().getMemId());
                break;

            default:
                break;
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("member", member.get());
        // System.out.println(member.get().getMemRoleCd());

        return modelAndView;
    }

    // 회원 권한 관리
    @GetMapping("modify_role_cd")
    public ModelAndView modifyMemRoleCd(@RequestParam long memId,
            @RequestParam String memRoleCd) {
        System.out.println("사용자 권한 수정 호출");

        Optional<Member> member = memberInfoRepository.findByMemId(memId);
        member.get().setMemRoleCd(memRoleCd);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        return modelAndView;
    }

}