package platform.game.service.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Member;

@Repository
public class UpdatePointHistoryImpl implements UpdatePointHistory {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @Transactional
    @Override
    public int insertPointHistoryByMemId(long memId, String pointKindCd, int pointCnt) {
        // 업데이트후 currentPoint를 리턴
        int currentPoint;
        // Member 테이블의 멤버 point 정보 확인    
        Optional<Member> member = memberInfoRepository.findById(memId);
        if (member.isPresent()) {
            currentPoint = member.get().getMemCurPoint();
            if (currentPoint + pointCnt < 0) {
                return -1;
            }
        } else{
            return -1;
        }

        // Member 테이블의 멤버 point 정보 업데이트
        Query query = entityManager.createNativeQuery(
                "UPDATE member " +
                        "SET mem_cur_point = " +
                        "CASE WHEN (mem_cur_point + :point) >= 0 " +
                        "THEN (mem_cur_point + :point) " +
                        "ELSE mem_cur_point END, " +
                        "mem_total_point = " +
                        "CASE WHEN :point > 0 " +
                        "THEN (mem_total_point + :point) " +
                        "ELSE mem_total_point END " +
                        "WHERE mem_id = :memId");
        query.setParameter("memId", memId);
        query.setParameter("point", pointCnt);
        int flag = query.executeUpdate();
        if (flag != 1)
            throw new RuntimeException("UpdatePointHistoryImtl 트랜잭션 롤백");

        // PointHistroy Id 값 커스텀
        Integer maxPointHistoryId = findMaxPointHistoryId(memId);
        maxPointHistoryId = (maxPointHistoryId != null) ? maxPointHistoryId + 1 : 1;

        // PointHistory 테이블 인서트
        query = entityManager.createNativeQuery(
                "INSERT INTO point_history (mem_id,point_history_id,point_kind_cd, point_cnt,created_at) " +
                        "VALUES(:memId,:pointHistoryId,:pointKindCd,:pointCnt,now())");
        query.setParameter("memId", memId);
        query.setParameter("pointHistoryId", maxPointHistoryId);
        query.setParameter("pointKindCd", pointKindCd);
        query.setParameter("pointCnt", pointCnt);

        flag = query.executeUpdate();
        if (flag != 1)
            throw new RuntimeException("UpdatePointHistoryImtl 트랜잭션 롤백");

        return currentPoint + pointCnt;
    }

    private Integer findMaxPointHistoryId(Long memId) {
        // 현재 member의 pointHistoryId 중 최대값 조회
        String jpql = "SELECT MAX(ph.pointHistoryId) FROM PointHistory ph WHERE ph.member.memId = :memId";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        query.setParameter("memId", memId);
        return query.getSingleResult();
    }
}
