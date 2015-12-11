package pw.redalliance.PriceFile;

import pw.redalliance.MarketItem;
import pw.redalliance.MarketTree.MarketType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Lynx on 22.07.2015.
 */

public class PriceFileMaker {
    private static final String filename = "output.txt";

    private static final Set<String> NPC_TRADED;
    private static final Set<String> EXPENSIVE_GROUPS;
    static {
        Set<String> npcTraded = new HashSet<>();
        npcTraded.add("Blueprint");
        npcTraded.add("Skill");
        NPC_TRADED = Collections.unmodifiableSet(npcTraded);

        Set<String> gr = new HashSet<>();
        gr.add("Titan");
        gr.add("Supercarrier");
        gr.add("Dreadnought");
        gr.add("Carrier");
        gr.add("Capital Industrial Ship");
        gr.add("Freighter");
        gr.add("Jump Freighter");
        gr.add("Marauder");
        gr.add("Black Ops");
        EXPENSIVE_GROUPS = Collections.unmodifiableSet(gr);
    }

    private PriceFileWriter writer;
    private BlueprintCalculator bpCalc = new BlueprintCalculator();

    public void makePriceFile(Collection<MarketItem> items) {
        writer = new PriceFileWriter(filename);
        processItems(items);
        writer.close();
    }

    private void processItems(Collection<MarketItem> items) {
        for (MarketItem item : items) {
            if (NPCTraded(item.type)) {
                processNPCTradedType(item.type);
            } else {
                printItem(item);
            }
        }
    }

    private boolean NPCTraded(MarketType type) {
        return NPC_TRADED.contains(type.getCategory());
    }

    private void processNPCTradedType(MarketType type) {
        writer.printLine(type.getBasePrice(), type.getVolume(), type.getName());
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
        if (price < 1e+9) return;
        if (type.getMetaGroup().equals("Officer")) return;
        if (EXPENSIVE_GROUPS.contains(type.getGroup())) return;
        if (blueprint && type.getMetaGroup().equals("Tech II")) return;
        printWarning(type, price, blueprint);
    }

    private void printWarning(MarketType type, double price, boolean bpc) {
        System.out.println("HIGH PRICE WARNING: " + type.getName() + (bpc ? " (BPC)" : "") + " price is " + price);
    }
}
