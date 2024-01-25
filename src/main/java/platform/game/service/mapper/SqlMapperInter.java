package platform.game.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.ComponentScan;

import platform.game.service.model.TO.LevelRankTO;
import platform.game.service.model.TO.PointRankTO;
import platform.game.service.model.TO.RollingRankTO;
import platform.game.service.model.TO.SwiperProfileTO;
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

        @Insert("insert into ranking (rank,rank_code,mem_id, rank_update,game_cd) select rank() over(order by mem_lvl desc, mem_id desc) as rank, 'Level', mem_id, now(), 0 from member limit 50")
        int setLevelRank();

        @Insert("insert into ranking (rank,rank_code,mem_id, rank_update,game_cd) select rank() over(order by round((mem_game_win_cnt/mem_total_game_cnt)*100,2) desc, mem_id desc) as rank, 'WinRate', mem_id, now(),0 from member where mem_total_game_cnt >0 limit 50")
        int setWinRateRank();

        @Insert("insert into ranking (rank,rank_code,mem_id, rank_update,game_cd) select rank() over(order by mem_total_point desc, mem_id desc) as rank, 'Point', mem_id, now(),0 from member limit 50")
        int setTotalPointRank();

        @Insert("insert into ranking(rank,rank_code,mem_id,rank_update,game_cd) select rank() over (order by m.mem_lvl desc, m.mem_id desc) as rank,'Level', m.mem_id, now(), #{game_cd} from member m join member_favorite_game f on m.mem_id=f.mem_id where f.game_cd = #{game_cd} order by m.mem_lvl desc,m.mem_id desc limit 50;")
        int setOtherLevelRank(String game_cd);

        @Insert("insert into ranking(rank,rank_code,mem_id,rank_update,game_cd) select rank() over (order by m.mem_total_point desc, m.mem_id desc) as rank,'Point', m.mem_id, now(), #{game_cd} from member m join member_favorite_game f on m.mem_id=f.mem_id where f.game_cd = #{game_cd} order by m.mem_total_point desc,m.mem_id desc limit 50;")
        int setOtherPointRank(String game_cd);

        @Insert("insert into ranking(rank,rank_code,mem_id,rank_update,game_cd) select rank() over (order by round((m.mem_game_win_cnt/m.mem_total_game_cnt)*100,2) desc, m.mem_id desc) as rank,'WinRate', m.mem_id, now(), #{game_cd} from member m join member_favorite_game f on m.mem_id=f.mem_id where f.game_cd = #{game_cd} order by round((m.mem_game_win_cnt/m.mem_total_game_cnt)*100,2) desc,m.mem_id desc limit 50;")
        int setOtherWinRateRank(String game_cd);

        // 구분 없이 레벨 랭킹
        @Select("select r.rank, m.mem_nick, m.mem_lvl from ranking r join member m on r.rank_code='Level' and m.mem_id=r.mem_id and r.game_cd=0 order by r.rank")
        public List<LevelRankTO> getLevelRank();

        // 구분 없이 상위 10명만
        @Select("SELECT r.rank, m.mem_nick, m.mem_lvl FROM ranking r JOIN member m ON r.rank_code='Level' AND m.mem_id=r.mem_id AND r.game_cd=0 ORDER BY r.rank ASC LIMIT 10")
        public List<LevelRankTO> getTop10LevelRanks();

        // 구분 없이 승률 랭킹
        @Select("SELECT r.rank, m.mem_nick, m.mem_lvl, CASE WHEN m.mem_total_game_cnt > 0 THEN round((m.mem_game_win_cnt / m.mem_total_game_cnt) * 100, 2) ELSE 0 END AS winrate FROM ranking r JOIN member m ON r.rank_code = 'WinRate' AND m.mem_id = r.mem_id AND r.game_cd = 0 ORDER BY r.rank;")
        public List<WinRankTO> getWinRateRank();

        // 구분 없이 포인트 랭킹
        @Select("select r.rank, m.mem_nick, m.mem_lvl, m.mem_total_point from ranking r join member m on r.rank_code='Point' and m.mem_id=r.mem_id and r.game_cd=0 order by r.rank")
        public List<PointRankTO> getPointRank();

        // 게임 종류 별 레벨 랭킹
        @Select("select r.rank, m.mem_nick, m.mem_lvl from ranking r join member m on r.mem_id = m.mem_id and r.game_cd=#{game_cd} and r.rank_code='level' order by m.mem_lvl desc;")
        public List<LevelRankTO> getOtherLevelRank(String game_cd);

        // 게임 종류 별 포인트 랭킹
        @Select("select r.rank, m.mem_nick, m.mem_lvl, m.mem_total_point from ranking r join member m on r.mem_id = m.mem_id and r.game_cd=#{game_cd} and r.rank_code='point' order by m.mem_total_point desc;")
        public List<PointRankTO> getOtherPointRank(String game_cd);

        // 게임 종류 별 승률 랭킹
        @Select("select r.rank, m.mem_nick, m.mem_lvl, round((m.mem_game_win_cnt/m.mem_total_game_cnt)*100,2) as winrate from ranking r join member m on r.mem_id = m.mem_id and r.game_cd=#{game_cd} and r.rank_code='winrate' order by winrate desc;")
        public List<WinRankTO> getOtherWinRateRank(String game_cd);

        // 비로그인 대상 swiper
        @Select("select distinct m.mem_id, m.mem_nick, m.mem_lvl from ranking r join member m on r.mem_id = m.mem_id where r.rank<16 order by rand() limit 16")
        public List<RollingRankTO> getRol();

        // 로그인 대상 선호 게임 swiper
        @Select("select distinct m.mem_id, m.mem_nick, m.mem_lvl from ranking r join member m on r.mem_id = m.mem_id where r.rank<16 and r.game_cd=#{game_cd} order by rand() limit 4")
        public List<RollingRankTO> getOtherRol(String game_cd);

        // 회원 가입 시 멤버 레벨 랭킹 셋팅
        @Insert("insert into member_ranking(rank_code, mem_id, mem_rank) select 'Level', mem_id, RANK() OVER(order by mem_lvl desc, mem_created_at desc) as mem_rank from member")
        public int setMemberLevelRanking();

        // 회원 가입 시 멤버 포인트 랭킹 셋팅
        @Insert("insert into member_ranking(rank_code, mem_id, mem_rank) select 'Point', mem_id, RANK() OVER(order by mem_total_point desc, mem_created_at desc) as mem_rank from member")
        public int setMemberPointRanking();

        // 회원 가입 시 멤버 승률 랭킹 셋팅
        @Insert("insert into member_ranking(rank_code, mem_id, mem_rank) select 'WinRate', mem_id, RANK() OVER (order by round((mem_game_win_cnt/mem_total_game_cnt)*100,2) desc, mem_created_at desc) as mem_rank from member where mem_total_game_cnt>0")
        public int setMemberWinRateRanking();

        @Insert("INSERT INTO member_game_match_record values(#{game_cd},#{mem_id}, default, default, default")
        public int setMatchRecord(String game_cd, long mem_id);

        // 스와이퍼 데이터로 프로필 불러오기
        @Select("SELECT m.mem_id, m.mem_nick, m.mem_lvl, mp.profile_intro, mp.profile_image, mp.profile_header, mp.profile_card, mp.profile_rep_badge, mp.profile_badge_list FROM member m JOIN member_profile mp ON m.mem_id = mp.mem_id WHERE m.mem_id = #{mem_id};")
        public SwiperProfileTO SwiperProfile(long mem_id);

}