package platform.game.model.DAO;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import platform.game.mapper.SqlMapperInter;
import platform.game.model.TO.UserTO;

@Repository
@MapperScan(basePackages = {"platform.game.mapper"})
public class UserDAO {
    @Autowired
    private SqlMapperInter mapper;
    
    public UserTO getUserTObyIDandPass(String id, String password){
        UserTO to = mapper.getUserTObyIDandPass(id,password);

        return to;
    }
}
