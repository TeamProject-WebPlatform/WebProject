package platform.game.service.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import platform.game.service.action.MailAction;
import platform.game.service.action.SignAction;
import platform.game.service.entity.AuthRequest;
import platform.game.service.entity.Member;
import platform.game.service.entity.PointHistory;
import platform.game.service.entity.SigninHistory;
import platform.game.service.model.DAO.UserDAO;
import platform.game.service.model.TO.UserSignTO;
import platform.game.service.model.TO.KakaoTO.KakaoOAuthTokenTO;
import platform.game.service.action.IpAction;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.repository.SigninHistoryRepository;
import platform.game.service.repository.UpdatePointHistoryImpl;
import platform.game.service.service.MemberInfoDetails;
import platform.game.service.service.SigninHistoryService;
import platform.game.service.service.jwt.SecurityPassword;

@RestController
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.model",
        "platform.game.module.*" })
@RequestMapping("/login")
public class LoginController {
    // 로그인과 회원가입

    @Autowired
    UserDAO userDAO;

    @Value("${domain}")
    String domain;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SecurityPassword securityPassword;

    @Autowired
    private SignAction signAction;

    // 로그인시 포인트 적립
    @Autowired
    private MemberInfoRepository MemberRepository;

    @Autowired
    private SigninHistoryRepository signinHistoryRepository;

    // @Qualifier("UpdatePointHistoryImpl")
    @Autowired
    private UpdatePointHistoryImpl updatePointHistoryImpl;

    @Autowired
    private SigninHistoryService signinHistoryService;

    @GetMapping("")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    // 회원가입 요청
    @PostMapping("/signup_ok")
    public int handleSignup(@RequestBody UserSignTO userSignup, HttpServletResponse response) {
        Cookie cookie = signAction.signUp(userSignup, 0);

        if (cookie != null) {
            // 성공
            response.addCookie(cookie);
            return 0;
        } else {
            return 1;
        }
    }

    // 이메일 인증 요청
    @PostMapping("/mail_ok")
    public int mail_ok(@RequestBody UserSignTO userSignup) {
        MailAction mailAction = new MailAction(javaMailSender);

        String toEmail = userSignup.getMemEmail();

        int number = mailAction.createNumber();
        // 메일 내용
        String subject = "GAMMOA 회원가입의 인증번호 입니다";
        String content = "<h1>인증 번호는 <br><span>" + number + "</span> 입니다.</h1>";
        Boolean success = mailAction.sendMail(toEmail, "new User", subject, content);
        if (!success)
            number = -1;
        // 리턴 number 값을 반환
        return number;
    }

    // 로그인 요청 시 DB 내 ID, PW 유무 검사
    @PostMapping("/memberCheck")
    public int memberCheck(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        int flag = 2;
        boolean member_Check = MemberRepository.existsByMemUserid(authRequest.getMemUserid());

        if (member_Check) {
            String mem_pw = MemberRepository.findByMemPw(authRequest.getMemUserid());
            if (securityPassword.matches(authRequest.getMemPw(), mem_pw)) {
                flag = 0;
            } else {
                flag = 1;
            }
        } else {
            flag = 1;
        }

        return flag;
    }

    // 가입 시 아이디 중복 체크
    @PostMapping("/DuplicateIdCheck")
    public boolean DuplicateIdCheck(@RequestBody String mem_userid, HttpServletResponse response) {
        return MemberRepository.existsByMemUserid(mem_userid);
    }

    // 가입 시 닉네임 중복 체크
    @PostMapping("/DuplicateNickCheck")
    public boolean DuplicateNickCheck(@RequestBody String mem_nickname, HttpServletResponse response) {
        return MemberRepository.existsByMemNick(mem_nickname);
    }

    // 가입 시 이메일 중복 체크
    @PostMapping("/DuplicateMailCheck")
    public boolean DuplicateMailCheck(@RequestBody String mem_mail, HttpServletResponse response) {
        return MemberRepository.existsByMemEmail(mem_mail);
    }

