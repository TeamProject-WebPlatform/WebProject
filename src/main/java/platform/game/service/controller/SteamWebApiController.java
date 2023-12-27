
package platform.game.service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.servlet.http.HttpServletResponse;
import platform.game.service.action.SteamAction;
import platform.game.service.action.SteamAction.SteamGameTO;
import platform.game.service.action.SteamAction.SteamNewsTO;
import platform.game.service.action.SteamAction.SteamPlayerSummaryTO;
import reactor.core.publisher.Mono;

@RestController
@ComponentScan(basePackages = { "platform.game.action", "platform.game.env.config", "platform.game.model" })
@RequestMapping("/steamapi")
public class SteamWebApiController {

    @Value("${steamWebApiKey}")
    String steamWebApiKey;
    @Autowired
    SteamAction steamAction;
    /*
     * playerSummary 와 myGameList 사용하면됨
     */
    // http://localhost:8080/steamapi/playerSummary?steamID=76561198272883644
    @GetMapping("/playerSummary")
    public Mono<String> steamPlayerSummary(String steamID) {
        // http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002?key=D5120901351AB7A4A78EA5CF8C568AAF&steamids=76561198272883644
        return WebClient.create("http://api.steampowered.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUser/GetPlayerSummaries/v0002")
                        .queryParam("key", steamWebApiKey)
                        .queryParam("steamids", steamID)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(jsonData->{
                    SteamPlayerSummaryTO steamUserTO = steamAction.getPlayerSummary(jsonData);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<img url="+steamUserTO.getAvatarUrl()+">");
                    sb.append("스팀 닉네임 : "+steamUserTO.getNickname()+"</br>");
                    sb.append("<a href = "+steamUserTO.getProfileUrl()+">스팀 프로필 링크 바로가기</a></br>");

                    return Mono.just(sb.toString());
                });
   }

    @GetMapping("/gameNews")
    public Mono<String> steamGameNews(String gameID) {
        // http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid=440&count=3&maxlength=300&format=json
        try {
            // WebClient 요청 코드
            return WebClient.create("http://api.steampowered.com")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                        .path("/ISteamNews/GetNewsForApp/v0002")
                        .queryParam("appid", gameID)
                        .queryParam("count", 20) // 뉴스 개수
                        .queryParam("maxlength", 300) // 뉴스 길이
                        .queryParam("format", "json")
                        .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(body -> {
                        SteamNewsTO[] gameNewsList = steamAction.getGameNews(body);
                        StringBuilder sb = new StringBuilder();
                        for(int i = 0;i<gameNewsList.length;i++){
                            String title = gameNewsList[i].getTitle();
                            String url = gameNewsList[i].getUrl();
                            sb.append("<a href = "+url+">"+title+"</a>");
                            sb.append("</br></br>");
                        }
                        return Mono.just(sb.toString());
                    });
        } catch (WebClientResponseException ex) {
            // 상세 응답 정보 확인
            System.out.println("Response body: " + ex.getResponseBodyAsString());
            // System.out.println("Response headers: " + ex.getResponseHeaders());
            return Mono.just("게임 목록 처리 오류");
        }

    }

    

    // http://localhost:8080/steamapi/myGameList?steamID=76561198272883644
    @GetMapping("/myGameList")
    public Mono<String> steamMyGameList(String steamID, HttpServletResponse response) {
        // http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=B52D1A3402E850E0F56BE12E89F145C6&steamid=76561198272883644&format=json
        try {
            // WebClient 요청 코드
            return WebClient.create("http://api.steampowered.com")
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/IPlayerService/GetOwnedGames/v0001")
                            .queryParam("key", steamWebApiKey)
                            .queryParam("steamid", steamID)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(body -> {
                        SteamGameTO[] gameList = steamAction.getMyGameList(body);
                        return WebClient.builder()
                                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                                .baseUrl("https://api.steampowered.com")
                                .build()
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/ISteamApps/GetAppList/v2")
                                        .build())
                                .retrieve()
                                .bodyToMono(String.class)
                                .map(jsonData -> {
                                    Map<Integer,String> gameMap = steamAction.getGameMap(jsonData);

                                    for (SteamGameTO game : gameList) {
                                        int appid = game.getAppid();
                                        String name = gameMap.get(appid);
                                        if(name==null) name = "Service Terminated";
                                        game.setName(name);
                                    }
                                    return gameList;
                                });
                    }).flatMap(gameList -> {
                        StringBuilder sb = new StringBuilder();
                        for(int i = 0;i<gameList.length;i++){
                            float playtime = gameList[i].getPlaytime()/60f;
                            sb.append("<a href = http://localhost:8080/steamapi/gameNews?gameID="+gameList[i].getAppid()+">뉴스 보기 </a>");
                            sb.append(gameList[i].getAppid()+ " / " +gameList[i].getName()+"의 플레이시간 : "+ String.format("%.1f", playtime)+"시간");
                            sb.append("</a>");
                            sb.append("</br>");
                        }

                        return Mono.just(sb.toString());
                    })
                    .onErrorResume(e -> {
                        // 에러 처리 및 적절한 응답 반환
                        System.err.println(e);
                        e.printStackTrace();
                        return Mono.just("게임 목록 처리 오류");
                    });
        } catch (WebClientResponseException ex) {
            // 상세 응답 정보 확인
            System.out.println("Response body: " + ex.getResponseBodyAsString());
            // System.out.println("Response headers: " + ex.getResponseHeaders());
            return Mono.just("게임 목록 처리 오류");
        }

    }
    // // http://localhost:8080/steamapi/myGameAchievement?steamID=76561198272883644&gameID=4000 
    // @GetMapping("/myGameAchievement")
    // public String steamMyGameAchievement(String steamID, String gameID) {
    //     // http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?appid=440&key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX&steamid=76561197972495328
    //     try {
    //         String body = WebClient.create("http://api.steampowered.com")
    //                 .get()
    //                 .uri(uriBuilder -> uriBuilder
    //                         .path("/ISteamUserStats/GetPlayerAchievements/v0001")
    //                         .queryParam("appid", gameID)
    //                         .queryParam("key", steamWebApiKey)
    //                         .queryParam("steamid", steamID)
    //                         .build())
    //                 .retrieve()
    //                 .bodyToMono(String.class)
    //                 .block();
    //         return body;
    //     } catch (Exception e) {
    //         System.err.println(e);
    //     }
    //     return "정보 없음";
    // }
    // @GetMapping("/gameAchievement")
    // public String steamGameAchievement(String gameID) {
    //     // http://api.steampowered.com/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/?gameid=76561198272883644&format=json
    //     try {
    //         String body = WebClient.create("http://api.steampowered.com")
    //                 .get()
    //                 .uri(uriBuilder -> uriBuilder
    //                         .path("/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002")
    //                         .queryParam("gameid", gameID)
    //                         .queryParam("format", "json")
    //                         .build())
    //                 .retrieve()
    //                 .bodyToMono(String.class)
    //                 .block();
    //         return body;
    //     } catch (Exception e) {
    //         System.err.println(e);
    //     }
    //     return "정보 없음";
    // }
}
