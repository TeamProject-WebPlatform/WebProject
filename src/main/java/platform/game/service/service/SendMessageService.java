package platform.game.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import platform.game.service.entity.MemberBetting;
import platform.game.service.model.TO.BattleMemberTO;
import platform.game.service.model.TO.BettingStateInfoTO;
import platform.game.service.model.TO.HeaderInfoTO;
import platform.game.service.model.TO.MemberBettingTO;

@Service
public class SendMessageService {
    
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

    }
    public void sendMessageToChangeState(int btId, String state, String result,int point) throws JsonProcessingException{
        //웹소켓 엔드포인트로 메시지를 보냄
        BettingStateInfoTO bettingStateInfo = new BettingStateInfoTO();
        bettingStateInfo.setBtId(btId);  
        bettingStateInfo.setState(state);   
        bettingStateInfo.setResult(result);
        bettingStateInfo.setPoint(point);
        // BettingInfoTO 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(bettingStateInfo);

        String topic = "/topic/pointbetting/" + bettingStateInfo.getBtId();
        messagingTemplate.convertAndSend(topic, jsonString);

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

        String topic = "/topic/pointbetting/" + bettingStateInfo.getBtId();
        messagingTemplate.convertAndSend(topic, jsonString);

    }
    public void sendMessageToChagePoint(long memId, int currentPoint,String cd) throws JsonProcessingException{
        HeaderInfoTO to = new HeaderInfoTO();
        to.setMemId(memId);
        to.setCurrentPoint(currentPoint);
        to.setFlag(1);
        to.setPointKindCd(cd);

        // BettingInfoTO 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(to);

        String topic = "/topic/headerInfo/" + memId;
        messagingTemplate.convertAndSend(topic, jsonString);

    }
    public void sendMessageToDistributePoint(int btId,List<MemberBettingTO> list) throws JsonProcessingException{
        DSBTTO to = new DSBTTO();
        to.setList(list);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(to);

        String topic = "/topic/pointbetting/" + btId;
        messagingTemplate.convertAndSend(topic, jsonString);
    }

    @Data
    class DSBTTO{
        int endpoint = 2;
        List<MemberBettingTO> list;
    }
}