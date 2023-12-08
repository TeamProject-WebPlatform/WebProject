package platform.game.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.ComponentScan;

import platform.game.model.TO.UserTO;

@Mapper
@ComponentScan(basePackages = {"platform.game.model"})
public interface SqlMapperInter {
    @Select("select * from user where id = #{id} and password=#{password}")
    public UserTO getUserTObyIDandPass(String id,String password);

    
    
}