package platform.game.service.action;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import platform.game.service.entity.AuthRequest;
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.model.TO.UserSignTO;
import platform.game.service.model.TO.KakaoTO.KakaoOAuthTokenTO;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.repository.UpdatePointHistory;
import platform.game.service.service.MemberInfoDetails;
import platform.game.service.service.MemberInfoService;
import platform.game.service.service.jwt.JwtService;
import platform.game.service.service.jwt.SecurityPassword;

@Component
public class SignAction {

    @Value("${domain}")
    String domain;
    @Value("${kakao.clientid}")
    String kakaoClientId;

    @Autowired
    private MemberInfoService memService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CommonCodeRepository comCdRepo;
    @Autowired
    private MemberInfoRepository memRepo;

    @Autowired
    private TransactionTemplate transactionTemplate; // 트랜잭션 템플릿. update 하려면 필요함.
    @Autowired
    private SecurityPassword securityPassword;
    // JWT Login
    @Autowired
    private AuthenticationManager authenticationManager;

    private final int JWT_EXPIRY_TIME = JwtService.JWT_EXPIRY_TIME; // JWT 토큰 만료 시간

    // 토큰 발행후 로그인시 포인트 적립
    @Autowired
    @Qualifier("updatePointHistoryImpl")
    private UpdatePointHistory updatePointHistory;

    public Cookie signUp(UserSignTO userSignup, int login) {
        // jwt 토큰 저장할 Cookie
        Cookie cookie = null;
        // 트랜잭션의 커밋, 롤백 여부를 저장하기 위한 가변 객체
        AtomicReference<Boolean> successFlag = new AtomicReference<>(true);
        // login 종류마다 달라지는 변수 값을 할당
        String[] loginParams = getLoginParams(login);
        String idCode = loginParams[0];
        String idPrefix = loginParams[1];
        String roleCode = loginParams[2];
        String loginKindCode = loginParams[3];
        String memCertified = loginParams[4];
        successFlag.set(true);
        // 트랜잭션 내에서 쿼리 실행
        transactionTemplate.execute(status -> {
            try {
                // System.out.println("체크 : " + userSignup.toString());
                // 멤버 id 할당 하기 위해 common code에서 값 select
                Optional<CommonCode> idInfo = comCdRepo.findByCd(idCode);
                Long lastid = Long.parseLong(idInfo.get().getRemark1()) + 1;
                Long id = Long.parseLong(idPrefix + lastid);
                // System.out.println("signupAction > id : " + id);
                // member 객체 생성
                Member member = Member.builder()
                        .memId(id)
                        .memUserid(userSignup.getMemUserid())
                        .memPw(userSignup.getMemPw())
                        .memNick(userSignup.getMemNick())
                        .memRoleCd(roleCode)
                        .memSteamid(userSignup.getMemSteamid())
                        .memKakaoid(userSignup.getMemKakaoid())
                        .memEmail(userSignup.getMemEmail())
                        .loginKindCd(loginKindCode)
                        .memCertified(memCertified)
                        .memCreatedAt(new Date().toString())
                        .build();
                // System.out.println("signupAction > 멤버 객체 생성 성공 " + member.toString());
                // memService bean에서 addUser로 만든 member를 DB에 추가(service 내에서 비번 암호화)
                System.out.println(member.getMemId());
                boolean flag = memService.addUser(member);
                boolean addRank = memService.addUserRanking(member);
                boolean addProfile = memService.addUserProfile(member);
                System.out.println("signupAction > 멤버 객체 추가 성공");
                // 성공하면 common code의 값 update
                if (flag && addRank && addProfile) {
                    comCdRepo.updateRemark1ByCd(idCode, String.valueOf(lastid));
                    userSignup.setMemId(id);    
                } else {
                    System.out.println("롤백 1");
                    successFlag.set(false);
                    status.setRollbackOnly();
                }
            } catch (Exception e) {
                System.err.println("Exception in transaction: " + e.getMessage());
                // 롤백을 수행하도록 표시
                System.out.println("롤백 2");
                successFlag.set(false);
                status.setRollbackOnly();
            }
            return null;
        });
        if (successFlag.get()) {
            AuthRequest authRequest = new AuthRequest();
            authRequest.setMemUserid(userSignup.getMemUserid());
            authRequest.setMemPw(userSignup.getMemPw());
            // Member member = ((MemberInfoDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();

            cookie = generateToken(authRequest);
            
            // 여기서 회원가입시 포인트 증가 로직  
            updatePointHistory.insertPointHistoryByMemId(userSignup.getMemId(), "50201", 10);

            return cookie;
        } else {
            return null;
        }
    }

