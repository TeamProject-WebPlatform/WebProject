package platform.game.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import platform.game.service.entity.MemberMatchRecord;

public interface MemberMatchRecordRepository extends JpaRepository<MemberMatchRecord, Integer> {

    MemberMatchRecord findByGameCdAndMemID(String game_cd, long mem_id);
}
