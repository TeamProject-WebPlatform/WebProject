package platform.game.model.DAO;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.mapper.SqlMapperInter;
import platform.game.model.TO.MemberTO;
import platform.game.module.jwt.SecurityPassword;

@Repository
@MapperScan(basePackages = {"platform.game.mapper", "platform.game.action"})
public class UserDAO {
    @Autowired
    private SqlMapperInter mapper;

    // 로그인
    public int getMemberTObyIDandPass(String userid, String password) {
        int flag = 1;

        // 암호화된 password 가져오기
        String s_password = mapper.searchMember(userid);

        SecurityPassword securityPassword = new SecurityPassword();
        boolean pwcheck = securityPassword.matches(password, s_password);

        if(pwcheck){
            flag = 0; //성공
        }
        return flag;
    }

    // security_password 반환
    public String getMemberTObySecurityPassword(String userid) {
        String s_password = mapper.searchMember(userid);
        return s_password;
    }

    // 회원가입
    public int setMember(MemberTO to) {
        int flag = 1;
        int result = mapper.setMember(to.getMember_id(), to.getUserid(), to.getPassword(), to.getEmail(), to.getNickname());

        if (result == 1) {
            //회원번호 가이드 DB에 마지막 회원번호 입력
            mapper.setMemberId(to.getMember_id(), "default");
            flag = 0;
            return flag; // 성공 시 0 반환
        } else {
            flag = 1;
            return flag; // 실패 시 1 반환
        }
    }

    //회원정보 뒷자리 불러오기
    public int getLastMemberId(String signin_type) {
        return mapper.getLastMemberId(signin_type);
    }
    
    // kakao 로그인(이메일 확인 후 계정이 없으면 회원정보 데이터 입력)
    public int setKakaoMemberCheck(MemberTO to){
        int flag = 1;
        int result = 0;

        result = mapper.checkKakaoMamber(to.getEmail());

        if (result != 1) {
            System.out.println("계정이 없습니다");
            flag = 1;
        }else if(result == 1){
            flag = 0;
        }
        return flag;
    }
    
    //카카오 회원정보 DB에 입력하기
    public int setKakaoMember(MemberTO to){
        int flag = 1;
        int result = mapper.setMember(to.getMember_id(), to.getUserid(), to.getPassword(), to.getEmail(), to.getNickname());

        if (result == 1) {
            //회원번호 가이드 DB에 마지막 회원번호 입력
            mapper.setMemberId(to.getMember_id(), "kakao");
            flag = 0;
            return flag;
        } else {
            flag = 1;
            return flag; // 실패 시 0 반환
        }
    }

    //카카오 마지막 회원번호 번호 불러오기
    public int getKakaoLastMemberId(String signin_type) {
        return mapper.getLastMemberId(signin_type);
    }

    // 스팀 로그인(이메일 확인)
    public int setSteamMemberCheck(MemberTO to){
        int flag = 1;
        int result = 0;

        result = mapper.checkSteamMamber(to.getUserid());

        if (result != 1) {
            System.out.println("계정이 없습니다");
            flag = 1;
        }else if(result == 1){
            flag = 0;
        }
        return flag;
    }

    //스팀 회원정보 DB에 입력
    public int setSteamMember(MemberTO to){
        int flag = 1;
        int result = mapper.setMember(to.getMember_id(), to.getUserid(), to.getPassword(), to.getEmail(), to.getNickname());

        if (result == 1) {
            //회원번호 가이드 DB에 마지막 회원번호 입력
            mapper.setMemberId(to.getMember_id(), "steam");
            flag = 0;
            return flag;
        } else {
            flag = 1;
            return flag; // 실패 시 0 반환
        }
    }
    //스팀 마지막 회원번호 번호 불러오기
    public int getSteamLastMemberId(String signin_type) {
        return mapper.getLastMemberId(signin_type);
    }

}
