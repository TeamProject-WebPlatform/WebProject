package platform.game.service.controller;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import org.springframework.ui.Model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import platform.game.service.action.KakaoAction;
import platform.game.service.action.MailAction;
import platform.game.service.action.SignUpAction;
import platform.game.service.entity.AuthRequest;
import platform.game.service.entity.Member;
import platform.game.service.model.DAO.UserDAO;
import platform.game.service.model.TO.MemberTO;
import platform.game.service.model.TO.UserSignTO;
import platform.game.service.model.TO.KakaoTO.OAuthTokenTO;
import platform.game.service.service.jwt.JwtManager;
import platform.game.service.service.jwt.JwtService;
import platform.game.service.service.jwt.SecurityPassword;

@RestController
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.model",
        "platform.game.module.*" })
@RequestMapping("/login")
public class LoginController {
    // 로그인과 회원가입

    @Autowired
    UserDAO userDAO;
    @Autowired
    JwtManager jwtManager;
    @Value("${domain}")
    String domain;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SecurityPassword securityPassword;

    @Autowired
    private SignUpAction signUpAction;

    // JWT Login
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @GetMapping("")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    // 회원가입 요청
    @PostMapping("/signup_ok")
    public int handleSignup(@RequestBody UserSignTO userSignup) {
        int flag = 2;

        System.out.println("id : " + userSignup.getId());
        System.out.println("password : " + userSignup.getPassword());
        System.out.println("nickname : " + userSignup.getNickname());
        System.out.println("email : " + userSignup.getEmail());

        flag = signUpAction.signUp(userSignup);

        if (flag == 0) {// 성공
            System.out.println("회원가입 성공");
        } else {
            System.out.println("회원가입 실패");
        }

        return flag;
    }

    // 이메일 인증 요청
    @PostMapping("/mail_ok")
    public int mail_ok(@RequestBody UserSignTO userSignup) {
        MailAction mailAction = new MailAction(javaMailSender);
        // int flag = 2;
        // System.out.println("javaMailSender : " + javaMailSender);
        userSignup.createNumber();

        String toEmail = userSignup.getEmail();
        String toName = userSignup.getNickname();
        int number = userSignup.getNumber();

        String subject = toName + "님의 인증번호 입니다";
        String content = "<h1>" + toName + "님의 인증 번호는 <br><span>" + number + "</span> 입니다.</h1>";

        System.out.println(number);
        System.out.println(toEmail);
        System.out.println(toName);
        System.out.println(subject);
        System.out.println(content);
        // 모델에 number 값을 추가

        mailAction.sendMail(toEmail, toName, subject, content);
        return number;
        // return flag;
    }

    // 로그인 요청(웹사이트 - default)
    @PostMapping("/generateToken")
    public int authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getMemUserid(), authRequest.getMemPw()));
        if (authentication.isAuthenticated()) {
            String password = securityPassword.encode(authRequest.getMemPw());
            String token = jwtService.generateToken(authRequest.getMemUserid(), password);

            Cookie cookie = new Cookie("jwtTokenCookie", token);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            response.addCookie(cookie);

            return 0;
        } else {
            return 2;
        }
    }

    // 아래 부터는 스팀 로그인 관련--------------------------------------------
    @GetMapping("/steam/callback")
    public ModelAndView steamLogin(
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
            HttpServletResponse response) {
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
            // 인증 성공 username : 스팀아이디
            // ModelAndView mav = new ModelAndView("steamWebAPI");
            String[] tmp = openidIdentity.split("/");
            String username = tmp[tmp.length - 1];

            // 스팀 로그인 DB 확인
            // 스팀에서 받아온 아이디가 디비에 등록이 되어있는지 확인하는 코드
            MemberTO to = new MemberTO();
            to.setUserid(username);
            int flag = userDAO.setSteamMemberCheck(to);

            if (flag == 0) { // 바로 로그인
                return new ModelAndView("index");
            } else if (flag == 1) {
                System.out.println("계정이 없습니다.");
                signUpAction.steamsignUp(to);
            }
            // mav.addObject("steamID", username);
            // return mav;
        } else {
            return new ModelAndView("error");
        }
        return new ModelAndView("index");
    }

    /* 카카오톡 로그인 버튼(이메일 받아오기) */
    // 카카오톡 콜백 컨트롤러(코드 받아오기)
    @GetMapping("/kakao/callback")
    public void kakaoCallback(String code, HttpServletResponse response) {// 데이터 리턴해주는 컨트롤러 함수
        String client_id = "6c633b1da1bdc67e6071145ed5723fec";

        RestTemplate restTemplate = new RestTemplate();

        // 헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 바디 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", domain + "/login/kakao/callback");
        params.add("code", code);

        // 헤더와 바디를 하나로 합침
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // http 요청 - post방식
        ResponseEntity<String> res = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthTokenTO oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(res.getBody(), OAuthTokenTO.class);
        } catch (JsonMappingException e) {
            System.out.println("카카오톡 로그인 에러1 : " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("카카오톡 로그인 에러2 : " + e.getMessage());
        }

        KakaoAction.getKakaoToken(oAuthToken.getAccess_token());

        // 카카오에서 받아온 이메일 주소가 디비에 등록이 되어있는지 확인하는 코드
        MemberTO to = new MemberTO();
        // 제이슨 파일에서 이메일을 받아옴
        to.setEmail(KakaoAction.getKakaoToken(oAuthToken.getAccess_token()));

        // 받은 이메일이 디비에 있는 이메일인지 확인
        int flag = userDAO.setKakaoMemberCheck(to);

        // flag가 0이면 통과
        if (flag == 0) { // 바로 로그인
            try {
                // 홈페이지로 돌아가는 구문
                response.sendRedirect(domain);
            } catch (IOException e) {
                System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
            }
        } else if (flag == 1) { // 계정 생성 후 이동
            signUpAction.kakaosignUp(to);
            try {
                // 홈페이지로 돌아가는 구문
                response.sendRedirect(domain);
            } catch (IOException e) {
                System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
            }
        }
    }
}
