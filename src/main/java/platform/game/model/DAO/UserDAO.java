package platform.game.model.DAO;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.jwt.SecurityPassword;
import platform.game.mapper.SqlMapperInter;
import platform.game.model.TO.MemberTO;

@Repository
@MapperScan(basePackages = {"platform.game.mapper"})
public class UserDAO {
    @Autowired
    private SqlMapperInter mapper;
    
    //로그인
    public int getMemberTObyIDandPass(String userid, String password){
        int flag = 1;

        //암호화된 password 가져오기
        String s_password = mapper.searchMember(userid);

        SecurityPassword securityPassword = new SecurityPassword();
        boolean pwcheck = securityPassword.matches(password, s_password);

        //int result = mapper.getMemberTObyIDandPass(userid, password);

        if(pwcheck){
            flag = 0; //성공
        }

        return flag;
    }

    //회원가입
    public int setMember(MemberTO to){
        int flag = 1;
        int result = mapper.setMember(to.getMember_id(), to.getUserid(), to.getPassword(), to.getEmail(), to.getNickname());
        //System.out.println("flag 값 : " + flag);

        if (result == 1) {
            //회원번호 가이드 DB에 마지막 회원번호 입력
            mapper.setMemberId(to.getMember_id());
            flag = 0;
            return flag;
        } else {
            flag = 1;
            return flag; // 실패 시 0 반환
        }
    }
    //회원정보 뒷자리 불러오기
    public int getLastMemberId() {
        return mapper.getLastMemberId();
    }


    // //kakao, 스팀 로그인 가능여부 확인
    // public int setSosialMemberCheck(MemberTO to){
    //     int flag = 1;
    //     int result = 0;

    //     result = mapper.checkKakaoMamber(to.getEmail());
    //     System.out.println("DAO kakao result : " + result);

    //     if (result == 1) { // 카카오 로그인 성공 result가 0 일경우 스팀인지 확인
    //         flag = 0;
    //     }
    //     return flag;
    // }

}
