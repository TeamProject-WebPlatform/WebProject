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
<<<<<<< HEAD
    @Query(value = "UPDATE common_code SET com_cd_param1 = :param WHERE com_cd = :code", nativeQuery = true)
=======
    @Query(value = "UPDATE common_code SET remark1 = :param WHERE cd = :code", nativeQuery = true)
>>>>>>> 417321e7a093bfb72c7c554a358ec42ef6f641c4
    void updateRemark1ByCd(@Param("code") String code, @Param("param") String param);

}