    // 로그인 요청(웹사이트 - default)
    @PostMapping("/generateToken")
    @Transactional
    public int authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletResponse response,
            HttpServletRequest request) {
        Cookie cookie = signAction.generateToken(authRequest);
        if (cookie == null) {
            System.out.println("로그인 실패");
            return 1;
        }
        // 로그인 성공
        response.addCookie(cookie);

        String memIp = IpAction.getIpAddress(request);

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getMember();
            if (member != null) {
                // 첫 로그인 여부 업데이트 및 포인트 증가
                if (signinHistoryService.isFirstLogin(member)) {
                    System.out.println("오늘 첫 로그인입니다.");
                    System.out.println(member.getMemId());
                    // 포인트 증가 로직
                    int updatedPoints = updatePointHistoryImpl.insertPointHistoryByMemId(member.getMemId(), "50201",
                            10);

                    if (updatedPoints < 0) {
                        // 포인트 증가 실패
                        System.out.println("포인트 증가 실패");
                        return 1; // 실패 시 처리 (원하는 값 또는 의미 있는 값을 반환)
                    }
                    System.out.println("포인트 증가 성공");
                } else {
                    System.out.println("이미 로그인한 사용자입니다.");
                }
                // SigninHistory 저장
                SigninHistory signinHistory = SigninHistory.builder()
                        .member(member)
                        .memIp(memIp)
                        .createdAt(LocalDateTime.now())
                        .build();

                // SigninHistory 저장
                signinHistoryRepository.save(signinHistory);
            }
        } else {
            System.out.println("멤버 없음");
        }

        return 0;
    }

    // 아래 부터는 스팀, 카카오 로그인 관련--------------------------------------------
    @GetMapping("/steam/callback")
    public void steamLogin(
            @RequestParam(value = "openid.ns") String openidNs,
            @RequestParam(value = "openid.mode") String openidMode,
            @RequestParam(value = "openid.op_endpoint") String openidOpEndpoint,
            @RequestParam(value = "openid.claimed_id") String openidClaimedId,
            @RequestParam(value = "openid.identity") String openidIdentity,
            @RequestParam(value = "openid.return_to") String openidReturnTo,
            @RequestParam(value = "openid.response_nonce") String openidResponseNonce,
            @RequestParam(value = "openid.assoc_handle") String openidAssocHandle,
            @RequestParam(value = "openid.signed") String openidSigned,
            @RequestParam(value = "openid.sig") String openidSig,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String body = WebClient.create("https://steamcommunity.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/openid/login")
                        .queryParam("openid.ns", openidNs)
                        .queryParam("openid.mode", "check_authentication")
                        .queryParam("openid.op_endpoint", openidOpEndpoint)
                        .queryParam("openid.claimed_id", openidClaimedId)
                        .queryParam("openid.identity", openidIdentity)
                        .queryParam("openid.return_to", openidReturnTo)
                        .queryParam("openid.response_nonce", openidResponseNonce)
                        .queryParam("openid.assoc_handle", openidAssocHandle)
                        .queryParam("openid.signed", openidSigned)
                        .queryParam("openid.sig", openidSig)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        boolean isTrue = Objects.requireNonNull(body).contains("true");
        if (isTrue) {
            String[] tmp = openidIdentity.split("/");
            String steamid = tmp[tmp.length - 1];
            Cookie cookie = signAction.steamSign(steamid);

            if (cookie != null) {
                // 성공
                response.addCookie(cookie);
            }
            // 3. 이미 다른 로그인으로 계정을 만들고 스팀을 연동했을 때의 방법?

            response.sendRedirect("/");
        } else {
            response.sendRedirect("/error");
        }
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallback(String code, HttpServletResponse response) {// 데이터 리턴해주는 컨트롤러 함수
        // 1차 json 토큰 요청
        KakaoOAuthTokenTO oAuthToken = signAction.getKakaoOAuthToken(code);
        // 2차 데이터 요청
        String email = signAction.getKakaoEmail(oAuthToken.getAccess_token());

        Cookie cookie = signAction.kakaoSign(email);
        try {
            if (cookie != null) {
                response.addCookie(cookie);
                response.sendRedirect("/");
            } else {
                response.sendRedirect("/login");
            }
        } catch (Exception e) {
        }

    }
}
