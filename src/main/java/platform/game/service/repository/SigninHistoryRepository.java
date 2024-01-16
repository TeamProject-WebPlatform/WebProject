package platform.game.service.repository;


import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Member;
import platform.game.service.entity.SigninHistory;
import platform.game.service.entity.compositeKey.SigninHistoryId;

@Repository
public interface SigninHistoryRepository extends JpaRepository<SigninHistory, SigninHistoryId>{

    // 특정 사용자의 로그인 기록을 조회하는 메서드
    ArrayList<SigninHistory> findByMemberOrderByCreatedAtDesc(Member member);

    //특정 IP 주소로 로그인한 기록을 조회하는 메서드
    ArrayList<SigninHistory> findByMemIp(String memIp);

    //특정 시간 이후의 로그인 기록을 조회하는 메서드
    ArrayList<SigninHistory> findByCreatedAtAfter(LocalDateTime createdAt);

    // 특정 멤버의 총 로그인 횟수를 조회하는 메서드
    int countByMember(Member member);
}
