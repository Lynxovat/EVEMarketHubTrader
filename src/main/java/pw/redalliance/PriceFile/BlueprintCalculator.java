package pw.redalliance.PriceFile;

import pw.redalliance.MarketItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lynx on 11.08.2015.
 */
public class BlueprintCalculator {
    private static final double BLUEPRINT_VOLUME = 0.01;
    private static final String BLUEPRINT_STRING = "Blueprint";
    private static final String BPC_APPENDER = " " + BLUEPRINT_STRING + " (Copy)";
    private static final String BPO_APPENDER = " " + BLUEPRINT_STRING + " (Original)";
    private static final String T2_STRING = "Tech II";
    private static final double T2_BPO_DEF_PRICE = 10000000000.0;
    private static final String FACTION_STRING = "Faction";

    private static final Map<String, Integer> RUNS_MAP;
    static {
        Map<String, Integer> runsMap = new HashMap<String, Integer>();
        runsMap.put("Ship", 1);
        runsMap.put("Module", 5);
        runsMap.put("Charge", 0);
        runsMap.put("Drone", 10);
        runsMap.put("Starbase", 1);
        RUNS_MAP = Collections.unmodifiableMap(runsMap);
    }

    public double getVolume() {
        return volume;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    private double volume = BLUEPRINT_VOLUME;
    private double price;
    private String name;

    //returns true if blueprint calculated
    public boolean calculateBlueprint(MarketItem item) {
        if (item.type.getMetaGroup() == null) return false;
        switch (item.type.getMetaGroup()) {
            case T2_STRING:
                price = T2_BPO_DEF_PRICE;
                name = item.type.getName() + BPO_APPENDER;
                break;
            case FACTION_STRING:
                double runPrice = item.marketData.getMaxBye() - item.type.getBasePrice();
                Integer multiplier = RUNS_MAP.get(item.type.getCategory());
                if (multiplier == null) {
                    System.out.println("Unknown category: " + item.type.getCategory());
                    return false;
                } else if (multiplier == 0) {
                    return false;
                } else {
                    price = runPrice * multiplier;
                    name = item.type.getName() + BPC_APPENDER;
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
