package pw.redalliance.MarketTree.DataBase;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import pw.redalliance.MarketItem;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Lynx on 21.07.2015.
 */
public class ItemJDBCTemplate implements ItemDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;
    private static final String tableName = "items";

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MarketItem> listSelectedItems() {
        String sql = "SELECT * FROM items WHERE selected = 1";
        List<MarketItem> items = jdbcTemplateObject.query(sql, new MarketItemMapper());
        return items;
    }

    @Override
    public List<MarketItem> listMarketGroupItems(int id) {
        String sql = "SELECT * FROM items WHERE marketGroupId = " + id;
        List<MarketItem> items = jdbcTemplateObject.query(sql, new MarketItemMapper());
        return items;
    }

    @Override
    public List<MarketItem> listItems() {
        String sql = "SELECT * FROM items";
        List<MarketItem> items = jdbcTemplateObject.query(sql, new MarketItemMapper());
        return items;
    }

    @Override
    public void insertMarketItems(List<MarketItem> items) {
        String sql = "INSERT INTO items (typeId,name,itemGroup,category,marketGroupId,metaLevel,metaGroup,volume,basePrice,iconId,maxBye,minSell,marketVolume,selected) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplateObject.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MarketItem item = items.get(i);
                ps.setInt(1, item.type.getTypeId());
                ps.setString(2, item.type.getName());
                ps.setString(3, item.type.getGroup());
                ps.setString(4, item.type.getCategory());
                ps.setInt(5, item.type.getMarketGroupId());
                ps.setInt(6, item.type.getMetaLevel());
                ps.setString(7, item.type.getMetaGroup());
                ps.setDouble(8, item.type.getVolume());
                ps.setDouble(9, item.type.getBasePrice());
                ps.setInt(10, item.type.getIconId());

                ps.setDouble(11, item.marketData.getMaxBye());
                ps.setDouble(12, item.marketData.getMinSell());
                ps.setDouble(13, item.marketData.getVolume());

                ps.setBoolean(14, item.isSelected());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }

    @Override
    public void updateItemSelected(MarketItem item) {
        String sql = "UPDATE items SET selected = ? WHERE typeId = ?";
        jdbcTemplateObject.update(sql, item.isSelected(), item.type.getTypeId());
    }

    @Override
    public void updateItemMarketData(MarketItem item) {
        String sql = "UPDATE items SET maxBye = ?, minSell = ?, marketVolume = ? WHERE typeId = ?";
        jdbcTemplateObject.update(sql, item.marketData.getMaxBye(), item.marketData.getMinSell(), item.marketData.getVolume(), item.type.getTypeId());
    }

    @Override
    public void updateItemsMarketData(List<MarketItem> items) {
        String sql = "UPDATE items SET maxBye = ?, minSell = ?, marketVolume = ? WHERE typeId = ?";
        jdbcTemplateObject.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MarketItem item = items.get(i);
                ps.setDouble(1, item.marketData.getMaxBye());
                ps.setDouble(2, item.marketData.getMinSell());
                ps.setDouble(3, item.marketData.getVolume());
                ps.setInt(4, item.type.getTypeId());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }

    @Override
    public void truncate() {
        jdbcTemplateObject.execute("TRUNCATE TABLE items");
    }
}
