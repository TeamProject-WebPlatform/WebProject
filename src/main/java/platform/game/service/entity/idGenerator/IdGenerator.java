package platform.game.service.entity.idGenerator;

import org.apache.ibatis.jdbc.SQL;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class IdGenerator implements IdentifierGenerator {
    public static final String MEM_ID_GENERATOR_PARAM_KEY = "realm";

    private String realm;

    // 속성값 처리 메소드 override
    @Override
    public void configure(org.hibernate.type.Type type, Properties params, ServiceRegistry serviceRegistry)
            throws MappingException {
        this.realm = ConfigurationHelper.getString(MEM_ID_GENERATOR_PARAM_KEY, params);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Connection conn = null;
        Long newId = 0l;
        try {
            conn = session.getJdbcConnectionAccess().obtainConnection();
        } catch (SQLException e) {
        }

        switch (realm) {
            case "MEMBER":
                newId = getMemberId(conn);
                break;
            case "BOARD":

                break;
        }

        try{if(conn!=null) {conn.close();}}catch(Exception e){};
        if (newId != -1l)
            return newId;
        else
            return null;
    }

    private Long getMemberId(Connection conn) {
        try {
            Statement statement = conn.createStatement();
            int startNum = 1;
            switch (UserSignupState.SIGNUP_TYPE) {
                case "KAKAO":
                    startNum = 2;
                    break;
                case "STEAM":
                    startNum = 3;
                    break;
                case "RIOT":
                    startNum = 4;
                    break;
                default:
                    startNum = 1;
                    break;
            }
            String sql = "select MAX(mem_id) AS maxId from MEMBER where mem_id like '" + startNum + "%'";
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                Long fullId = rs.getLong("maxId");
                Long id = Long.parseLong(String.valueOf(fullId).substring(3));
                id += 1;
                return Long.parseLong(startNum + "00" + id);
            } else {
                return (long) startNum * 1000 + 1;
            }
        } catch (SQLException e) {
            return -1l;
        }
    }
}
