package platform.game.service.repository;

import org.springframework.stereotype.Repository;

import platform.game.service.entity.MemberRanking;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface MemberRankingRepository extends JpaRepository<MemberRanking, Integer> {

    // 멤버 ID 기준으로 랭킹코드와 그에 맞는 랭킹 가져오기
    @Query(value = "select * from member_ranking m where m.mem_id=:mem_id", nativeQuery = true)
    List<MemberRanking> findByMemId(@Param("mem_id") long mem_id);

    @Query(value = "insert into member_ranking values (:rank_code,:mem_id,:mem_rank)", nativeQuery = true)
    void SetMemberRanking(String rank_code, long mem_id, Long mem_rank);
}
