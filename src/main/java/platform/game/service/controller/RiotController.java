package platform.game.service.controller;

import java.io.IOException;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@ComponentScan(basePackages = {"platform.game.action","platform.game.env.config","platform.game.jwt"})
public class RiotController {
    @GetMapping("/loginRiot")
    public void loginRiot(HttpServletResponse response) throws IOException {
        // Riot 로그인 페이지로 리다이렉션
        String riotLoginUrl = "https://authenticate.riotgames.com/?client_id=prod-xsso-leagueoflegends&code_challenge=aoQCOQA8tWSFLkt9bgoqQroU0a8YwNr1gBwrvHS90mw&method=riot_identity&platform=web&redirect_uri=https%3A%2F%2Fauth.riotgames.com%2Fauthorize%3Fclient_id%3Dprod-xsso-leagueoflegends%26code_challenge%3DaoQCOQA8tWSFLkt9bgoqQroU0a8YwNr1gBwrvHS90mw%26code_challenge_method%3DS256%26redirect_uri%3Dhttps%253A%252F%252Fxsso.leagueoflegends.com%252Fredirect%26response_type%3Dcode%26scope%3Dopenid%2520account%2520email%26state%3Df136ad1e5210381586c7816d37&security_profile=low";
        response.sendRedirect(riotLoginUrl);
    }

    @GetMapping("/loginRiot/success")
    public String loginRiotSuccess() {
        // Riot 로그인 성공 후에 원래 페이지로 리다이렉션
        return "redirect:/original-page";
    }

    @GetMapping("/original-page")
    public String originalPage() {
        // 원래 페이지의 컨텐츠를 보여주는 로직
        return "/";
    }
}
