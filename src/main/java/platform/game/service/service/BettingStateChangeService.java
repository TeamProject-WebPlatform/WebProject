package platform.game.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import platform.game.service.model.TO.BattleMemberTO;
import platform.game.service.model.TO.BettingStateInfoTO;

@Service
public class BettingStateChangeService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public void sendMessageToChangeState(int btId, String state) throws JsonProcessingException{
        //웹소켓 엔드포인트로 메시지를 보냄
        BettingStateInfoTO bettingStateInfo = new BettingStateInfoTO();
        bettingStateInfo.setBtId(btId);  
        bettingStateInfo.setState(state);   

        // BettingInfoTO 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(bettingStateInfo);

        String topic = "/topic/pointbetting/" + bettingStateInfo.getBtId();
        messagingTemplate.convertAndSend(topic, jsonString);
        System.out.println("메세지 전송함");
    }
    public void sendMessageToChangeState(int btId, String state,long delay, BattleMemberTO client) throws JsonProcessingException{
        //웹소켓 엔드포인트로 메시지를 보냄
        BettingStateInfoTO bettingStateInfo = new BettingStateInfoTO();
        bettingStateInfo.setBtId(btId);  
        bettingStateInfo.setState(state);   
        bettingStateInfo.setDelay(delay);
        bettingStateInfo.setClient(client);

        // BettingInfoTO 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(bettingStateInfo);
        System.out.println(1);
        String topic = "/topic/pointbetting/" + bettingStateInfo.getBtId();
        messagingTemplate.convertAndSend(topic, jsonString);
        System.out.println("메세지 전송함");
    }
    
}