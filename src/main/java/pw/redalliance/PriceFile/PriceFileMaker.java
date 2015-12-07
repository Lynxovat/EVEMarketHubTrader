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
    private static final String filename = "output.txt";

    private static final Set<String> NPC_TRADED;
    static {
        Set<String> npcTraded = new HashSet<>();
        npcTraded.add("Blueprint");
        npcTraded.add("Skill");
        NPC_TRADED = Collections.unmodifiableSet(npcTraded);
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
        if ((!blueprint && type.getCategory().equals("Ship")) || (blueprint && type.getMetaGroup().equals("Tech II")) || type.getMetaGroup().equals("Officer")) return;
        printWarning(type, price, blueprint);
    }

    private void printWarning(MarketType type, double price, boolean bpc) {
        System.out.println("HIGH PRICE WARNING: " + type.getName() + (bpc ? " (BPC)" : "") + " price is " + price);
    }
}
