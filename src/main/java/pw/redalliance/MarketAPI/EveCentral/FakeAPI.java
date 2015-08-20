package pw.redalliance.MarketAPI.EveCentral;

import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketAPI.MarketAPI;

/**
 * Created by Lynx on 28.07.2015.
 */
public class FakeAPI implements MarketAPI {
    @Override
    public ItemMarketData getData(int typeID, int systemID) {
        return new ItemMarketData(1000000.00, 500000.00, 10.0);
    }
}
