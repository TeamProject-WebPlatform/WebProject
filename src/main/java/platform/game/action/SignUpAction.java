package platform.game.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import platform.game.model.DAO.UserDAO;
import platform.game.model.TO.MemberTO;
import platform.game.model.TO.UserSignTO;

@Component
public class SignUpAction {

    @Autowired
    private UserDAO userDAO;

    public int signUp(UserSignTO userSignup) {
        System.out.println("엑션 signUp(UserSignTO userSignup) 호출");
        System.out.println("UserSignTO 값 : " + userSignup);

        //회원번호 불러오기
        int lastMemberId = userDAO.getLastMemberId();
        int member_id = lastMemberId + 1;

        MemberTO to = new MemberTO();
        to.setMember_id(member_id);
        to.setUserid(userSignup.getId());
        to.setPassword(userSignup.getPassword());
        to.setNickname(userSignup.getNickname());
        to.setEmail(userSignup.getEmail());

        System.out.println("MemberTO 확인 : " + to);

        int flag = userDAO.setMember(to);

        return flag;
    }
}
