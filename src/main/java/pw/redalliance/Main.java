package pw.redalliance;

import com.tree.TreeNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pw.redalliance.MarketAPI.EveCentral.EveCentralMarketAPI;
import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketAPI.MarketAPI;
import pw.redalliance.MarketTree.DataBase.ItemJDBCTemplate;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketType;
import pw.redalliance.MarketTree.MarketTypesUpdater;
import pw.redalliance.MarketTree.Updater.MarketJDBCTemplate;
import pw.redalliance.PriceFile.PriceFileMaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Main {
    private static void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        ItemJDBCTemplate itemTemplate = (ItemJDBCTemplate) context.getBean("ItemJDBCTemplate");
//
//        MarketItem it = itemTemplate.listSelectedItems().get(0);
//        System.out.println(it);
//        System.out.println(it.type.getName().length());
//        if (true) return;
//
//        MarketType mt = new MarketType();
//        mt.setTypeId(1);
//        mt.setName("a");
//        mt.setCategory("a_c");
//        mt.setMarketGroupId(5);
//        mt.setMetaLevel(1);
//        mt.setMetaGroup("Tech I");
//        mt.setVolume(10);
//        mt.setBasePrice(500000);
//        mt.setIconId(1);
//        MarketItem item = new MarketItem(mt);
//        item.marketData = new ItemMarketData(40000.9, 60000.5, 9432785);
//        item.setSelected(true);
//        List list = new ArrayList<MarketItem>();
//        list.add(item);
//
//        itemTemplate.insertMarketItems(list);


        MarketTypesUpdater updater = new MarketTypesUpdater();
        Collection<MarketType> types = updater.makeMarketTypes();
//        PriceFileMaker pfm = new PriceFileMaker();
//        pfm.makePriceFile(types, new EveCentralMarketAPI());

        List items = new ArrayList<MarketItem>();
        MarketAPI api = new EveCentralMarketAPI();
        boolean a = true;
        int counter = 0;
        for (MarketType type : types) {
            MarketItem item = new MarketItem(type);
            item.marketData = api.getData(type.getTypeId(), 30000142);
            item.setSelected(a);
            items.add(item);
            a = !a;
            if ((++counter % 500) == 0) {
                System.out.println("API: " + counter + " types processed");
            }
        }

        System.out.println("Inserting items");
        itemTemplate.insertMarketItems(items);

//        int i = 0;
//        for (MarketType type : types) {
//            if (++i > 100) return;
//            System.out.println(type.toString());
//        }
//        MarketTreeUpdater upd = new MarketTreeUpdater();
//        upd.readYAML("./src/main/resources/typeIDs.yaml");

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
