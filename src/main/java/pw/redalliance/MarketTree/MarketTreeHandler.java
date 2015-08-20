package pw.redalliance.MarketTree;

import com.tree.TreeNode;
import pw.redalliance.MarketTree.Updater.MarketTreeUpdater;

import java.io.*;

/**
 * Created by Lynx on 22.07.2015.
 */
public abstract class MarketTreeHandler {
    public static TreeNode<MarketGroup> getFromDatabase() {
        return new MarketTreeUpdater().makeMarketTree();
    }

    public static void serializeTree(TreeNode<MarketGroup> tree, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        serializeNode(oos, tree);
        oos.flush();
        oos.close();
    }

    private static void serializeNode(ObjectOutputStream oos, TreeNode<MarketGroup> parentNode) throws IOException {
        oos.writeObject(parentNode.data);
        oos.writeInt(parentNode.children.size());
        for (TreeNode<MarketGroup> node : parentNode.children) {
            serializeNode(oos, node);
        }
    }

    public static TreeNode<MarketGroup> deserializeTree(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream oin = new ObjectInputStream(fis);
        return deserializeNode(oin, null);
    }

    public static TreeNode<MarketGroup> deserializeNode(ObjectInputStream oin, TreeNode<MarketGroup> parentNode) throws IOException, ClassNotFoundException {
        MarketGroup group = (MarketGroup)oin.readObject();
        TreeNode<MarketGroup> node = parentNode == null ?
                new TreeNode<>(group) :
                parentNode.addChild(group);
        for (int i = 0, size = oin.readInt(); i < size; ++i) {
            deserializeNode(oin, node);
        }
        return node;
    }

    public static void print(TreeNode<MarketGroup> tree) {
        StringBuilder sb = new StringBuilder();
        for (TreeNode<MarketGroup> node : tree) {
            for (int i = 0, size = node.getLevel(); i < size; ++i) {
                sb.append(' ');
            }
            sb.append(node);
            sb.append('\n');
        }
        System.out.println(sb.toString());
    }

    public static String groupHierarchy(TreeNode<MarketGroup> group) {
        return (group.isRoot() ? "" : (group.parent.data.getName() + "->")) + group.data.getName();
    }
}
