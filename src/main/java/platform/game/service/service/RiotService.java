package platform.game.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import platform.game.service.entity.RiotInfo;

@Service
@PropertySource(ignoreResourceNotFound = false, value = "classpath:properties/env.properties")
public class RiotService {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${riot.api.key}")
    private String mykey;

    public RiotInfo callRiotAPISummonerByName(String summonerName){

        RiotInfo riotInfo;

        String serverUrl = "https://kr.api.riotgames.com";

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=" + mykey);

            HttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() != 200){
                System.out.println( "[에러] RiotService" );
                return null;
            }

            HttpEntity entity = response.getEntity();
            riotInfo = objectMapper.readValue(entity.getContent(), RiotInfo.class);

            request = new HttpGet(serverUrl + "/lol/league/v4/entries/by-summoner/" + riotInfo.getId() + "?api_key=" + mykey);
            response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON 문자열을 JsonNode로 파싱
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // JsonNode를 이용하여 원하는 데이터 추출
            for (JsonNode entry : jsonNode) {
                String tier = entry.get("tier").asText();
                String rank = entry.get("rank").asText();
                int leaguePoints = entry.get("leaguePoints").asInt();

                System.out.println("Tier: " + tier);
                System.out.println("Rank: " + rank);
                System.out.println("League Points: " + leaguePoints);
                System.out.println();
            }
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }

        return riotInfo;
    }
}