package pw.redalliance.MarketTree.Updater;

import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketType;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Lynx on 21.07.2015.
 */
public interface MarketDAO {
    public void setDataSource(DataSource ds);
    public List<MarketGroup> listMarketGroup(int marketGroupId);
    public MarketTypeDBData getMarketTypeData(int typeId);
}
