package pw.redalliance;

import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pw.redalliance.MarketAPI.EveCentral.EveCentralMarketAPI;
import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketAPI.MarketAPI;
import pw.redalliance.MarketTree.DataBase.ItemJDBCTemplate;
import pw.redalliance.MarketTree.MarketType;
import pw.redalliance.MarketTree.MarketTypesUpdater;
import pw.redalliance.PriceFile.PriceFileMaker;
import pw.redalliance.ui.ItemsTable;
import pw.redalliance.ui.MarketTypesTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final int JITA_ID = 30000142;
    private static final int HOME_ID = 00000001;

    public static void main(String[] args) {
//        final EveNetwork eve = new DefaultEveNetwork();
//        final ServerStatusRequest request = new ServerStatusRequest();
//        final ServerStatusResponse status = eve.execute(request);
//        System.out.println(status.getOnlinePlayers() + " pilots online");
//        if (true) return;


        Scanner reader = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Select option:\n" +
                    "0 - exit\n" +
                    "1 - update database\n" +
                    "2 - update prices (jita)\n" +
                    "3 - show tree\n" +
                    "4 - create prices file\n" +
                    "5 - update prices (jita - C-J6MT)\n" +
                    "6 - show table");
            int n = reader.nextInt();
            long startTime = System.nanoTime();
            switch (n) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    updateDB();
                    break;
                case 2:
                    updatePrices(JITA_ID);
                    break;
                case 3:
                    exit = true;
                    showTree(args);
                    break;
                case 4:
                    createPricesFile();
                    break;
                case 5:
                    updatePrices(JITA_ID, HOME_ID);
                    break;
                case 6:
                    exit = true;
                    showTable(args);
                    break;                                
                default:
                    System.out.println("Number: " + n);
            }
            System.out.println("[MAIN] Elapsed time: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
        }
    }

    private static void updateDB() {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        ItemJDBCTemplate itemTemplate = (ItemJDBCTemplate) context.getBean("ItemJDBCTemplate");
        itemTemplate.truncate();
        MarketTypesUpdater updater = new MarketTypesUpdater();
        Collection<MarketType> types = updater.makeMarketTypes();
        List items = new ArrayList<MarketItem>();
        for (MarketType type : types) {
            MarketItem item = new MarketItem(type);
            item.marketData = new ItemMarketData(0, 0, 0);
            items.add(item);
        }
        itemTemplate.insertMarketItems(items);
    }

    private static void updatePrices(int systemId) {
        updatePrices(systemId, 0);
    }

    private static void updatePrices(int systemByeId, int systemSellId) {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        ItemJDBCTemplate itemTemplate = (ItemJDBCTemplate) context.getBean("ItemJDBCTemplate");

        List<MarketItem> items = itemTemplate.listItems();

        fillWithPrices(items, systemByeId, systemSellId);

        System.out.println("Updating items");
        itemTemplate.updateItemsMarketData(items);
    }

    private static void fillWithPrices(List<MarketItem> items, int systemByeId, int systemSellId) {
        MarketAPI api = new EveCentralMarketAPI();
        int counter = 0;
        System.out.println("Requesting market data from EVE-CENTRAL API");
        for (MarketItem item : items) {
            ItemMarketData marketData = api.getData(item.type.getTypeId(), systemByeId);
            if (systemSellId != 0) {
                ItemMarketData sellSystemMD = api.getData(item.type.getTypeId(), systemSellId);
                marketData.setMinSell(sellSystemMD.getMinSell());
                marketData.setVolume(sellSystemMD.getVolume());
            }
            item.marketData = marketData;

            if ((++counter % 500) == 0) {
                System.out.println("API: " + counter + " item processed");
            }
        }
    }

    private static void showTree(String[] args) {
        Application.launch(MarketTypesTree.class, args);
    }

        private static void showTable(String[] args) {
        Application.launch(ItemsTable.class, args);
    }

    private static void createPricesFile() {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        ItemJDBCTemplate itemTemplate = (ItemJDBCTemplate) context.getBean("ItemJDBCTemplate");

        PriceFileMaker pfm = new PriceFileMaker();
        pfm.makePriceFile(itemTemplate.listItems());
    }
}

