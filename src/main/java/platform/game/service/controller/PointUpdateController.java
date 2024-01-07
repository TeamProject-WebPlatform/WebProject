package platform.game.service.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PointUpdateController {
    @GetMapping("/test")
    public String test(){
        return "websocketTest";
    }
    @MessageMapping("/sendMessage")
    @SendTo("/topic/receiveMessage")
    public String handleIncomingMessage(String message) {
        // 메시지 처리 로직
        return "서버에서 받은 메시지: " + message;
    }
}