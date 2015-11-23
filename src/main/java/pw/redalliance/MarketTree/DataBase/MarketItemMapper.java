package pw.redalliance.MarketTree.DataBase;

import org.springframework.jdbc.core.RowMapper;
import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketItem;
import pw.redalliance.MarketTree.MarketType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Lynx on 22.07.2015.
 */
public class MarketItemMapper implements RowMapper<MarketItem> {
    @Override
    public MarketItem mapRow(ResultSet resultSet, int i) throws SQLException {
    	MarketItem item = new MarketItem(mapType(resultSet));
    	item.marketData = mapMarketData(resultSet);
    	item.setSelected(resultSet.getBoolean("selected"));
    	return item;        
    }

    private MarketType mapType(ResultSet rs) throws SQLException {
    	MarketType mt = new MarketType();
    	mt.setTypeId(rs.getInt("typeId"));
    	mt.setName(rs.getString("name"));
    	mt.setCategory(rs.getString("category"));
    	mt.setMarketGroupId(rs.getInt("marketGroupId"));
    	mt.setMetaLevel(rs.getInt("metaLevel"));
    	mt.setMetaGroup(rs.getString("metaGroup"));
    	mt.setVolume(rs.getDouble("volume"));
    	mt.setBasePrice(rs.getDouble("basePrice"));
    	mt.setIconId(rs.getInt("iconId"));
    	return mt;
    }

    private ItemMarketData mapMarketData(ResultSet rs) throws SQLException {
    	ItemMarketData md = new ItemMarketData(
    		rs.getDouble("maxBye"),
    		rs.getDouble("minSell"),
    		rs.getDouble("marketVolume")
    	);
    	return md;
    }

}    