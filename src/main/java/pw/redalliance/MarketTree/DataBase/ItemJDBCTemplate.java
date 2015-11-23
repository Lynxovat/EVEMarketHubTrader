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
    public void insertMarketItems(List<MarketItem> items) {
        String sql = "INSERT INTO items (typeId,name,category,marketGroupId,metaLevel,metaGroup,volume,basePrice,iconId,maxBye,minSell,marketVolume,selected) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplateObject.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MarketItem item = items.get(i);
                ps.setInt(1, item.type.getTypeId());
                ps.setString(2, item.type.getName());
                ps.setString(3, item.type.getCategory());
                ps.setInt(4, item.type.getMarketGroupId());
                ps.setInt(5, item.type.getMetaLevel());
                ps.setString(6, item.type.getMetaGroup());
                ps.setDouble(7, item.type.getVolume());
                ps.setDouble(8, item.type.getBasePrice());
                ps.setInt(9, item.type.getIconId());

                ps.setDouble(10, item.marketData.getMaxBye());
                ps.setDouble(11, item.marketData.getMinSell());
                ps.setDouble(12, item.marketData.getVolume());

                ps.setBoolean(13, item.isSelected());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }
}