    // 카카오 로그인
    public KakaoOAuthTokenTO getKakaoOAuthToken(String code) {
        KakaoOAuthTokenTO oAuthToken = null;
        ObjectMapper objectMapper = new ObjectMapper();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", domain + "/login/kakao/callback");
        params.add("code", code);
        try {
            String body = WebClient.create("https://kauth.kakao.com")
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth/token")
                            .build())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            oAuthToken = objectMapper.readValue(body, KakaoOAuthTokenTO.class);
        } catch (Exception e) {
        }
        return oAuthToken;
    }

    public String getKakaoEmail(String access_token) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = null;

        try {
            String body = WebClient.create("https://kapi.kakao.com")
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/user/me")
                            .build())
                    .headers(httpHeaders -> {
                        httpHeaders.add("Authorization", "Bearer " + access_token);
                        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                    })
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            responseMap = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
        }

        String email = "";
        if (responseMap != null) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseMap.get("kakao_account");
            if (kakaoAccount != null) {
                email = kakaoAccount.get("email").toString();
            }
        }
        return email;
    }

    public Cookie kakaoSign(String email) {
        Cookie cookie = null;
        Optional<Member> mem = memRepo.findByMemKakaoid(email);
        if (mem.isPresent()) {
            // 로그인
            AuthRequest authRequest = new AuthRequest();
            authRequest.setMemUserid("kakao_" + email);
            authRequest.setMemPw("kakao_" + email);

            cookie = generateToken(authRequest);
        } else {
            // 회원가입
            UserSignTO userSignTO = new UserSignTO();
            userSignTO.setMemUserid("kakao_" + email);
            userSignTO.setMemNick("kakao_" + email);
            userSignTO.setMemPw("kakao_" + email);
            userSignTO.setMemEmail(email);
            userSignTO.setMemKakaoid(email);
            cookie = signUp(userSignTO, 1);
        }
        return cookie;
    }

    // 스팀 로그인시 자동 회원가입 jwt 토큰을 리턴
    public Cookie steamSign(String steamid) {
        // steamid 검색해서 이미 있으면 로그인
        // 없으면 회원가입
        Cookie cookie = null;
        Optional<Member> mem = memRepo.findByMemSteamid(steamid);
        if (mem.isPresent()) {
            // 로그인
            AuthRequest authRequest = new AuthRequest();
            authRequest.setMemUserid("steam_" + steamid);
            authRequest.setMemPw("steam_" + steamid);

            cookie = generateToken(authRequest);
        } else {
            // 회원가입
            UserSignTO userSignTO = new UserSignTO();
            System.out.println(steamid);
            userSignTO.setMemUserid("steam_" + steamid);
            userSignTO.setMemNick("steam_" + steamid);
            userSignTO.setMemPw("steam_" + steamid);
            userSignTO.setMemEmail("");
            userSignTO.setMemSteamid(steamid);
            cookie = signUp(userSignTO, 2);
        }
        return cookie;
    }

    // JWT 토큰 만들기
    public Cookie generateToken(AuthRequest authRequest) {
        Cookie cookie = null;
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getMemUserid(), authRequest.getMemPw()));
            if (authentication.isAuthenticated()) {
                String password = securityPassword.encode(authRequest.getMemPw());
                String token = jwtService.generateToken(authRequest.getMemUserid(), password);

                cookie = new Cookie("jwtTokenCookie", token);
                cookie.setMaxAge(JWT_EXPIRY_TIME);
                cookie.setPath("/");
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return cookie;
    }

    public String[] getLoginParams(int login) {
        String[] arr = new String[5];
        String idCode = "";
        String idPrefix = "";
        String roleCode = "";
        String loginKindCode = "";
        String memCertified = "";
        switch (login) {
            case 0: // 사이트
                idCode = "19001";
                idPrefix = "100";
                roleCode = "10003";
                loginKindCode = "19101";
                memCertified = "Y";
                break;
            case 1: // 카카오
                idCode = "19002";
                idPrefix = "200";
                roleCode = "10003";
                loginKindCode = "19102";
                memCertified = "Y";
                break;
            case 2: // 스팀
                idCode = "19003";
                idPrefix = "300";
                roleCode = "10003";
                loginKindCode = "19103";
                memCertified = "N";
                break;
            case 3: // 라이엇
                idCode = "19004";
                idPrefix = "400";
                roleCode = "10003";
                loginKindCode = "19104";
                memCertified = "N";
                break;
        }
        arr[0] = idCode;
        arr[1] = idPrefix;
        arr[2] = roleCode;
        arr[3] = loginKindCode;
        arr[4] = memCertified;
        return arr;
    }
}
