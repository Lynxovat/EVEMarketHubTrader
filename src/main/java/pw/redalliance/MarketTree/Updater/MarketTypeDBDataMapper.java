package pw.redalliance.MarketTree.Updater;

import org.springframework.jdbc.core.RowMapper;
import pw.redalliance.MarketTree.MarketType;
import pw.redalliance.MarketTree.Updater.MarketTypeDBData;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Lynx on 13.10.2015.
 */
public class MarketTypeDBDataMapper implements RowMapper<MarketTypeDBData> {
    @Override
    public MarketTypeDBData mapRow(ResultSet resultSet, int i) throws SQLException {
        MarketTypeDBData data = new MarketTypeDBData();
        data.setTypeId(resultSet.getInt("typeID"));
        data.setMetaLevel(resultSet.getInt("metaLevel"));
        data.setMetaGroup(resultSet.getString("metaGroupName"));
        return fix(data);
    }

    private MarketTypeDBData fix(MarketTypeDBData data) {
        if (data.getMetaGroup() == null) {
            int metaLevel = data.getMetaLevel();
            if (metaLevel < 0) {
                System.out.println("Unknown metaGroup: " + data.getMetaLevel() + " " + data.getTypeId());
                return data;
            } else if (metaLevel < 5) {
                data.setMetaGroup("Tech I");
            } else if (metaLevel < 6) {
                data.setMetaGroup("Tech II");
            } else if (metaLevel < 10) {
                data.setMetaGroup("Faction");
            } else if (metaLevel < 20) {
                data.setMetaGroup("Deadspace");
            } else {
                System.out.println("Unknown metaGroup: " + data.getMetaLevel() + " " + data.getTypeId());
            }
            if (!data.getMetaGroup().equals("Tech I")) {
                System.out.println("FIXED ITEM: [" + data.getMetaGroup() + "] " + data.getTypeId());
            }
        }
        return data;
    }
}