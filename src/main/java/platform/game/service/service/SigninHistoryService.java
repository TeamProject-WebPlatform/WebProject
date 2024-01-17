package platform.game.service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import platform.game.service.entity.Member;
import platform.game.service.entity.SigninHistory;
import platform.game.service.repository.SigninHistoryRepository;

@Service
public class SigninHistoryService {

    @Autowired
    private SigninHistoryRepository signinHistoryRepository;

    @Transactional
    public boolean isFirstLogin(Member member) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        SigninHistory latestSignin = signinHistoryRepository.findTopByMemberOrderByCreatedAtDesc(member);

        if (latestSignin == null || latestSignin.getCreatedAt().isAfter(currentDateTime)) {
            // 첫 로그인 시 모든 로그인 기록 삭제
            deleteByMember(member);
            return true;
        }

        return false;
    }

    @Transactional
    public void deleteByMember(Member member) {
        signinHistoryRepository.deleteByMember(member);
    }
}