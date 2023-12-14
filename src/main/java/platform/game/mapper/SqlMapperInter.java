package platform.game.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.ComponentScan;

@Mapper
@ComponentScan(basePackages = {"platform.game.model"})
public interface SqlMapperInter {
    
    //로그인 쿼리문
    @Select("select * from member where userid = #{userid} and password=#{password}")
    public int getMemberTObyIDandPass(String userid, String password);

    //카카오로그인 확인 쿼리문
    @Select("select * from member where email = #{email}")
    public int checkKakaoMamber(String email);
    //스팀로그인 확인 쿼리문
    @Select("select * from member where email = #{email}")
    public int checkSteamMamber(String email);

    //회원가입DB 입력때 사용할 member_id 받아오는 sql문
    @Select("select last_member_id from member_id_guide where signin_type = 'default' order by last_member_id desc")
    public int getLastMemberId();
    
    //회원가입 완료가 되면 회원번호 디비에 마지막 회원번호 저장하기
    //현재 디폴트만 되어있는데 카카오, 스팀도 가능하도록 변경하기
    @Insert("update member_id_guide set last_member_id = #{last_member_id} where signin_type = 'default'")
    public int setMemberId(int last_member_id);

    // 회원가입DB 입력문구(웹사이트 회원가입)
    @Insert("INSERT INTO member (member_id, userid, password, email, nickname) " +
            "VALUES(#{member_id},#{userid},#{password}, #{email}, #{nickname})")
    public int setMember(int member_id, String userid, String password, String email, String nickname);
}