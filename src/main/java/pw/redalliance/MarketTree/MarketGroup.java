package pw.redalliance.MarketTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lynx on 21.07.2015.
 */
public class MarketGroup implements Serializable {
    private String name;
    private int id;
    private boolean hasTypes;
    private List<MarketType> types = new ArrayList<>();

    public MarketGroup(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasTypes() {
        return hasTypes;
    }

    public void setHasTypes(boolean hasTypes) {
        this.hasTypes = hasTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MarketType> getTypes() {
        return types;
    }

    public void setTypes(List<MarketType> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return name + " " + types.toString();
    }
}
