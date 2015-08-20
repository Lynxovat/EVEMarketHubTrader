package pw.redalliance.MarketTree.Updater;

import org.springframework.jdbc.core.RowMapper;
import pw.redalliance.MarketTree.MarketGroup;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Lynx on 22.07.2015.
 */
public class MarketGroupMapper implements RowMapper<MarketGroup> {
    @Override
    public MarketGroup mapRow(ResultSet resultSet, int i) throws SQLException {
        MarketGroup group = new MarketGroup(resultSet.getString("marketGroupName"));
        group.setId(resultSet.getInt("marketGroupId"));
        group.setHasTypes(resultSet.getBoolean("hasTypes"));
        return group;
    }
}
