package platform.game.service.action;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import platform.game.service.entity.Member;
import platform.game.service.entity.SigninHistory;
import platform.game.service.repository.SigninHistoryRepository;
import platform.game.service.repository.UpdatePointHistory;
import platform.game.service.service.SigninHistoryService;

public class LoginAction {

    private final SigninHistoryService signinHistoryService;
    private final UpdatePointHistory updatePointHistory;
    private final SigninHistoryRepository signinHistoryRepository;

    @Autowired
    public LoginAction(SigninHistoryService signinHistoryService, 
                       UpdatePointHistory updatePointHistory, 
                       SigninHistoryRepository signinHistoryRepository) {
        this.signinHistoryService = signinHistoryService;
        this.updatePointHistory = updatePointHistory;
        this.signinHistoryRepository = signinHistoryRepository;
    }

    public Object[] getLogin(Member member) {
        // 첫 로그인 여부 업데이트 및 포인트 증가
        boolean isFirstLogin = signinHistoryService.isFirstLogin(member);
        Object[] o = new Object[2];

        o[0] = false;
        o[1] = 0;

        if (isFirstLogin) {
            int updatedPoints = updatePointHistory.insertPointHistoryByMemId(member.getMemId(), "50201", 10);
            if (updatedPoints < 0) {
                System.out.println("포인트 증가 실패");
                return o;
            }else{
                o[0] = true;
                o[1] = updatedPoints;
            }
            System.out.println("포인트 증가 성공");
        } else {
            System.out.println(isFirstLogin);
            System.out.println("이미 로그인한 사용자입니다.");
        }

        System.out.println("로그인 성공");

        // SigninHistory 저장
        SigninHistory signinHistory = SigninHistory.builder()
                .member(member)
                .createdAt(LocalDateTime.now())
                .build();

        // SigninHistory 저장
        signinHistoryRepository.save(signinHistory);

        return o;
    }
}

