package platform.game.service.action;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import platform.game.service.model.DAO.UserDAO;
import platform.game.service.model.TO.MemberTO;
import platform.game.service.model.TO.UserSignTO;
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.service.jwt.SecurityPassword;

@Component
public class SignUpAction {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MemberInfoRepository memRepo; 

    public int signUp(UserSignTO userSignup) {
        //회원번호 불러오기
        String signin_type = "default";
        int startNum  = 1;
        Long id = 0l;

        Optional<Integer> maxId = memRepo.findMaxMemId(String.valueOf(startNum));
        if(maxId.isPresent()){
            // 신규
            id = Long.parseLong(startNum+"00"+1);
        }else{
            String tmp = String.valueOf(maxId.get()).substring(3);
            id = Long.parseLong(tmp);
            id += 1;
            id = Long.parseLong(startNum + "00" + id);
        }
        
        MemberTO to = new MemberTO();
        //to.setMember_id(id);
        to.setUserid(userSignup.getId());

        // 비밀번호 암호화
        SecurityPassword securityPassword = new SecurityPassword();
        String password = securityPassword.encode(userSignup.getPassword());

        to.setPassword(password);
        to.setNickname(userSignup.getNickname());
        to.setEmail(userSignup.getEmail());

        System.out.println("MemberTO 확인 : " + to);

        int flag = userDAO.setMember(to);

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
