package pw.redalliance;

import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketTree.MarketType;

/**
 * Created by Lynx on 22.07.2015.
 */
public class MarketItem {
    public final MarketType type;
    public ItemMarketData marketData;

    public MarketItem(MarketType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MarketItem{" +
                "type=" + type +
                ", marketData=" + marketData +
                '}';
    }
}
