package platform.game.service.repository;

public interface UpdatePointHistory{
    int insertPointHistoryByMemId(long memId,String pointKindCd,int pointCnt) throws RuntimeException;
}
