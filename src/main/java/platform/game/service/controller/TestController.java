package platform.game.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import platform.game.service.entity.Member;
import platform.game.service.entity.UserInfo;
import platform.game.service.service.MemberInfoService;
import platform.game.service.service.UserInfoService;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MemberInfoService memberService;
    @PostMapping("/addNewUser") 
    public String addNewUser(@RequestBody Member member) { 
        return memberService.addUser(member);
    } 
}
