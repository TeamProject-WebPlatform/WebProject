package platform.game.service.service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import platform.game.service.repository.BattleCustomRepositoryImpl;

@Service
public class AsyncTaskService {

    @Autowired
    @Lazy
    BattleCustomRepositoryImpl battleCustomRepositoryImpl;

    public void scheduleTask(int btId, int postId, Date date,int flag) {
        try {
            long startTime = date.getTime();
            long curTime = System.currentTimeMillis();
            long delay = 0;
            long targetTime = 0;

            if(flag==0){
                long minimumTime = TimeUnit.MINUTES.toMillis(15);
                targetTime = startTime - minimumTime;
                if(targetTime < minimumTime) targetTime = minimumTime;
                delay = targetTime - curTime;
            }else if(flag==1){
                long waiting = TimeUnit.MINUTES.toMillis(30);
                targetTime = startTime+waiting;
                delay = targetTime - curTime;
            }

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            
            System.out.println("BattleScheduleService 스케쥴 flag:"+flag+" - Delay : "+delay+"초");
            executorService.schedule(() -> {
                // 예약된 시간에 실행할 작업
                if (flag==0) {
                    // Betting 종료
                    battleCustomRepositoryImpl.terminateBetting(btId);
                } else if(flag==1){
                    // 배틀 자동 패배
                    battleCustomRepositoryImpl.controlBattle(3, btId, postId);
                }
                executorService.shutdown(); // 작업 완료 후 executor 종료
            }, delay, TimeUnit.MILLISECONDS);
            
        } catch (Exception e) {
            throw new RuntimeException("비동기 작업 중 오류 발생", e);
        }
    }
}
