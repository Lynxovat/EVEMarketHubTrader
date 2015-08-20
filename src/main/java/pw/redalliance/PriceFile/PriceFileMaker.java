package pw.redalliance.PriceFile;

import com.tree.TreeNode;
import pw.redalliance.MarketAPI.MarketAPI;
import pw.redalliance.MarketItem;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketTreeHandler;
import pw.redalliance.MarketTree.MarketType;

import java.io.*;
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
        npcTraded.add("Blueprints");
        npcTraded.add("Skills");
        NPC_TRADED = Collections.unmodifiableSet(npcTraded);
    }

    private PriceFileWriter writer;
    private MarketAPI api;
    private BlueprintCalculator bpCalc = new BlueprintCalculator();

    public void makePriceFile(TreeNode<MarketGroup> tree, MarketAPI api) {
        writer = new PriceFileWriter(filename);
        this.api = api;
        processTree(tree);
        writer.close();
    }

    private void processTree(TreeNode<MarketGroup> tree) {
        for (TreeNode<MarketGroup> group : tree) {
            if (group.data.hasTypes()) {
                System.out.print(MarketTreeHandler.groupHierarchy(group) + " [" + group.data.getTypes().size() + "] ");
                if (NPCTraded(group)) {
                    processNPCTradedGroup(group);
                } else {
                    processMarketGroup(group);
                }
                System.out.println("OK");
            }
        }
    }

    private boolean NPCTraded(TreeNode<MarketGroup> group) {
        TreeNode<MarketGroup> g = group;
        while (g != null) {
            if (NPC_TRADED.contains(g.data.getName())) {
                return true;
            }
            g = g.parent;
        }
        return false;
    }

    private void processNPCTradedGroup(TreeNode<MarketGroup> group) {
        for (MarketType type : group.data.getTypes()) {
            writer.printLine(type.getBasePrice(), type.getVolume(), type.getName());
        }
    }

    private void processMarketGroup(TreeNode<MarketGroup> group) {
        for (MarketType type : group.data.getTypes()) {
            MarketItem item = new MarketItem(type);
            item.marketData = api.getData(type.getTypeId(), jitaId);
            printItem(item);
        }
    }

    private void printItem(MarketItem item) {
        writer.printLine(item.marketData.getMaxBye(), item.type.getVolume(), item.type.getName());
        if (bpCalc.calculateBlueprint(item)) {
            writer.printLine(bpCalc.getPrice(), bpCalc.getVolume(), bpCalc.getName());
        }
    }
}
