package platform.game.service.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Member;

@Repository
public interface MemberInfoRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemUserid(String mem_userid);

    Optional<Member> findByMemSteamid(String memSteamid);

    Optional<Member> findByMemKakaoid(String memKakaoid);

    Optional<Member> findByMemNick(String mem_nickname);

    // @Query("SELECT MAX(m.memId) FROM MEMBER m WHERE SUBSTRING(m.memId,1,3) <
    // :maxValue")
    // @Query("SELECT MAX(m.memId) FROM Member m WHERE CAST(m.memId AS CHAR) LIKE
    // :startNum%")
    // Optional<Integer> findMaxMemId(@Param("startNum") String startNum);

    boolean existsByMemUserid(String mem_userid);

    boolean existsByMemNick(String mem_nickname);

    boolean existsByMemEmail(String mem_mail);

    @Query(value = "select mem_pw from member where mem_userid = :mem_userid", nativeQuery = true)
    String findByMemPw(@Param("mem_userid") String mem_userid);

    // 멤버 생성 시 랭킹 테이블 맨 아래로 등록하기 위한 멤버수 구하기
    Long countBy();

    Optional<Member> findByMemId(long memId);

    Member findByMemIdOrderByMemIdDesc(long memId);
}
