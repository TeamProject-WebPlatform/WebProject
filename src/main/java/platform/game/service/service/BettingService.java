package platform.game.service.service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class BettingService {

    @Autowired
    @Lazy
    AsyncTaskService asyncTaskService;

    public CompletableFuture<Long[]> bettingSchedule(int btId, Date date) {
        // date는 배틀 시작시간
        return asyncTaskService.scheduleTask(btId, 0, date, 1);
    }
}
