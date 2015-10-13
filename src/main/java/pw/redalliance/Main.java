package pw.redalliance;

import com.tree.TreeNode;
import pw.redalliance.MarketAPI.EveCentral.EveCentralMarketAPI;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketType;
import pw.redalliance.MarketTree.MarketTypesUpdater;
import pw.redalliance.PriceFile.PriceFileMaker;

import java.util.Collection;
import java.util.concurrent.TimeUnit;


public class Main {
    private static void test() {
        MarketTypesUpdater updater = new MarketTypesUpdater();
        Collection<MarketType> types = updater.makeMarketTypes();
        PriceFileMaker pfm = new PriceFileMaker();
        pfm.makePriceFile(types, new EveCentralMarketAPI());

//        int i = 0;
//        for (MarketType type : types) {
//            if (++i > 100) return;
//            System.out.println(type.toString());
//        }
//        MarketTreeUpdater upd = new MarketTreeUpdater();
//        upd.readYAML("./src/main/resources/typeIDs.yaml");
        if (true) return;

//        System.out.println("Reading types from database");

//        System.out.println("\nMaking pricefile using EveCentral API");
//        pfm.makePriceFile(tree, new EveCentralMarketAPI());
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
//        System.out.println("TEST");
        test();
        System.out.println("[MAIN] Elapsed time: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
    }
}

/*TODO:
-report IOException in PriceFileMaker.java
-issues: skill books are not in output
         output has 9509 lines of 12113 types (even with b2 bpo and faction bpc)
*/
