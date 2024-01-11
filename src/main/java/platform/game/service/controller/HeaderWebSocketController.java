package platform.game.service.controller;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import platform.game.service.repository.MemberInfoRepository;
import platform.game.service.repository.UpdateMemberRepositryImpl;
import platform.game.service.service.MemberInfoDetails;

@Controller
public class HeaderWebSocketController {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private UpdateMemberRepositryImpl updateMemberRepositryImpl;
    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @MessageMapping("/memPointChange")
    public void memPointChange(@Payload HeaderInfoTO to) throws JsonMappingException, JsonProcessingException {
        // Long memId = Long.parseLong(memIdstr);
        long memId = to.getMemId();
        int pointChange = to.getPointChange();
        // 여기서 이제 DB에 포인트 업데이트하고 가져오기
        // 여기서 포인트 0이하라 부족할때 못사는 로직
        AtomicReference<Boolean> successFlag = new AtomicReference<>(true);        
        successFlag.set(true);
        transactionTemplate.execute(status -> {
            try {
                int flag = updateMemberRepositryImpl.updateMemCurPointByMemId(memId, pointChange);
                if(flag==1){
                    to.setFlag(flag);
                }else{
                    successFlag.set(false);
                }
            } catch (Exception e) {
                System.err.println("Transaction error : "+e.getMessage());
                // 롤백을 수행하도록 표시
                successFlag.set(false);
                status.setRollbackOnly();
            }
            return null;
        });
        // to.setCurrentPoint(point);
        String topic = "/topic/headerInfo/" + memId;
        Optional<Member> member = memberInfoRepository.findById(memId);
        if(member.isPresent()){
            int point = member.get().getMemCurPoint();
            to.setCurrentPoint(point);
        }
        String jsonString = objectMapper.writeValueAsString(to);
        messagingTemplate.convertAndSend(topic, jsonString);
    }
}
