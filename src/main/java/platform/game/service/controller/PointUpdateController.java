package platform.game.service.controller;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import platform.game.service.entity.Battle;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.model.TO.BettingInfoTO;
import platform.game.service.repository.BattleRepository;
import platform.game.service.repository.MemberBettingRepository;
import platform.game.service.repository.UpdatePointHistoryImpl;

@Controller
public class PointUpdateController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    BattleRepository battleRepository;
    @Autowired
    MemberBettingRepository memberBettingRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private UpdatePointHistoryImpl updatePointHistoryImpl;


    @MessageMapping("/pointbetting")
    public void pointUpdate(@Payload BettingInfoTO bettingInfo)
            throws JsonMappingException, JsonProcessingException {
        // 메시지 처리 로직

        int point = bettingInfo.getPoint();
        int flag = bettingInfo.getFlag();
        int btId = bettingInfo.getBtId();
        Long memId = bettingInfo.getMemId();
        String topic = "/topic/pointbetting/" + btId;
        
        AtomicReference<Boolean> successFlag = new AtomicReference<>(true);        
        successFlag.set(true);
        transactionTemplate.execute(status -> {
            // BATTLE 테이블에 포인트 정보 변경
            // 성공한다면 멤버 포인트 정보에 추가
            
            try {
                if (flag==0) {
                    // 호스트
                    battleRepository.updateHostBetPoint(btId, point);
                } else if (flag==1) {
                    // 클라이언트
                    battleRepository.updateClientBetPoint(btId, point);
                }
                // 여기에 memId를 기준으로 member_betting에 정보 추가
                // 이후에 추가된 정보로 베팅 현황 알수 있게
                memberBettingRepository.insertData(point,memId,btId,flag);
            } catch(DataIntegrityViolationException e){
                // 이미 베팅한 곳에 또 베팅 하면 뜨는 에러
                successFlag.set(false);
                status.setRollbackOnly();
            } catch (Exception e) {
                System.err.println("Transaction error : "+e.getMessage());
                // 롤백을 수행하도록 표시
                successFlag.set(false);
                status.setRollbackOnly();
            }
            return null;
        });
        


        try {
            if(!successFlag.get()){
                messagingTemplate.convertAndSend(topic, "");
                return;
            }
            // 객체를 JSON 문자열로 변환
            Optional<Battle> optionalBattle = battleRepository.findByBtId(btId);
            Battle battle = optionalBattle.get();
            BattlePointTO to = new BattlePointTO(battle);
            to.setFlag(flag);
            String jsonString = objectMapper.writeValueAsString(to);
            // 메시지 전송
            messagingTemplate.convertAndSend(topic, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            messagingTemplate.convertAndSend(topic, "");
            // JSON 변환 오류 처리
        }
    }
}