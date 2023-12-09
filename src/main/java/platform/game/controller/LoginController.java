package platform.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import platform.game.model.DAO.UserDAO;
import platform.game.model.TO.UserSignTO;
import platform.game.model.TO.UserTO;

@RestController
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config","platform.game.model"})
@RequestMapping("/login")
public class LoginController {
    //로그인과 회원가입

    @Autowired
    UserDAO userDAO;

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
    public int handleSigninup(@RequestBody UserSignTO userSignin){
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
}
