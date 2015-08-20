package pw.redalliance.MarketTree.Updater;

import com.tree.TreeNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pw.redalliance.MarketTree.MarketGroup;

import java.util.List;

/**
 * Created by Lynx on 21.07.2015.
 */
public class MarketTreeUpdater {
    MarketJDBCTemplate marketTemplate;

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
            if (group.hasTypes() == true) {
                group.setTypes(marketTemplate.listMarketTypes(group.getId()));
            }
        }
    }
}
