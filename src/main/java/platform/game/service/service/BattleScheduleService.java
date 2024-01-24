package platform.game.service.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class BattleScheduleService {
    
    @Autowired
    @Lazy
    AsyncTaskService asyncTaskService;

    public void battleStartSchedule(int btId, int postId, Date date) {
        // date는 배틀 시작시간
        asyncTaskService.scheduleTask(btId, postId, date, 1);
    }
}
