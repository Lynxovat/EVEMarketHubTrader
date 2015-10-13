package pw.redalliance.MarketTree.Updater;

/**
 * Created by Lynx on 13.10.2015.
 */
public class MarketTypeDBData {
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    private int typeId;
    private int metaLevel;
    private String metaGroup;
}
