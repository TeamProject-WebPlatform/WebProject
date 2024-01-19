package platform.game.service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Member;
import platform.game.service.entity.SigninHistory;
import platform.game.service.entity.compositeKey.SigninHistoryId;

@Repository
public interface SigninHistoryRepository extends JpaRepository<SigninHistory, SigninHistoryId> {

    // 특정 사용자의 로그인 기록을 조회하는 메서드
    List<SigninHistory> findByMemberOrderByCreatedAtDesc(Member member);
    
    // 특정 사용자의 최근 로그인 기록을 조회하는 메서드
    SigninHistory findTopByMemberOrderByCreatedAtDesc(Member member);
    
    // 특정 IP 주소로 로그인한 기록을 조회하는 메서드
    List<SigninHistory> findByMemIp(String memIp);
    
    // 특정 시간 이후의 로그인 기록을 조회하는 메서드
    List<SigninHistory> findByCreatedAtAfter(LocalDateTime createdAt);
    
    // 특정 멤버의 총 로그인 횟수를 조회하는 메서드
    int countByMember(Member member);
    
    // 수정된 부분: JPQL을 사용한 삭제 쿼리
    void deleteByMember(Member member);

    // 추가: JPQL을 사용한 모든 로그인 기록 삭제 쿼리
    void deleteAllByMember(Member member);
}
