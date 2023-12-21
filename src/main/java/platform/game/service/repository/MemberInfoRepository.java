package platform.game.service.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Member;

@Repository
public interface  MemberInfoRepository extends JpaRepository<Member, Integer>{
    Optional<Member> findByMemUserid(String memUserid);
    Optional<Member> findByMemSteamid(String memSteamid);
    Optional<Member> findByMemKakaoid(String memKakaoid);
    // @Query("SELECT MAX(m.memId) FROM MEMBER m WHERE SUBSTRING(m.memId,1,3) < :maxValue")
    // @Query("SELECT MAX(m.memId) FROM Member m WHERE CAST(m.memId AS CHAR) LIKE :startNum%")
    //Optional<Integer> findMaxMemId(@Param("startNum") String startNum);

}
