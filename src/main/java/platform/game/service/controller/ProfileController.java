package platform.game.service.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Member;
import platform.game.service.entity.MemberProfile;
import platform.game.service.entity.MemberRanking;
import platform.game.service.model.TO.FavoriteGameTO;
import platform.game.service.repository.MemberFavoriteGameRepository;
import platform.game.service.repository.MemberItemInfoRepository;
import platform.game.service.repository.MemberProfileRepository;
import platform.game.service.repository.MemberRankingRepository;
import platform.game.service.service.MemberInfoDetails;
import platform.game.service.service.jwt.SecurityPassword;

@Controller
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.model",
        "platform.game.module.*" })
@RequestMapping("/profile")
@Transactional
public class ProfileController {

    @Autowired
    private MemberFavoriteGameRepository gameRepository;

    @Autowired
    private MemberProfileRepository profileRepository;

    @Autowired
    private MemberRankingRepository rankingRepository;

    @Autowired
    private MemberItemInfoRepository itemInfoRepository;

    @Autowired
    private SecurityPassword securityPassword;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView Profile() {

        ModelAndView mav = new ModelAndView("profile");

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
                List<MemberRanking> memberRanking = rankingRepository.findByMemId(member.getMemId());
                List<String> memberItems = itemInfoRepository.getHaveBadges(member.getMemId());
                mav.addObject("nickname", member.getMemNick());
                mav.addObject("memberProfile", memberProfile);
                mav.addObject("memberRanking", memberRanking);
                mav.addObject("currentPoint", member.getMemCurPoint());
                mav.addObject("memberItems", memberItems);
            }
        } else {
            System.out.println("멤버 없음");
        }

        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        mav.addObject("member", member);
        return mav;
    }

    @PostMapping("/favoritegame")
    public ResponseEntity<Integer> FavoriteGameRegister(@RequestBody FavoriteGameTO to, HttpServletResponse response) {
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        int flag = 0;
        int count = gameRepository.countByMemId(member.getMemId());

        if (count != 0) {
            gameRepository.GameModify(to.getFirstgame(), member.getMemId(), 1);
            gameRepository.GameModify(to.getSecondgame(), member.getMemId(), 2);
            gameRepository.GameModify(to.getThirdgame(), member.getMemId(), 3);
        } else {
            gameRepository.RegisterGame(1, member.getMemId(), to.getFirstgame());
            gameRepository.RegisterGame(2, member.getMemId(), to.getSecondgame());
            gameRepository.RegisterGame(3, member.getMemId(), to.getThirdgame());
            flag = 1;
        }

        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

    @GetMapping("/editprofile")
    public ModelAndView EditProfie() {

        ModelAndView mav = new ModelAndView("editprofile");
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());

        mav.addObject("member", member);
        mav.addObject("profile", memberProfile);

        return mav;
    }

    @PostMapping("/updateintroduce")
    public ResponseEntity<String> UpdateIntroduce(@RequestBody Map<String, String> introduce) {
        int flag = 0;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();

        String introduction = introduce.get("introduce");

        if (profileRepository.IntroduceModify(introduction, member.getMemId()) == 1) {
            flag = 1;
        }

        return ResponseEntity.ok(String.valueOf(flag));
    }

    @PostMapping("/changepassword")
    public ResponseEntity<String> ChangePassword(@RequestBody Map<String, String> password) {
        int flag = 0;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();

        String NewPassword = securityPassword.encode(password.get("ModifyPassword"));

        if (profileRepository.UpdatePassword(NewPassword, member.getMemId()) == 1) {
            flag = 1;
        }

        return ResponseEntity.ok(String.valueOf(flag));
    }

    @PostMapping("/changenick")
    public ResponseEntity<String> ChangeNick(@RequestBody Map<String, String> nickname) {
        int flag = 0;

        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();

        String NewNickname = nickname.get("Nickname");

        if (profileRepository.UpdateNick(NewNickname, member.getMemId()) == 1) {
            flag = 1;
        }

        return ResponseEntity.ok(String.valueOf(flag));
    }

    @GetMapping("/editmycard")
    public ModelAndView EditMyCard(){
        ModelAndView mav = new ModelAndView("editmycard");
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getMember();
        MemberProfile memberProfile = profileRepository.findProfileIntroByMemId(member.getMemId());
        List<Object[]> HeaderList = profileRepository.HaveHeaderList(member.getMemId());
        List<Object[]> CardList = profileRepository.HaveCardList(member.getMemId());
        List<Object[]> BadgeList = profileRepository.HaveBadgeList(member.getMemId());

        mav.addObject("member", member);
        mav.addObject("memberProfile", memberProfile);
        mav.addObject("header", HeaderList);
        mav.addObject("card", CardList);
        mav.addObject("badge", BadgeList);

        return mav;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> ProfileImage(@RequestPart("image") MultipartFile image) {
        int flag=0;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        .getMember();

        String upload = "C:\\teamp\\WebProject\\src\\main\\resources\\static\\img\\profileimage\\";
        String filename = member.getMemId() + "_" + image.getOriginalFilename();

        try {
            // 프로필 사진 저장 경로 폴더 // 없을 시 폴더 생성
            if(!new File(upload).exists()){
                new File(upload).mkdir();
            }

            File DuplicateFile = findExistingFile(member.getMemId()+"");

            if(DuplicateFile!=null){
                DuplicateFile.delete();
            }

            image.transferTo(new File(upload+filename));
            if (profileRepository.UpdateProfileImage(filename,member.getMemId())==1){
                flag = 1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } 
        return ResponseEntity.ok(String.valueOf(flag));
    }


    @PostMapping("/headerprofile")
    public ResponseEntity<String> HeaderProfile(@RequestBody Map<String,String> header){
        int flag=0;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getMember();

        String NewHeader = header.get("Header");
        System.out.println(NewHeader);
        if(profileRepository.UpdateProfileHeader(NewHeader,member.getMemId())==1){
            flag = 1;
        }

        return ResponseEntity.ok(String.valueOf(flag));
    }

    @PostMapping("/cardprofile")
    public ResponseEntity<String> CardProfile(@RequestBody Map<String,String> card){
        int flag=0;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getMember();

        String NewCard = card.get("Card");
        if(profileRepository.UpdateProfileCard(NewCard,member.getMemId())==1){
            flag = 1;
        }

        return ResponseEntity.ok(String.valueOf(flag));
    }

    @PostMapping("/repbadgeprofile")
    public ResponseEntity<String> RedBadgeProfile(@RequestBody Map<String,String> badge){
        int flag=0;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getMember();

        String NewRepBadge = badge.get("RepBadge");
        if(profileRepository.UpdateProfileRepBadge(NewRepBadge,member.getMemId())==1){
            flag = 1;
        }

        return ResponseEntity.ok(String.valueOf(flag));
    }

    @PostMapping("/badgelist")
    public ResponseEntity<String> BadgeList(@RequestBody Map<String,List<String>> badges){
        int flag=0;
        Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getMember();

        List<String> badgeList = badges.get("BadgeList");
        System.out.println(badgeList);
        return ResponseEntity.ok(String.valueOf(flag));
    }

    // 파일이 있는지 여부 확인
    private File findExistingFile(String memberId) {

        String upload = "C:\\teamp\\WebProject\\src\\main\\resources\\static\\img\\profileimage";
        File[] files = new File(upload).listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith(memberId)) {
                    return file;
                }
            }
        }
        return null; // 동일한 memberId로 시작하는 파일이 없으면 null 반환
    }
}
