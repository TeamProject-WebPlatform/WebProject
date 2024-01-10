package platform.game.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.Header;

import lombok.Data;
import platform.game.service.entity.Member;
import platform.game.service.model.TO.BettingInfoTO;
import platform.game.service.model.TO.HeaderInfoTO;
import platform.game.service.service.MemberInfoDetails;

@Controller
public class HeaderWebSocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @MessageMapping("/memPointChange")
    public void memPointChange(@Payload HeaderInfoTO to)
            throws JsonMappingException, JsonProcessingException {
        // Long memId = Long.parseLong(memIdstr);
        long memId = to.getMemId();
        int point = to.getPointChange();

        // 여기서 이제 DB에 포인트 업데이트하고 가져오기
        // 여기서 포인트 0이하라 부족할때 못사는 로직

        // to.setCurrentPoint(point);
        String topic = "/topic/headerInfo/" + memId;
        
        String jsonString = objectMapper.writeValueAsString(to);
        messagingTemplate.convertAndSend(topic, jsonString);
    }
}
