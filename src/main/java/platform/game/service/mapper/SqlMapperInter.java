package platform.game.service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.ComponentScan;

@Mapper
@ComponentScan(basePackages = {"platform.game.model"})
public interface SqlMapperInter {
    

    //사용자 아이디 암호화된 비밀번호 불러오기
    @Select("select password from member where userid=#{userid}")
    public String searchMember(String userid);

    //로그인 쿼리문
    @Select("select * from member where userid = #{userid} and password=#{password}")
    public int getMemberTObyIDandPass(String userid, String password);

    //카카오로그인 확인 쿼리문
    @Select("select count(*) from member where email = #{email}")
    public int checkKakaoMamber(String email);
    //스팀로그인 확인 쿼리문
    @Select("select count(*) from member where userid = #{userid}")
    public int checkSteamMamber(String userid);

    //회원가입DB 입력때 사용할 member_id 받아오는 sql문
    @Select("select last_member_id from member_id_guide where signin_type = #{signin_type}")
    public int getLastMemberId(String signin_type);
    
    //회원가입 완료가 되면 회원번호 디비에 마지막 회원번호 저장하기
    @Insert("update member_id_guide set last_member_id = #{last_member_id} where signin_type = #{signin_type}")
    public int setMemberId(int last_member_id, String signin_type);

    // MemberTO -> UserInfo Copy
    @Insert("INSERT INTO userinfo (name, password, roles) SELECT userid, password, roles FROM member;")
    public int setUserInfo();

    // 회원가입DB 입력문구(웹사이트 회원가입)
    @Insert("INSERT INTO member (member_id, userid, password, email, nickname) " +
            "VALUES(#{member_id},#{userid},#{password}, #{email}, #{nickname})")
    public int setMember(int member_id, String userid, String password, String email, String nickname);

    // 회원가입DB 입력문구(카카오 회원가입)
    @Insert("INSERT INTO member (member_id, userid, password, email, nickname) " +
            "VALUES(#{member_id},#{userid},#{password}, #{email}, #{nickname})")
    public int setKakaoMember(int member_id, String userid, String password, String email, String nickname);

    // 회원가입DB 입력문구(스팀 회원가입)
    @Insert("INSERT INTO member (member_id, userid, password, email, nickname) " +
            "VALUES(#{member_id},#{userid},#{password}, #{email}, #{nickname})")
    public int setSteamMember(int member_id, String userid, String password, String email, String nickname);




    // 언젠간 사용할 회원수 불러오는 쿼리문
    
    // 카카오 회원수 가져오기
    @Select("select count(*) as count from member where member_id between 100000 and 199999")
    public int getKakaoMemberCount();

    // 스팀 회원수 가져오기
    @Select("select count(*) as count from member where member_id between 200000 and 299999")
    public int getSteamMemberCount();
}