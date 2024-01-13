package platform.game.service.repository;

public interface UpdateItemHistory {
    int insertItemHistoryByMemId(long memId, int memItemId, int itemCd);
}
