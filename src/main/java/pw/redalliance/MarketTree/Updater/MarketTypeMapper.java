package pw.redalliance.MarketTree.Updater;

import pw.redalliance.MarketTree.MarketType;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


/**
 * Created by Lynx on 21.07.2015.
 */
public class MarketTypeMapper implements RowMapper<MarketType> {
    @Override
    public MarketType mapRow(ResultSet resultSet, int i) throws SQLException {
        MarketType item = new MarketType();
        item.setTypeId(resultSet.getInt("typeID"));
        item.setName(resultSet.getString("typeName"));
        item.setCategory(resultSet.getString("categoryName"));
        item.setMetaLevel(resultSet.getInt("metaLevel"));
        item.setMetaGroup(resultSet.getString("metaGroupName"));
        item.setVolume(resultSet.getDouble("volume"));
        item.setBasePrice(resultSet.getBigDecimal("basePrice").doubleValue());
        return fix(item);
    }

    private MarketType fix(MarketType type) {
        if (type.getMetaGroup() == null) {
            int metaLevel = type.getMetaLevel();
            if (metaLevel < 0) {
                System.out.println("Unknown metaGroup: " + type.getMetaLevel() + " " + type.getName());
                return type;
            } else if (metaLevel < 5) {
                type.setMetaGroup("Tech I");
            } else if (metaLevel < 6) {
                type.setMetaGroup("Tech II");
            } else if (metaLevel < 10) {
                type.setMetaGroup("Faction");
            } else if (metaLevel < 20) {
                type.setMetaGroup("Deadspace");
            } else {
                System.out.println("Unknown metaGroup: " + type.getMetaLevel() + " " + type.getName());
            }
            if (type.getMetaGroup() != "Tech I") {
                System.out.println("FIXED ITEM: [" + type.getMetaGroup() + "] " + type.getName());
            }
        }
        return type;
    }
}