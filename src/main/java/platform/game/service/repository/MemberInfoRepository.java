package platform.game.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.Member;

@Repository
public interface  MemberInfoRepository extends JpaRepository<Member, Integer>{
    Optional<Member> findByMemUserid(String mem_userid);
}
