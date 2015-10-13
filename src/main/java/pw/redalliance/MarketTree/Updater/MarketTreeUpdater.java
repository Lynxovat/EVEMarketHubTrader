package pw.redalliance.MarketTree.Updater;

import com.tree.TreeNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.yaml.snakeyaml.Yaml;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.MarketType;
import pw.redalliance.MarketTree.Updater.MarketJDBCTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Lynx on 21.07.2015.
 */
public class MarketTreeUpdater {
    MarketJDBCTemplate marketTemplate;
    private ArrayList<MarketType> types;
    private TreeMap<Integer, String> categories;


    public MarketTreeUpdater() {
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

        marketTemplate = (MarketJDBCTemplate) context.getBean("MarketJDBCTemplate");
    }

    public TreeNode<MarketGroup> makeMarketTree() {
        MarketGroup root = new MarketGroup("Market");
        root.setId(0);
        root.setHasTypes(false);
        TreeNode<MarketGroup> tree = new TreeNode<>(root);
        processNode(tree);
        return tree;
    }

    private void processNode(TreeNode<MarketGroup> parentNode) {
        List<MarketGroup> groups = marketTemplate.listMarketGroup(parentNode.data.getId());
        for (MarketGroup group : groups) {
            TreeNode<MarketGroup> node = parentNode.addChild(group);
            processNode(node);
            if (group.hasTypes()) {
                group.setTypes(marketTemplate.listMarketTypes(group.getId()));
            }
        }
    }

    private void readYAMLs() {

    }

    public void readYAML(String fileName) {
        Yaml yaml = new Yaml();
        try {
            InputStream ios = new FileInputStream(new File(fileName));
            Map<String,Object> result = (Map<String,Object>) yaml.load(ios);
            System.out.println(result.toString());
//            Collection<Object> file = result.values();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
