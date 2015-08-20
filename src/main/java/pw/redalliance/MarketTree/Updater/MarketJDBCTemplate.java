package pw.redalliance.MarketTree.Updater;

import org.springframework.jdbc.core.JdbcTemplate;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketType;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Lynx on 21.07.2015.
 */
public class MarketJDBCTemplate implements MarketDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MarketGroup> listMarketGroup(int marketGroupId) {
        String sql = "SELECT marketGroupID, marketGroupName, hasTypes " +
        "FROM dbo.invMarketGroups " +
        "WHERE parentGroupID" + (marketGroupId == 0 ? " IS NULL" : ("=" + marketGroupId)) + " " +
        "ORDER BY hasTypes, marketGroupName";

        List<MarketGroup> groups = jdbcTemplateObject.query(sql, new MarketGroupMapper());
        return groups;
    }

    @Override
    public List<MarketType> listMarketTypes(int marketGroupId) {
        String sql = "SELECT t.typeID, t.typeName, categoryName, ISNULL(dta.valueFloat, dta.valueInt) AS metaLevel, mg.metaGroupName, t.volume, t.basePrice " +
                "FROM dbo.invTypes AS t " +
                "LEFT JOIN dbo.invGroups AS ig ON t.groupID=ig.groupID " +
                "LEFT JOIN dbo.invCategories AS ic ON ig.categoryID=ic.categoryID " +
                "LEFT JOIN dbo.invMetaTypes AS mt ON t.typeID=mt.typeID " +
                "LEFT JOIN dbo.invMetaGroups AS mg ON mt.metaGroupID=mg.metaGroupID " +
                "LEFT JOIN dbo.dgmTypeAttributes AS dta ON t.typeID=dta.typeID AND dta.attributeID=633 " +
                "WHERE t.marketGroupID=" + marketGroupId + " AND t.published=1 " +
//                "ORDER BY dta.valueFloat, dta.valueInt, t.typeName";
                "ORDER BY metaLevel, t.typeName";

        List<MarketType> types = jdbcTemplateObject.query(sql, new MarketTypeMapper());
        return types;
    }


}
