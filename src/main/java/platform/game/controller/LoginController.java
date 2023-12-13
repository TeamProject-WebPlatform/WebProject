package platform.game.controller;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
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
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import platform.game.action.KakaoAction;
import platform.game.model.DAO.UserDAO;
import platform.game.model.TO.UserSignTO;
import platform.game.model.TO.UserTO;
import platform.game.model.TO.KakaoTO.OAuthTokenTO;
import platform.game.security.SecurityUser;

@RestController
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config","platform.game.model"})
@RequestMapping("/login")
public class LoginController {
    //로그인과 회원가입

    @Autowired
    UserDAO userDAO;
    @Value("${steamWebApiKey}")
    String steamWebApiKey;
    @Value("${domain}")
    String domain;

    @GetMapping("")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    // 회원가입 요청
    @PostMapping("/signup_ok")
    public int handleSignup(@RequestBody UserSignTO userSignup){
        int flag = 2;
        System.out.println("id : "+userSignup.getId());
        System.out.println("password : "+userSignup.getPassword());
        System.out.println("nickname : "+userSignup.getNickname());
        
        // jwt로 암호화
        // db에 조회
        // 아이디, 닉네임 중복체크
        // 결과 flag에 int로 저장

        return flag;
    }
    // 로그인 요청
    @PostMapping("/signin_ok")
    public int handleSigninin(@RequestBody UserSignTO userSignin){
        int flag = 2;
        System.out.println("id : "+userSignin.getId());
        System.out.println("password : "+userSignin.getPassword());
        System.out.println("nickname : "+userSignin.getNickname());
        
        // jwt로 암호화
        // db에 조회
        // 아이디, 닉네임 중복체크
        // 결과 flag에 int로 저장
        UserTO to = userDAO.getUserTObyIDandPass(userSignin.getId(), userSignin.getPassword());
        if(to!=null){
            System.out.println("로그인 성공");
            System.out.println("UserTO : "+to.toString());
        }else{
            System.out.println("로그인 실패");
        }
        return flag;
    }

    // 아래 부터는 스팀 로그인 관련--------------------------------------------
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
            HttpServletResponse response
    ) {
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
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        boolean isTrue = Objects.requireNonNull(body).contains("true");

//         1. findBySteamId(steamId)
//         2. 없으면 회원가입 Member or 로그인
//         security 객체를 만들고 session에 저장

        // System.err.println("openidIdentity:"+openidIdentity);
        // System.err.println("openidClaimedId:"+openidClaimedId);
        String[] tmp = openidIdentity.split("/");
        String username = tmp[tmp.length-1];

        SecurityUser user = SecurityUser.builder()
                .username(username)
                .build();

        Authentication authentication =
                new OAuth2AuthenticationToken(user, user.getAuthorities(), "steam");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 새로운 세션 생성
        session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        // 세션 ID를 쿠키에 설정
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        try{
            response.sendRedirect(domain + "/login/steam/check");
        }catch(IOException e){
            System.out.println("LoginController.steamLogin : 리다이렉션 실패");
        }
    }

    @GetMapping("/steam/check")
    public ModelAndView steamLoginCheck() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String steamID = user.getUsername();
        ModelAndView mav = new ModelAndView("steamWebAPI");
        
       
        
        mav.addObject("steamID", steamID);
        return mav;
    }
    @GetMapping("/steam/playerSummary")
    public String steamPlayerSummary(String steamID){
        String body = WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUser/GetPlayerSummaries/v0002")
                        .queryParam("key", steamWebApiKey)
                        .queryParam("steamids", steamID)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return body;
    }
    @GetMapping("/steam/gameNews")
    public String steamGameNews(String gameID){
        //http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid=440&count=3&maxlength=300&format=json
        String body = WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamNews/GetNewsForApp/v0002")
                        .queryParam("appid", gameID)
                        .queryParam("count", 5) // 뉴스 개수
                        .queryParam("maxlength", 300) // 뉴스 길이  
                        .queryParam("format", "json")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return body;
    }
    @GetMapping("/steam/gameAchievement")
    public String steamGameAchievement(String gameID){
        //http://api.steampowered.com/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/?gameid=76561198272883644&format=json
        try{
            String body = WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002")
                        .queryParam("gameid", gameID)
                        .queryParam("format", "json")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return body;
        }catch(Exception e){
            System.err.println(e);
        }
        return "정보 없음";
    }
    @GetMapping("/steam/myGameList")
    public String steamMyGameList(String steamID){
        //http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=B52D1A3402E850E0F56BE12E89F145C6&steamid=76561198272883644&format=json
        try{
            String body = WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/IPlayerService/GetOwnedGames/v0001")
                        .queryParam("key", steamWebApiKey)
                        .queryParam("steamid", steamID)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return body;
        }catch(Exception e){
            System.err.println(e);
        }
        return "정보 없음";
    }
    @GetMapping("/steam/myGameAchievement")
    public String steamMyGameAchievement(String steamID,String gameID){
        //http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?appid=440&key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX&steamid=76561197972495328
        try{
            String body = WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUserStats/GetPlayerAchievements/v0001")
                        .queryParam("appid", gameID)
                        .queryParam("key", steamWebApiKey)
                        .queryParam("steamids", steamID)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return body;
        }catch(Exception e){
            System.err.println(e);
        }
        return "정보 없음";
    }

    /* 카카오톡 로그인 버튼(이메일 받아오기) */
    //카카오톡 콜백 컨트롤러(코드 받아오기)
    @GetMapping("/kakao/callback")
    public void kakaoCallback(String code, HttpServletResponse response){//데이터 리턴해주는 컨트롤러 함수

        String client_id = "6c633b1da1bdc67e6071145ed5723fec";

        RestTemplate restTemplate = new RestTemplate();
        
        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //바디 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", domain+":8080/login/kakao/callback");
        params.add("code", code);

        //헤더와 바디를 하나로 합침
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //http 요청 - post방식
        ResponseEntity<String> res = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthTokenTO oAuthToken = null;
        
        try {
            oAuthToken = objectMapper.readValue(res.getBody(), OAuthTokenTO.class);    
        } catch (JsonMappingException e) {
            System.out.println("카카오톡 로그인 에러1 : " + e.getMessage());
        } catch (JsonProcessingException e){
            System.out.println("카카오톡 로그인 에러2 : " + e.getMessage());
        }

        //System.out.println("카카오 토큰 : " + oAuthToken.getAccess_token());
        //ResponseEntity<String> end = userDAO.getKakaoToken(oAuthToken.getAccess_token());
        //return "카카오 토큰 요청 완료(토큰에 대한 응답값) : " + response;
        //return end.getBody();

        KakaoAction.getKakaoToken(oAuthToken.getAccess_token());

        try{
            response.sendRedirect(domain);
        }catch(IOException e){
            System.out.println("LoginController.kakaoLogin : 리다이렉션 실패");
        }
    }
}
