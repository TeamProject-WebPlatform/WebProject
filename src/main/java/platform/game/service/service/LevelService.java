package platform.game.service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LevelService {

    public int calculateLevel(int points) {
        int level = 0;
        if(points < 5843224){
            int basePoints = 1000;
            double logBase = 1.1;
            points += basePoints * logBase; // 0->1 레벨 가는거

            level = (int) (Math.log((double) points / basePoints) / Math.log(logBase));
            if(level<0) level = 0;
        }else if(points >= 5843224 && points<6000000){
            level = 90;
        }else if(points>=6000000 && points<11000000){
            level = 91 + (points-6000000)/1000000;
        }else if(points>=11000000){
            level = 95 + (points-10000000)/2000000;
        }
        
        return level;
    }
    public List<Integer> getLevelDesign(){
        List<Integer> levelDesign = new ArrayList();

        int level = 0;
        int points = 0;
        while(level<100){
            int temp = calculateLevel(points);
            if(temp!=level) {
                level = temp;
                levelDesign.add(points);
                // System.out.println("Level: " + level+", Points: " + points);
            }
            points++;
        }
    return levelDesign;
    }
}
