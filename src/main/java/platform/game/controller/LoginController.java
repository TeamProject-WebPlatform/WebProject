package platform.game.controller;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import platform.game.action.KakaoAction;
import platform.game.action.SignUpAction;
import platform.game.model.DAO.UserDAO;
import platform.game.model.TO.UserSignTO;
import platform.game.model.TO.KakaoTO.OAuthTokenTO;
import platform.game.module.jwt.JwtManager;
import platform.game.module.jwt.SecurityPassword;

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
    private SecurityPassword securityPassword;

    @Autowired
    private SignUpAction signUpAction;

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

        flag = signUpAction.signUp(userSignup);

        if (flag == 0) {// 성공
            System.out.println("회원가입 성공");
        } else {
            System.out.println("회원가입 실패");
        }

        return flag;
    }

    // 로그인 요청(웹사이트 - default)
    @PostMapping("/signin_ok")
    public int handleSigninin(@RequestBody UserSignTO userSignin, HttpServletResponse response) {
        int flag = 2;
        System.out.println("id : " + userSignin.getId());
        System.out.println("password : " + userSignin.getPassword());

        // jwt로 암호화
        // db에 조회
        // 아이디, 닉네임 중복체크
        // 결과 flag에 int로 저장
        // 토큰 생성 및 복호화 테스트 추후 수정 필요
        // String token = jwtManager.createToken(userSignin.getId(),
        // userSignin.getPassword());
        // System.out.println(token);
        // try{Thread.sleep(5000);}catch(Exception e){}
        // System.out.println("5초 지남");
        // boolean s = jwtManager.validateToken(token);
        // System.out.println("테스트 : "+s);

        flag = userDAO.getMemberTObyIDandPass(userSignin.getId(), userSignin.getPassword());
        String s_password = userDAO.getMemberTObySecurityPassword(userSignin.getId());

        if (flag == 0) {
            System.out.println("로그인 성공");
            String token = jwtManager.createToken(userSignin.getId(), s_password);
            System.out.println(token);

            // 쿠키 생성
            Cookie cookie = new Cookie("jwtTokenCookie", token);

            // 쿠키를 안전하게 설정하기 위해 secure 및 httpOnly 설정
            // cookie.setSecure(true); // HTTPS 프로토콜 사용 여부
            // cookie.setHttpOnly(true); // JavaScript를 통한 접근 금지

            // 쿠키의 속성 설정 (예: 유효 시간, 경로 등)
            cookie.setMaxAge(3600); // 60 * 60 1시간 동안 유효
            // cookie.setDomain("localhost");
            cookie.setPath("/");    // 모든 경로에서 접근 가능

            // 쿠키를 응답 헤더에 추가
            response.addCookie(cookie);
        } else {
            System.out.println("로그인 실패");
        }
        return flag;
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
            //ModelAndView mav = new ModelAndView("steamWebAPI");
            String[] tmp = openidIdentity.split("/");
            String username = tmp[tmp.length - 1];

            //스팀 로그인 DB 확인
            //스팀에서 받아온 아이디가 디비에 등록이 되어있는지 확인하는 코드
            MemberTO to = new MemberTO();
            to.setUserid(username);
            int flag = userDAO.setSteamMemberCheck(to);

            if(flag == 0){ // 바로 로그인
                return new ModelAndView("index");
            }else if(flag == 1){
                System.out.println("계정이 없습니다.");
                signUpAction.steamsignUp(to);
            }
            //mav.addObject("steamID", username);
            //return mav;
        }else{
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

        //카카오에서 받아온 이메일 주소가 디비에 등록이 되어있는지 확인하는 코드
        MemberTO to = new MemberTO();
        //제이슨 파일에서 이메일을 받아옴
        to.setEmail(KakaoAction.getKakaoToken(oAuthToken.getAccess_token()));

        //받은 이메일이 디비에 있는 이메일인지 확인
        int flag = userDAO.setKakaoMemberCheck(to);

        //flag가 0이면 통과
        if (flag == 0) { // 바로 로그인
            try {
                //홈페이지로 돌아가는 구문
                response.sendRedirect(domain);
            } catch (IOException e) {
                System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
            }
        }else if(flag == 1){ // 계정 생성 후 이동
            signUpAction.kakaosignUp(to);
            try {
                //홈페이지로 돌아가는 구문
                response.sendRedirect(domain);
            } catch (IOException e) {
                System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
            }
        }
    }
}
