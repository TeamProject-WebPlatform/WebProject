package platform.game.service.service;

import java.time.LocalDate;
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

        if (latestSignin == null || !latestSignin.getCreatedAt().toLocalDate().isEqual(currentDateTime.toLocalDate())) {
            // 첫 로그인 시 모든 로그인 기록 삭제
            deleteByMember(member);

            // 첫 로그인 시 현재 시간을 저장
            logFirstLogin(member, currentDateTime);
            System.out.println("첫 로그인");
            return true;
        }

        return false;
    }

    @Transactional
    public void deleteByMember(Member member) {
        signinHistoryRepository.deleteByMember(member);
    }

    private void logFirstLogin(Member member, LocalDateTime loginTime) {

        // SigninHistory에 저장
        SigninHistory signinHistory = SigninHistory.builder()
                .member(member)
                .createdAt(loginTime)
                .build();

        signinHistoryRepository.save(signinHistory);

        // 여기에 추가적인 로그 또는 작업을 수행할 수 있습니다.
        System.out.println("첫 로그인 - 사용자: " + member.getMemId() + ", 날짜: " + loginTime);
    }
}