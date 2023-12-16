package platform.game.service.service.userStats;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import platform.game.service.model.TO.MemberTO;

@Component
@ComponentScan(basePackages = {"platform.game.model"})
public class PointManager {
    static int addPoint(MemberTO to, int amount){ 
        if(to==null) return 0;
        if(amount<0) return to.getPoint();

        int nowPoint = to.getPoint();
        to.setPoint(nowPoint + amount);

        return to.getPoint();
    }
    // 남은 포인트 값 return, 
    // 남은 포인트보다 큰 값이 들어오면 -1 return
    static int removePoint(MemberTO to, int amount){
        if(to==null) return 0;
        if(amount<0) return to.getPoint();

        int nowPoint = to.getPoint();
        int point = nowPoint - amount;
        if(point < 0) return -1;
        to.setPoint(point);
        
        return to.getPoint();
    }
}