package platform.game.service.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
public class UpdateMemberRepositoryImpl implements UpdateMemberRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public int updateMemCurPointByMemId(long memId, int point) {
        Query query = entityManager.createNativeQuery(
            "UPDATE member "+
            "SET mem_cur_point = "+
                "CASE WHEN (mem_cur_point + :point) >= 0 "+
                "THEN (mem_cur_point + :point) "+
                "ELSE mem_cur_point END, "+
            "mem_total_point = "+
                "CASE WHEN :point > 0 "+
                "THEN (mem_total_point + :point) "+
                "ELSE mem_total_point END "+
            "WHERE mem_id = :memId");
        query.setParameter("memId", memId);
        query.setParameter("point", point);
        return query.executeUpdate();
    }
     
    @Transactional
    @Override
    public int insertData(int point, long memId, int btId, int flag) {
        Query query = entityManager.createNativeQuery(
                "INSERT INTO member_betting " +
                "VALUES(now(),:point,:memId,:btId,:flag,0,'-1')");
        query.setParameter("point", point);
        query.setParameter("memId", memId);
        query.setParameter("btId", btId);
        query.setParameter("flag", flag);
        
        int res = query.executeUpdate();
        if (res != 1)
            throw new RuntimeException("BattleCustomRepoImpl 트랜잭션 롤백");

        return res;
    }
}