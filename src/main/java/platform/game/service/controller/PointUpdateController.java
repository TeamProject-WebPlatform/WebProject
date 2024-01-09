package platform.game.service.controller;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import platform.game.service.entity.Battle;
import platform.game.service.model.TO.BattlePointTO;
import platform.game.service.repository.BattleRepository;

@Controller
public class PointUpdateController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    BattleRepository battleRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;


    @MessageMapping("/pointbetting/{data}")
    public void pointUpdate(@DestinationVariable String data, String pointInfo)
            throws JsonMappingException, JsonProcessingException {
        // 메시지 처리 로직
        String btId = data.split("-")[0];
        String flag = data.split("-")[1];

        String topic = "/topic/pointbetting/" + btId;
        JsonNode jsonNode = objectMapper.readTree(pointInfo);
        int point = jsonNode.get("point").asInt();

        AtomicReference<Boolean> successFlag = new AtomicReference<>(true);        
        successFlag.set(true);
        transactionTemplate.execute(status -> {
            // BATTLE 테이블에 포인트 정보 변경
            // 성공한다면 멤버 포인트 정보에 추가
            
            try {
                if (flag.equals("0")) {
                    // 호스트
                    battleRepository.updateHostBetPoint(btId, point);
                } else if (flag.equals("1")) {
                    // 클라이언트
                    battleRepository.updateClientBetPoint(btId, point);
                }
            } catch (Exception e) {
                System.err.println("Exception in transaction: " + e.getMessage());
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
            Optional<Battle> optionalBattle = battleRepository.findByBtId(Integer.parseInt(btId));
            Battle battle = optionalBattle.get();
            BattlePointTO to = new BattlePointTO(battle);

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