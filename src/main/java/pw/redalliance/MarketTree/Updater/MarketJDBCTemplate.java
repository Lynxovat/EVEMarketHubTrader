package pw.redalliance.MarketTree.Updater;

import org.springframework.jdbc.core.JdbcTemplate;
import pw.redalliance.MarketTree.MarketGroup;

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
    public MarketTypeDBData getMarketTypeData(int typeId) {
        String sql = "SELECT mt.typeID, ISNULL(dta.valueFloat, dta.valueInt) AS metaLevel, mg.metaGroupName " +
                "FROM dbo.invMetaTypes AS mt " +
                "LEFT JOIN dbo.invMetaGroups AS mg ON mt.metaGroupID = mg.metaGroupID " +
                "LEFT JOIN dbo.dgmTypeAttributes AS dta ON mt.typeID = dta.typeID AND dta.attributeID = 633 " +
                "WHERE mt.typeID = ?";

        List<MarketTypeDBData> dataLst = jdbcTemplateObject.query(sql, new Object[]{typeId}, new MarketTypeDBDataMapper());
        if (dataLst.isEmpty()) {
            MarketTypeDBData data = new MarketTypeDBData();
            data.setTypeId(typeId);
            data.setMetaLevel(0);
            data.setMetaGroup("Tech I");
            return data;
        } else if (dataLst.size() == 1) {
            return dataLst.get(0);
        } else {
            System.out.println(typeId + " has multiple (" + dataLst.size() + ") results in getMarketTypeData sql request");
            return dataLst.get(0);
        }
    }
}
