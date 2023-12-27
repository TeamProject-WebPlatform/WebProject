package platform.game.service.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component
public class SteamAction {

    @Data
    public class SteamGameTO {
        private int appid;
        private int playtime;
        private String name;
    }

    @Data
    public class SteamNewsTO {
        private int appid;
        private String title;
        private String url;
    }

    @Data
    @AllArgsConstructor
    public class SteamPlayerSummaryTO {
        private String nickname;
        private String profileUrl;
        private String avatarUrl;
    }

    public SteamGameTO[] getMyGameList(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject response = jsonObject.getAsJsonObject("response");
        JsonArray games = response.getAsJsonArray("games");

        SteamGameTO[] list = new SteamGameTO[games.size()];
        for (int i = 0; i < games.size(); i++) {
            JsonObject game = games.get(i).getAsJsonObject();
            int appid = game.get("appid").getAsInt();
            int playtime = game.get("playtime_forever").getAsInt();
            SteamGameTO gameInfo = new SteamGameTO();
            gameInfo.setAppid(appid);
            gameInfo.setPlaytime(playtime);

            list[i] = gameInfo;
        }
        return list;
    }

    public Map<Integer, String> getGameMap(String jsonData) {
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
        JsonObject appList = jsonObject.getAsJsonObject("applist");
        JsonArray apps = appList.getAsJsonArray("apps");
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < apps.size(); i++) {
            JsonObject app = apps.get(i).getAsJsonObject();
            int appid = app.get("appid").getAsInt();
            String name = app.get("name").getAsString();
            map.put(appid, name);
        }
        return map;
    }

    public SteamNewsTO[] getGameNews(String jsonData){
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
        JsonObject appNews = jsonObject.getAsJsonObject("appnews");
        
        int appid = appNews.get("appid").getAsInt();
        JsonArray newItems = appNews.getAsJsonArray("newsitems");
        
        SteamNewsTO[] list = new SteamNewsTO[newItems.size()];
        for(int i = 0;i<newItems.size();i++){
            JsonObject newsItem = newItems.get(i).getAsJsonObject();
            String title = newsItem.get("title").getAsString();
            String url = newsItem.get("url").getAsString();

            SteamNewsTO steamNewsTO = new SteamNewsTO();
            steamNewsTO.setAppid(appid);
            steamNewsTO.setTitle(title);
            steamNewsTO.setUrl(url);

            list[i] = steamNewsTO;
        }
        
        return list;
    }
    public SteamPlayerSummaryTO getPlayerSummary(String jsonData){
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
        JsonObject response = jsonObject.getAsJsonObject("response");
        JsonObject players = response.getAsJsonArray("players").get(0).getAsJsonObject();
        String name = players.get("personaname").getAsString();
        String profilelUrl = players.get("profileurl").getAsString();
        String avatarUrl = players.get("avatarfull").getAsString();

        SteamPlayerSummaryTO steamUserTO = new SteamPlayerSummaryTO(name,profilelUrl,avatarUrl);

        return steamUserTO;
    }
}
