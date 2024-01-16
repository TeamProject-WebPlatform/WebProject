package platform.game.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.game.service.entity.Member;
import platform.game.service.entity.SigninHistory;
import platform.game.service.repository.SigninHistoryRepository;

import java.util.List;

@Service
public class SigninHistoryService {

    @Autowired
    private SigninHistoryRepository signinHistoryRepository;

    public boolean isFirstLogin(Member member) {
        // 특정 사용자의 로그인 기록을 시간 내림차순으로 조회
        List<SigninHistory> signinHistories = signinHistoryRepository.findByMemberOrderByCreatedAtDesc(member);

        // 로그인 기록이 없으면 첫 로그인
        return signinHistories.isEmpty();
    }
}
