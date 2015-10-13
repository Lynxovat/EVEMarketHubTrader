package pw.redalliance;

import com.tree.TreeNode;
import pw.redalliance.MarketAPI.EveCentral.EveCentralMarketAPI;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketTreeHandler;
import pw.redalliance.MarketTree.Updater.MarketTreeUpdater;
import pw.redalliance.MarketTree.Updater.YAML.YAMLCategoryMapper;
import pw.redalliance.MarketTree.Updater.YAML.YAMLReader;
import pw.redalliance.PriceFile.PriceFileMaker;

import java.util.concurrent.TimeUnit;


public class Main {
    private static void test() {
        String path = "./src/main/resources/categoryIDs.yaml";
        System.out.println(YAMLReader.readYAML(path, new YAMLCategoryMapper()));
//        MarketTreeUpdater upd = new MarketTreeUpdater();
//        upd.readYAML("./src/main/resources/typeIDs.yaml");
        if (true) return;

        System.out.println("Reading types from database");
        TreeNode<MarketGroup> tree = MarketTreeHandler.getFromDatabase();
        count(tree);
        save1(tree);

        System.out.println("\nMaking pricefile using EveCentral API");
        PriceFileMaker pfm = new PriceFileMaker();
        pfm.makePriceFile(tree, new EveCentralMarketAPI());
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
//        System.out.println("TEST");
        test();
        System.out.println("[MAIN] Elapsed time: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
    }

    private static void testUpdater() {
        MarketTreeHandler.print(MarketTreeHandler.getFromDatabase());
    }

    private static void save() {
        System.out.println("Reading from DB");
        TreeNode<MarketGroup> tree = MarketTreeHandler.getFromDatabase();
        save1(tree);
        count(tree);
    }

    private static void save1(TreeNode<MarketGroup> tree) {
        System.out.print("Serializing ");
        try {
            MarketTreeHandler.serializeTree(tree, "test.bin");
            System.out.println("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TreeNode<MarketGroup> load() {
        System.out.print("Deserializing ");
        TreeNode<MarketGroup> tree = null;
        try {
            tree = MarketTreeHandler.deserializeTree("test.bin");
            System.out.println("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tree != null) {
            count(tree);
//            MarketTreeHandler.print(tree);
        } else {
            System.out.println("Tree = NULL");
        }
        return tree;
    }

    private static void count(TreeNode<MarketGroup> tree) {
        int elements = 0;
        for (TreeNode<MarketGroup> node : tree) {
            elements += node.data.getTypes().size();
        }
        System.out.println("Types in tree: " + elements);
    }
}

/*TODO:
-report IOException in PriceFileMaker.java
*/
