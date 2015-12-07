package pw.redalliance.MarketTree.DataBase;

import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketItem;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Lynx on 21.07.2015.
 */
public interface ItemDAO {
    void setDataSource(DataSource ds);
    List<MarketItem> listMarketGroupItems(int id);
    List<MarketItem> listSelectedItems();
    List<MarketItem> listItems();
    void insertMarketItems(List<MarketItem> items);
    void updateItemSelected(MarketItem item);
    void updateItemMarketData(MarketItem item);
    void updateItemsMarketData(List<MarketItem> items);
    void truncate();
}
