package pw.redalliance.MarketTree.DataBase;

import pw.redalliance.MarketItem;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Lynx on 21.07.2015.
 */
public interface ItemDAO {
    void setDataSource(DataSource ds);
    List<MarketItem> listSelectedItems();
    void insertMarketItems(List<MarketItem> items);
}
