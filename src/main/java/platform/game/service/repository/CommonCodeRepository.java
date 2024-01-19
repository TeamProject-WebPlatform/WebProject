package platform.game.service.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import platform.game.service.entity.CommonCode;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
    // 코드로 갖고 오기
    Optional<CommonCode> findByCd(String code);

    @Modifying
    @Query(value = "UPDATE common_code SET remark1 = :param WHERE cd = :code", nativeQuery = true)
    void updateRemark1ByCd(@Param("code") String code, @Param("param") String param);

    CommonCode findByCdOrderByCd(String code);

    // 여기에 작성
    @Query(value = "UPDATE common_code SET remark1 = :remark1, remark3 = :remark3 WHERE cd = '99001'", nativeQuery = true)
    void updateRemarksByCd(@Param("remark1") String remark1, @Param("remark3") String remark3);
}
