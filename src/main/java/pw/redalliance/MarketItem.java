package pw.redalliance;

import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketTree.MarketType;

/**
 * Created by Lynx on 22.07.2015.
 */
public class MarketItem {
    public final MarketType type;
    public ItemMarketData marketData;    

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected = false;    

    public MarketItem(MarketType type) {
        this.type = type;
    }

    public MarketType getType() { return type; }

    public ItemMarketData getMarketData() { return marketData; }
    public void setMarketData(ItemMarketData data) { this.marketData = data; }

    @Override
    public String toString() {
        return "[" + (selected ? "X" : " ") + "] MarketItem{" +
                "type=" + type +
                ", marketData=" + marketData +
                '}';
    }
}
