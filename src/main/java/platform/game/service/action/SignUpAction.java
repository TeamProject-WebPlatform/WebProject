package platform.game.service.action;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.transaction.Transactional;
import platform.game.service.entity.CommonCode;
import platform.game.service.entity.Member;
import platform.game.service.model.DAO.UserDAO;
import platform.game.service.model.TO.MemberTO;
import platform.game.service.model.TO.UserSignTO;
import platform.game.service.repository.CommonCodeRepository;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.service.MemberInfoService;
import platform.game.service.service.jwt.SecurityPassword;

@Component
public class SignUpAction {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CommonCodeRepository comCdRepo; 
    @Autowired
    private MemberInfoService service; 
    //트랜잭션 템플릿. update 하려면 필요함.
    @Autowired
    private TransactionTemplate transactionTemplate;

    public boolean signUp(UserSignTO userSignup) {
        // 회원번호 불러오기;
        Optional<CommonCode> idInfo = comCdRepo.findBycomCd("19001");
        Long lastid = Long.parseLong(idInfo.get().getComCdParam1())+1;
        Long id = Long.parseLong("100"+lastid);
        Member member = Member.builder()
                                .memId(id)
                                .memUserid(userSignup.getId())
                                .memPw(userSignup.getPassword())
                                .memNick(userSignup.getNickname())
                                .memRoleCd("10003")
                                .memEmail(userSignup.getEmail())
                                .loginKindCd("19101")
                                .memCertified("Y")
                                .memCreatedAt(new Date().toString())
                                .build();
        boolean flag = service.addUser(member);
        if(flag){
            // 공통 코드에서 마지막 회원 번호 update
            transactionTemplate.execute(status -> {
                // 트랜잭션 내에서 업데이트 쿼리 실행
                comCdRepo.updateParam1ByCode("19001",String.valueOf(lastid));
                return null;
            });
        }

        return flag;
    }

    //카카오 로그인시 자동 회원가입
    public int kakaosignUp(MemberTO to){
        //카카오 마지막 회원번호 불러오기
        String signin_type = "kakao";
        
        //카카오 계정 생성을 위한 변수 값 생성
        int lastMemberId = userDAO.getKakaoLastMemberId(signin_type);
        int member_id = lastMemberId + 1;
        //kakao_ 뒤에 붙는 숫자 만들기(카운트해서 뒤에 숫자 더하기)
        int kakaoMember = member_id;
        //카카오톡 로그인 전용 userid, password, email, nickname 만들기 
        to.setMember_id(member_id);
        to.setUserid("kakao_" + kakaoMember);
        to.setPassword("kakao_" + kakaoMember);
        to.setNickname("kakao_" + kakaoMember);
        
        int flag = userDAO.setKakaoMember(to);

        return flag;
    }

    //스팀 로그인시 자동 회원가입
    public int steamsignUp(MemberTO to){
        String signin_type = "steam";
        System.out.println("signin_type : " + signin_type);

        //스팀 계정 생성을 위한 변수 값 생성
        int lastMemberId = userDAO.getSteamLastMemberId(signin_type);
        int member_id = lastMemberId + 1;
        //steam_ 뒤에 붙는 숫자 만들기(카운트해서 뒤에 숫자 더하기)
        int steamMember = member_id;
        //steam 로그인 전용 userid password, email, nickname 만들기
        to.setMember_id(member_id);
        to.setPassword("steam_" + steamMember);
        to.setEmail("steam_" + steamMember);
        to.setNickname("steam_" + steamMember);

        int flag = userDAO.setSteamMember(to);

        return flag;
    }
}
