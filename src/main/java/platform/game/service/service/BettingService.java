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
public class BettingService {
    
    @Autowired
    @Lazy
    BattleCustomRepositoryImpl battleCustomRepositoryImpl;

    @Async
    public CompletableFuture<Long[]> bettingSchedule(int btId, Date date){
        //date는 배틀 시작시간
        try {
            long minimumTime = TimeUnit.MINUTES.toMillis(15); // 15분을 밀리초로 변환
            // 클라이언트가 결정된 순간
            long clientDecisionTime = System.currentTimeMillis();
            // 배틀 시작 시간
            long battleStartTime = date.getTime(); 

            // targetTime 계산
            long targetTime = battleStartTime - minimumTime;
            if(targetTime < minimumTime) targetTime = minimumTime;

            // ScheduledExecutorService 생성
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            long delay = targetTime - clientDecisionTime;
            // 특정 시간에 작업 예약
            executorService.schedule(() -> {
                // 예약된 시간에 실행할 작업
                battleCustomRepositoryImpl.terminateBetting(btId);
                executorService.shutdown(); // 작업 완료 후 executor 종료
                System.out.println("스케쥴 완료");
            }, delay, TimeUnit.MILLISECONDS);
            Long[] data = new Long[2];
            data[0] = targetTime;
            data[1] = delay;
            return CompletableFuture.completedFuture(data);
        } catch (Exception e) {
            throw new RuntimeException("비동기 작업 중 오류 발생", e);
        }
    }
}
