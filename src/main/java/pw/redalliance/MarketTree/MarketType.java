package pw.redalliance.MarketTree;

import pw.redalliance.MarketTree.Updater.MarketTypeDBData;

import java.io.Serializable;

/**
 * Created by Lynx on 20.07.2015.
 */
public class MarketType implements Serializable {
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMetaLevel() {
        return metaLevel;
    }

    public void setMetaLevel(int metaLevel) {
        this.metaLevel = metaLevel;
    }

    public String getMetaGroup() {
        return metaGroup;
    }

    public void setMetaGroup(String metaGroup) {
        this.metaGroup = metaGroup;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getMarketGroupId() {
        return marketGroupId;
    }

    public void setMarketGroupId(int marketGroupId) {
        this.marketGroupId = marketGroupId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    private int typeId;
    private String name;
    private String category;
    private int marketGroupId;
    private int metaLevel;
    private String metaGroup;
    private double volume;
    private double basePrice;
    private int iconId;

    public void setDBData(MarketTypeDBData data) {
        setMetaLevel(data.getMetaLevel());
        setMetaGroup(data.getMetaGroup());
    }

    @Override
    public String toString() {
        return name + "(" + typeId + ")";
    }
}