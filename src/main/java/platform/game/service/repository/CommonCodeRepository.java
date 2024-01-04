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

}
