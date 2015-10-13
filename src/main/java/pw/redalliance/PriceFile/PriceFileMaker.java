package pw.redalliance.PriceFile;

import com.tree.TreeNode;
import pw.redalliance.MarketAPI.MarketAPI;
import pw.redalliance.MarketItem;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketType;

import java.util.*;


/**
 * Created by Lynx on 22.07.2015.
 */

public class PriceFileMaker {
    private static final int jitaId = 30000142;
    private static final String filename = "output.txt";

    private static final Set<String> NPC_TRADED;
    static {
        Set<String> npcTraded = new HashSet<>();
        npcTraded.add("Blueprint");
        npcTraded.add("Skill");
        NPC_TRADED = Collections.unmodifiableSet(npcTraded);
    }

    private PriceFileWriter writer;
    private MarketAPI api;
    private BlueprintCalculator bpCalc = new BlueprintCalculator();

    public void makePriceFile(Collection<MarketType> types, MarketAPI api) {
        writer = new PriceFileWriter(filename);
        this.api = api;
        processTypes(types);
        writer.close();
    }

    private void processTypes(Collection<MarketType> types) {
        System.out.println("Requesting market data from EVE-CENTRAL API:");
        final int size = types.size();
        int count = 0;
        int threshold = 500;
        for (MarketType type : types) {
            if (NPCTraded(type)) {
                processNPCTradedType(type);
            } else {
                processMarketType(type);
            }
            if ((++count % threshold) == 0) {
                System.out.println("API: " + count + " types of " + size + " processed");
            }
        }
    }

    private boolean NPCTraded(MarketType type) {
        return NPC_TRADED.contains(type.getCategory());
    }

    private void processNPCTradedType(MarketType type) {
        writer.printLine(type.getBasePrice(), type.getVolume(), type.getName());
    }

    private void processMarketType(MarketType type) {
        MarketItem item = new MarketItem(type);
        item.marketData = api.getData(type.getTypeId(), jitaId);
        printItem(item);
    }

    private void printItem(MarketItem item) {
        double price = item.marketData.getMaxBye();
        checkPrice(item.type, price, false);
        writer.printLine(price, item.type.getVolume(), item.type.getName());

        if (bpCalc.calculateBlueprint(item)) {
            price = (bpCalc.getPrice());
            checkPrice(item.type, price, true);
            writer.printLine(price, bpCalc.getVolume(), bpCalc.getName());
        }
    }

    private void checkPrice(MarketType type, double price, boolean blueprint) {
        if (price < 10e+9) return;
        if ((!blueprint && type.getCategory().equals("Ship")) || (blueprint && type.getMetaGroup().equals("Tech II")) || type.getMetaGroup().equals("Officer")) return;
        printWarning(type, price, blueprint);
    }

    private void printWarning(MarketType type, double price, boolean bpc) {
        System.out.println("HIGH PRICE WARNING: " + type.getName() + (bpc ? " (BPC)" : "") + " price is " + price);
    }
}
