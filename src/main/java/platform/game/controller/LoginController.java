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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import platform.game.action.KakaoAction;
import platform.game.jwt.JwtManager;
import platform.game.jwt.SecurityPassword;
import platform.game.model.DAO.UserDAO;
import platform.game.model.TO.UserSignTO;
import platform.game.model.TO.UserTO;
import platform.game.model.TO.KakaoTO.OAuthTokenTO;

@RestController
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.model",
        "platform.game.jwt" })
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

        // jwt로 암호화
        // db에 조회
        // 아이디, 닉네임 중복체크
        // 결과 flag에 int로 저장


        return flag;
    }

    // 로그인 요청
    @PostMapping("/signin_ok")
    public int handleSigninin(@RequestBody UserSignTO userSignin) {
        int flag = 2;
        System.out.println("id : " + userSignin.getId());
        System.out.println("password : " + userSignin.getPassword());
        System.out.println("nickname : " + userSignin.getNickname());

        // jwt로 암호화
        // db에 조회
        // 아이디, 닉네임 중복체크
        // 결과 flag에 int로 저장
        // 토큰 생성 및 복호화 테스트 추후 수정 필요
        String token = jwtManager.createToken(userSignin.getId(), userSignin.getPassword());
        System.out.println(token);
        try{Thread.sleep(5000);}catch(Exception e){}
        System.out.println("5초 지남");
        boolean s = jwtManager.validateToken(token);
        System.out.println("테스트 : "+s);

        // 현종이 DB에 현재 문제있음
        // UserTO to = userDAO.getUserTObyIDandPass(userSignin.getId(), userSignin.getPassword());
        // if (to != null) {
        //     System.out.println("로그인 성공");
        //     System.out.println("UserTO : " + to.toString());
        // } else {
        //     System.out.println("로그인 실패");
        // }
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
            ModelAndView mav = new ModelAndView("steamWebAPI");
            String[] tmp = openidIdentity.split("/");
            String username = tmp[tmp.length - 1];
            mav.addObject("steamID", username);
            return mav;
        }else{
            return new ModelAndView("error");
        }

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

        // System.out.println("카카오 토큰 : " + oAuthToken.getAccess_token());
        // ResponseEntity<String> end =
        // userDAO.getKakaoToken(oAuthToken.getAccess_token());
        // return "카카오 토큰 요청 완료(토큰에 대한 응답값) : " + response;
        // return end.getBody();

        KakaoAction.getKakaoToken(oAuthToken.getAccess_token());

        try {
            response.sendRedirect(domain);
        } catch (IOException e) {
            System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
        }
    }
}
