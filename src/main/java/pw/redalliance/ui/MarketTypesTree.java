//http://stackoverflow.com/questions/30964007/how-to-define-a-simple-model-for-a-complex-treeviewt-in-javafx-8

package pw.redalliance.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pw.redalliance.MarketItem;
import pw.redalliance.MarketTree.DataBase.ItemJDBCTemplate;
import pw.redalliance.MarketTree.MarketGroup;
import pw.redalliance.MarketTree.Updater.MarketJDBCTemplate;

import java.util.*;

public class MarketTypesTree extends Application {
    private int wrongHasTypes = 0;

    private static final Set<String> META_GROUPS;
    static {
        Set<String> metaGroups = new LinkedHashSet<>();
        metaGroups.add("Faction");
        metaGroups.add("Deadspace");
        metaGroups.add("Officer");
        META_GROUPS = Collections.unmodifiableSet(metaGroups);
    }

	MarketJDBCTemplate groupTemplate;
	ItemJDBCTemplate itemsTemplate;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Market Tree");
		initializeSource();

		TreeView tree = new TreeView<>(makeMarketTree());
		setupTree(tree);

		Scene scene = new Scene(tree, 600, 800);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

    private void setupTree(TreeView tree) {
        tree.setCellFactory(p -> new CheckBoxTreeCell<Object>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else {
                    if (item instanceof MarketGroup) {
                        setText(((MarketGroup) item).getName());
                    } else if (item instanceof MarketItem) {
                        setText(((MarketItem) item).type.getName());
                    }
                }
            }
        });
    }

	private void initializeSource() {
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        groupTemplate = (MarketJDBCTemplate) context.getBean("MarketJDBCTemplate");
        itemsTemplate = (ItemJDBCTemplate) context.getBean("ItemJDBCTemplate");
	}	

	private CheckBoxTreeItem<MarketGroup> makeMarketTree() {
         MarketGroup rootGroup = new MarketGroup("Market");
         rootGroup.setId(0);
         rootGroup.setHasTypes(false);
         CheckBoxTreeItem<MarketGroup> root = new CheckBoxTreeItem<>(rootGroup);
         root.setExpanded(true);
         processNode(root);
         reportCcpfails();
         return root;
    }

    private void reportCcpfails() {
        System.out.println("Wrong has types: " + wrongHasTypes);
        wrongHasTypes = 0;
    }
 
    private void processNode(CheckBoxTreeItem<MarketGroup> parentNode) {        
        List<MarketGroup> groups = groupTemplate.listMarketGroup(parentNode.getValue().getId());
        for (MarketGroup group : groups) {
        	CheckBoxTreeItem<MarketGroup> node = new CheckBoxTreeItem<>(group);
        	parentNode.getChildren().add(node);
            processNode(node);
            if (group.hasTypes()) {
            	insertTypes(node, group.getId());
            }
        }
    }

    private void insertTypes(CheckBoxTreeItem node, int groupId) {
        Map<String,List<MarketItem>> items = getSortedItems(groupId);

        if (items.size() == 1) {
            addItems(node, items.entrySet().iterator().next().getValue());
        } else {
            if (items.containsKey(null)) {
                addItems(node, items.get(null));
            }
            META_GROUPS.stream().filter(metaGroup -> items.containsKey(metaGroup)).forEach(metaGroup -> {
                CheckBoxTreeItem<String> subNode = new CheckBoxTreeItem<>(metaGroup);
                node.getChildren().add(subNode);
                addItems(subNode, items.get(metaGroup));
            });
        }
    }

    private Map<String,List<MarketItem>> getSortedItems(int marketGroupId) {
        Map<String,List<MarketItem>> items = new HashMap<>();

        List<MarketItem> itemsList = itemsTemplate.listMarketGroupItems(marketGroupId);

        //Костыль, ибо ццп в своих дампах не проставляет "hasTypes = true" для некоторых групп, у которых нет айтемов
        if (itemsList.isEmpty()) {
            ++wrongHasTypes;
            return items;
        }

        //Костыль, ибо ццп в своих дампах не проставляет правильные мета левел кораблям нормально
        if ("Ship".equals(itemsList.get(0).type.getCategory())) {
            items.put(null, itemsList);
            return items;
        }

        for (MarketItem item : itemsList) {
            boolean inserted = false;
            for (String metaGroup : META_GROUPS) {
                if (metaGroup.equals(item.type.getMetaGroup())) {
                    addItemToMap(items, item, metaGroup);
                    inserted = true;
                    break;
                }
            }
            if (!inserted) {
                addItemToMap(items, item, null);
            }
        }

        return items;
    }

    private void addItemToMap(Map<String, List<MarketItem>> items, MarketItem item, String metaGroup) {
        if (!items.containsKey(metaGroup)) {
            items.put(metaGroup, new ArrayList<>());
        }
        items.get(metaGroup).add(item);
    }

    private void addItems(CheckBoxTreeItem node, List<MarketItem> items) {
        for (MarketItem item : items) {
            addItem(node, item);
        }
    }

    private void addItem(CheckBoxTreeItem node, MarketItem item) {
        CheckBoxTreeItem treeItem = new CheckBoxTreeItem<>(item);
        node.getChildren().add(treeItem);
        treeItem.setSelected(item.isSelected());

        treeItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
            item.setSelected(newValue);
            itemsTemplate.updateItemSelected(item);
        });
    }
}