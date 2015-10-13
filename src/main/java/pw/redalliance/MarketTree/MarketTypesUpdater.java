package pw.redalliance.MarketTree;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pw.redalliance.MarketTree.Updater.MarketJDBCTemplate;
import pw.redalliance.MarketTree.Updater.YAML.YAMLCategoryMapper;
import pw.redalliance.MarketTree.Updater.YAML.YAMLMarketGroupToCategoryMapper;
import pw.redalliance.MarketTree.Updater.YAML.YAMLMarketTypeMapper;
import pw.redalliance.MarketTree.Updater.YAML.YAMLReader;

import java.util.*;

public class MarketTypesUpdater {
    MarketJDBCTemplate marketTemplate;
    private Map<Integer,MarketType> types;


    public MarketTypesUpdater() {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

        marketTemplate = (MarketJDBCTemplate) context.getBean("MarketJDBCTemplate");
    }

    public Collection<MarketType> makeMarketTypes() {
        readYAMLs();
        System.out.println("Types size: " + types.size() + "\nRequesting data from database:");
        final int size = types.size();
        int count = 0;
        int threshold = 2000;
        for (Map.Entry<Integer, MarketType> entry : types.entrySet()) {
            int typeID = entry.getKey();
            MarketType type = entry.getValue();

            type.setTypeId(typeID);
            type.setDBData(marketTemplate.getMarketTypeData(typeID));

            if ((++count % threshold) == 0) {
                System.out.println("DB: " + count + " types of " + size + " processed");
            }
        }
        return types.values();
    }

    private void readYAMLs() {
        String path = "./src/main/resources/";
        System.out.println("Processing YAML files:");
        Map<Integer,String> categories = YAMLReader.readYAML(path + "categoryIDs.yaml", new YAMLCategoryMapper());
        System.out.println("categories");
        categories = YAMLReader.readYAML(path + "groupIDs.yaml", new YAMLMarketGroupToCategoryMapper(categories));
        System.out.println("groups");
        types = YAMLReader.readYAML(path + "typeIDs.yaml", new YAMLMarketTypeMapper(categories));
        System.out.println("types");
    }
}