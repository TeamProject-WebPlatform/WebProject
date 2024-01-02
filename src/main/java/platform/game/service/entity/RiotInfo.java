package platform.game.service.entity;

import lombok.Data;

@Data
public class RiotInfo {
    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String name;
    private String id;
    private String puuid;
    private long summonerLevel;
    
    // private String leagueId;
    // private String queueType;
    // private String tier;
    // private String rank;
    // private String summonerId;
    // private String summonerName;
    // private String leaguePoints;
    // private String wins;
    // private String losses;
    // private String veteran;
    // private String inactive;
    // private String freshBlood;
    // private String hotStreak;
}