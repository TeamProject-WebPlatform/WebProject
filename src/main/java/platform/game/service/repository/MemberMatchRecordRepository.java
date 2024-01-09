package platform.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.MemberMatchRecord;

@Repository
public interface MemberMatchRecordRepository extends JpaRepository<MemberMatchRecord, Integer> {

    MemberMatchRecord findByGameCdAndMemId(String gameCd, long memId);
}
