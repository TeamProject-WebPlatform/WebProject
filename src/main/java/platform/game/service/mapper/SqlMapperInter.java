package platform.game.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.ComponentScan;

import platform.game.service.model.TO.AttendRankTO;
import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.WinRankTO;

@Mapper
@ComponentScan(basePackages = { "platform.game.model" })
public interface SqlMapperInter {

        // 사용자 아이디 암호화된 비밀번호 불러오기
        @Select("select password from member where userid=#{userid}")
        public String searchMember(String userid);

        // 로그인 쿼리문
        @Select("select * from member where userid = #{userid} and password=#{password}")
        public int getMemberTObyIDandPass(String userid, String password);

        // 카카오로그인 확인 쿼리문
        @Select("select count(*) from member where email = #{email}")
        public int checkKakaoMamber(String email);

        // 스팀로그인 확인 쿼리문
        @Select("select count(*) from member where userid = #{userid}")
        public int checkSteamMamber(String userid);

        // 회원가입DB 입력때 사용할 member_id 받아오는 sql문
        @Select("select last_member_id from member_id_guide where signin_type = #{signin_type}")
        public int getLastMemberId(String signin_type);

        // 회원가입 완료가 되면 회원번호 디비에 마지막 회원번호 저장하기
        @Insert("update member_id_guide set last_member_id = #{last_member_id} where signin_type = #{signin_type}")
        public int setMemberId(int last_member_id, String signin_type);

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

        // 차트 생성 및 랭킹 테스트용 DB 추가
        @Insert("INSERT INTO member VALUES(#{mem_id}, #{mem_userid}, #{mem_pw}, #{mem_nick}, #{mem_role_cd}, #{mem_email}, default,default,default,#{mem_total_point}, #{mem_lvl}, #{mem_attend}, #{mem_game_count}, #{mem_win_count}, #{mem_lose_count}, default, default, default, default,default, now(), default )")
        public int setTestMember(String mem_id, String mem_userid, String mem_pw, String mem_nick, String mem_role_cd,
                        String mem_email, int mem_total_point, int mem_lvl, int mem_attend, int mem_game_count,
                        int mem_win_count, int mem_lose_count);

        @Insert("INSERT INTO ranklist (rank, rank_code, mem_id, rank_update) " +
                        "SELECT #{rank}, 0, mem_id, NOW() " +
                        "FROM member " +
                        "ORDER BY mem_lvl DESC " +
                        "LIMIT #{i}, 1")
        int setLevelRank(int rank, int i);

        @Select("select mem_userid, mem_attend, mem_total_point, mem_lvl from member order by mem_attend desc limit 30")
        public int getAttendRank();

        @Insert("INSERT INTO ranklist (rank, rank_code, mem_id, rank_update) " +
                        "SELECT #{rank}, 1, mem_id, NOW() " +
                        "FROM member " +
                        "ORDER BY mem_attend DESC " +
                        "LIMIT #{i}, 1")
        int setAttendRank(int rank, int i);

        @Select("select mem_userid, mem_win_count, mem_total_point, mem_lvl from member order by mem_win_count desc limit 30")
        public int getWinRateRank();

        @Insert("INSERT INTO ranklist (rank, rank_code, mem_id, rank_update) " +
                        "SELECT #{rank}, 2, mem_id, NOW() " +
                        "FROM member " +
                        "ORDER BY mem_win_count DESC " +
                        "LIMIT #{i}, 1")
        int setWinrateRank(int rank, int i);

        @Insert("INSERT INTO ranklist (rank, rank_code, mem_id, rank_update) " +
                        "SELECT #{rank}, 3, mem_id, NOW() " +
                        "FROM member " +
                        "ORDER BY mem_total_point DESC " +
                        "LIMIT #{i}, 1")
        int setTotalPointRank(int rank, int i);

        @Select("select r.rank, m.mem_userid, m.mem_lvl from ranklist r join member m on r.rank_code=0 and m.mem_id=r.mem_id")
        public List<LevelRankTO> getLevelrank();

        @Select("select r.rank, m.mem_userid, m.mem_attend from ranklist r join member m on r.rank_code=1 and m.mem_id=r.mem_id")
        public List<AttendRankTO> getAttendrank();

        @Select("select r.rank, m.mem_userid, (m.mem_win_count/m.mem_game_count)*100 as winrate from ranklist r join member m on r.rank_code=2 and m.mem_id=r.mem_id")
        public List<WinRankTO> getWinrank();

        @Select("select r.rank, m.mem_userid, (m.mem_win_count/m.mem_game_count)*100 as winrate from ranklist r join member m on r.rank_code=2 and m.mem_id=r.mem_id")
        public List<PointRankTO> getPointrank();
}