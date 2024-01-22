package platform.game.service.repository;

import org.apache.ibatis.annotations.Param;

public interface UpdateMemberRepository {
    int updateMemCurPointByMemId(long memId, int point);
    int insertData(int point,long memId, int btId, int flag);
}
