package platform.game.service.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import platform.game.service.entity.Member;
import platform.game.service.service.LevelService;

@Repository
@Qualifier("updatePointHistoryImpl")
public class UpdatePointHistoryImpl implements UpdatePointHistory {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MemberInfoRepository memberInfoRepository;
    @Autowired
    private LevelService levelService;
    @Transactional
    @Override
    public int insertPointHistoryByMemId(long memId, String pointKindCd, int pointCnt) {
        // 업데이트후 currentPoint를 리턴
        int currentPoint;
        int totalPoint;
        int level;
        // Member 테이블의 멤버 point 정보 확인    
        Optional<Member> member = memberInfoRepository.findById(memId);
        if (member.isPresent()) {
            currentPoint = member.get().getMemCurPoint();
            totalPoint = member.get().getMemTotalPoint();
            level = levelService.calculateLevel(totalPoint);
            if (pointCnt == 0){
                return currentPoint;
            }
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
                        "ELSE mem_total_point END, " +
                        "mem_lvl = :mem_lvl "+
                        "WHERE mem_id = :memId");
        query.setParameter("memId", memId);
        query.setParameter("point", pointCnt);
        query.setParameter("mem_lvl", level);
        int flag = query.executeUpdate();
        if (flag != 1)
            throw new RuntimeException("UpdatePointHistoryImtl 트랜잭션 롤백");

        // PointHistroy Id 값 커스텀
        Integer maxPointHistoryId = findMaxPointHistoryId(memId);
        maxPointHistoryId = (maxPointHistoryId != null) ? maxPointHistoryId + 1 : 1;

        // PointHistory 테이블 인서트
        query = entityManager.createNativeQuery(
                "INSERT INTO point_history (mem_id,point_history_id,point_kind_cd, point_cnt,created_at,cur_point,total_point) " +
                        "VALUES(:memId,:pointHistoryId,:pointKindCd,:pointCnt,now(),:currentPoint,:totalPoint)");
        query.setParameter("memId", memId);
        query.setParameter("pointHistoryId", maxPointHistoryId);
        query.setParameter("pointKindCd", pointKindCd);
        query.setParameter("pointCnt", pointCnt);
        query.setParameter("currentPoint", currentPoint + pointCnt);
        if(pointCnt>0) query.setParameter("totalPoint", totalPoint + pointCnt);
        else query.setParameter("totalPoint", totalPoint);
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
