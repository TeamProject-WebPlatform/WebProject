package platform.game.service.action;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.Cookie;
import platform.game.service.entity.AuthRequest;
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.model.TO.UserSignTO;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.service.MemberInfoService;
import platform.game.service.service.jwt.JwtService;
import platform.game.service.service.jwt.SecurityPassword;

@Component
public class SignAction {
    
    @Value("${domain}")
    String domain;

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

    private final int JWT_EXPIRY_TIME = 3600; // JWT 토큰 만료 시간
    
    public boolean signUp(UserSignTO userSignup, int login) {
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
                // 멤버 id 할당 하기 위해 common code에서 값 select
                Optional<CommonCode> idInfo = comCdRepo.findBycomCd(idCode);
                Long lastid = Long.parseLong(idInfo.get().getComCdParam1()) + 1;
                Long id = Long.parseLong(idPrefix + lastid);
                // member 객체 생성 
                Member member = Member.builder()
                        .memId(id)
                        .memUserid(userSignup.getId())
                        .memPw(userSignup.getPassword())
                        .memNick(userSignup.getNickname())
                        .memRoleCd(roleCode)
                        .memSteamid(userSignup.getSteamid())
                        .memEmail(userSignup.getEmail())
                        .loginKindCd(loginKindCode)
                        .memCertified(memCertified)
                        .memCreatedAt(new Date().toString())
                        .build();
                // memService bean에서 addUser로 만든 member를 DB에 추가(service 내에서 비번 암호화)
                boolean flag = memService.addUser(member);
                // 성공하면 common code의 값 update
                if (flag) {
                    comCdRepo.updateParam1ByCode(idCode, String.valueOf(lastid));
                }
                else {
                    successFlag.set(false);
                    status.setRollbackOnly();
                }
            } catch (Exception e) {
                System.err.println("Exception in transaction: " + e.getMessage());
                // 롤백을 수행하도록 표시
                successFlag.set(false);
                status.setRollbackOnly();
            }
            return null;
        });

        return successFlag.get();
    }

    // 위에 member 객체 생성시 .memSteamid를 추가했음. kakaoid도 추가해야됨 erd보고
    // 그리고 userSignTO 에도 steamid 변수를 추가했음.
    // 카카오 로그인시 자동 회원가입
    // public int kakaosignUp(MemberTO to){
    // //카카오 마지막 회원번호 불러오기
    // String signin_type = "kakao";

    // //카카오 계정 생성을 위한 변수 값 생성
    // int lastMemberId = userDAO.getKakaoLastMemberId(signin_type);
    // int member_id = lastMemberId + 1;
    // //kakao_ 뒤에 붙는 숫자 만들기(카운트해서 뒤에 숫자 더하기)
    // int kakaoMember = member_id;
    // //카카오톡 로그인 전용 userid, password, email, nickname 만들기
    // to.setMember_id(member_id);
    // to.setUserid("kakao_" + kakaoMember);
    // to.setPassword("kakao_" + kakaoMember);
    // to.setNickname("kakao_" + kakaoMember);

    // int flag = userDAO.setKakaoMember(to);

    // return flag;
    // }

    // 스팀 로그인시 자동 회원가입
    public int steamSign(String steamid) {
        // steamid 검색해서 이미 있으면 로그인
        // 없으면 회원가입
        Optional<Member> mem = memRepo.findByMemSteamid(steamid);
        if (mem.isPresent()) {
            // 로그인
            // 로그인에 post할 AuthRequest 객체 보내기
            AuthRequest authRequest = new AuthRequest();
            authRequest.setMemUserid("steam_"+steamid);
            authRequest.setMemPw("steam_"+steamid);

            // Post 요청
            String body = WebClient.create(domain)
                .post()
                .uri("/login/generateToken")
                .body(BodyInserters.fromValue(authRequest))
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return 0;
        } else {
            // 회원가입
            UserSignTO userSignTO = new UserSignTO();
            System.out.println(steamid);
            userSignTO.setId("steam_" + steamid);
            userSignTO.setNickname("steam_" + steamid);
            userSignTO.setPassword("steam_" + steamid);
            userSignTO.setEmail("");
            userSignTO.setSteamid(steamid);
            signUp(userSignTO, 2);
            return 1;
        }
    }


    // JWT 토큰 만들기
    public Cookie generateToken(AuthRequest authRequest) {
        String password = securityPassword.encode(authRequest.getMemPw());
        String token = jwtService.generateToken(authRequest.getMemUserid(), password);

        Cookie cookie = new Cookie("jwtTokenCookie", token);
        cookie.setMaxAge(JWT_EXPIRY_TIME);
        cookie.setPath("/");
        return cookie;
    }
    public String[] getLoginParams(int login){
        String[] arr = new String[5];
        String idCode = "";
        String idPrefix = "";
        String roleCode = "";
        String loginKindCode = "";
        String memCertified = "";
        switch (login) {
            case 0: // 사이트
                idCode = "19001"; idPrefix = "100"; roleCode = "10003"; loginKindCode = "19101"; memCertified = "Y";
                break;
            case 1: // 카카오
                idCode = "19002"; idPrefix = "200"; roleCode = "10003"; loginKindCode = "19102"; memCertified = "N";
                break;
            case 2: // 스팀
                idCode = "19003"; idPrefix = "300"; roleCode = "10003"; loginKindCode = "19103"; memCertified = "N";
                break;
            case 3: // 라이엇
                idCode = "19004"; idPrefix = "400"; roleCode = "10003"; loginKindCode = "19104"; memCertified = "N";
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
