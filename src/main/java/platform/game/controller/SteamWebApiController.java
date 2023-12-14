package platform.game.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.model" })
@RequestMapping("/steamapi")
public class SteamWebApiController {
    
    @Value("${steamWebApiKey}")
    String steamWebApiKey;

    @GetMapping("/playerSummary")
    public String steamPlayerSummary(String steamID) {
        String body = WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUser/GetPlayerSummaries/v0002")
                        .queryParam("key", steamWebApiKey)
                        .queryParam("steamids", steamID)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return body;
    }

    @GetMapping("/gameNews")
    public String steamGameNews(String gameID) {
        // http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid=440&count=3&maxlength=300&format=json
        String body = WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamNews/GetNewsForApp/v0002")
                        .queryParam("appid", gameID)
                        .queryParam("count", 5) // 뉴스 개수
                        .queryParam("maxlength", 300) // 뉴스 길이
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return body;
    }

    @GetMapping("/gameAchievement")
    public String steamGameAchievement(String gameID) {
        // http://api.steampowered.com/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/?gameid=76561198272883644&format=json
        try {
            String body = WebClient.create("http://api.steampowered.com")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002")
                            .queryParam("gameid", gameID)
                            .queryParam("format", "json")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return body;
        } catch (Exception e) {
            System.err.println(e);
        }
        return "정보 없음";
    }

    @GetMapping("/myGameList")
    public String steamMyGameList(String steamID) {
        // http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=B52D1A3402E850E0F56BE12E89F145C6&steamid=76561198272883644&format=json
        try {
            String body = WebClient.create("http://api.steampowered.com")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/IPlayerService/GetOwnedGames/v0001")
                            .queryParam("key", steamWebApiKey)
                            .queryParam("steamid", steamID)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return body;
        } catch (Exception e) {
            System.err.println(e);
        }
        return "정보 없음";
    }

    @GetMapping("/myGameAchievement")
    public String steamMyGameAchievement(String steamID, String gameID) {
        // http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?appid=440&key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX&steamid=76561197972495328
        try {
            String body = WebClient.create("http://api.steampowered.com")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ISteamUserStats/GetPlayerAchievements/v0001")
                            .queryParam("appid", gameID)
                            .queryParam("key", steamWebApiKey)
                            .queryParam("steamids", steamID)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return body;
        } catch (Exception e) {
            System.err.println(e);
        }
        return "정보 없음";
    }
}
