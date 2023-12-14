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
import platform.game.action.SignUpAction;
import platform.game.model.DAO.UserDAO;
import platform.game.model.TO.MemberTO;
import platform.game.model.TO.UserSignTO;
import platform.game.model.TO.KakaoTO.KakaoProfileTO;
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

    @Autowired
    private SignUpAction signUpAction;

    @GetMapping("")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    // 회원가입 요청
    @PostMapping("/signup_ok")
    public int handleSignup(@RequestBody UserSignTO userSignup) {

        System.out.println("컨트롤러 handleSignup 호출");

        int flag = 2;
        System.out.println("id : " + userSignup.getId());
        System.out.println("password : " + userSignup.getPassword());
        System.out.println("nickname : " + userSignup.getNickname());

        // jwt로 암호화
        // db에 조회
        // 아이디, 닉네임 중복체크
        // 결과 flag에 int로 저장


        flag = signUpAction.signUp(userSignup);

        if (flag == 1) {//성공
            System.out.println("회원가입 성공");
        }else{
            System.out.println("회원가입 실패");
        }

        return flag;
    }

    // 로그인 요청(웹사이트 - default)
    @PostMapping("/signin_ok")
    public ModelAndView handleSigninin(@RequestBody UserSignTO userSignin) {
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

        flag = userDAO.getMemberTObyIDandPass(userSignin.getId(), userSignin.getPassword());

        if (flag == 0) {
            System.out.println("로그인 성공");
            return new ModelAndView("index");
        } else {
            System.out.println("로그인 실패");
        }

		return new ModelAndView("index");
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
            return new ModelAndView("login");
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

        KakaoAction.getKakaoToken(oAuthToken.getAccess_token());

        try {
            response.sendRedirect(domain);
        } catch (IOException e) {
            System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
        }


        // MemberTO to = new MemberTO();
        // //제이슨 파일에서 이메일을 받아옴
        // to.setEmail(KakaoAction.getKakaoToken(oAuthToken.getAccess_token()));

        // System.out.println("controller email : " + to.getEmail());

        // //받은 이메일이 디비에 있는 이메일인지 확인
        // //int flag = userDAO.setSosialMemberCheck(to);
        // //System.out.println("controller flag : " + flag);
        
        // //flag가 0이면 통과
        // if (flag == 0) {
        //     try {
        //         response.sendRedirect(domain);
        //     } catch (IOException e) {
        //         System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
        //     }
        // }else{
        //     try {
        //         //회원가입 화면으로 이동 혹은 다시 로그인 화면으로 이동
        //         response.sendRedirect(domain + "login");
        //     } catch (IOException e) {
        //         System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
        //     }
        // }
    }
}
