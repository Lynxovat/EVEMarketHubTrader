//http://stackoverflow.com/questions/30964007/how-to-define-a-simple-model-for-a-complex-treeviewt-in-javafx-8
//

package pw.redalliance.ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.util.List;

public class MarketTypesTree extends Application {
	MarketJDBCTemplate groupTemplate;
	ItemJDBCTemplate itemsTemplate;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Market Tree");
		initializeSource();

		TreeView tree = new TreeView<>(makeMarketTree());
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

		Scene scene = new Scene(tree, 600, 800);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
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
         return root;
    }
 
    private void processNode(CheckBoxTreeItem<MarketGroup> parentNode) {
        List<MarketGroup> groups = groupTemplate.listMarketGroup(parentNode.getValue().getId());
        for (MarketGroup group : groups) {
        	CheckBoxTreeItem<MarketGroup> node = new CheckBoxTreeItem<>(group);
        	parentNode.getChildren().add(node);
            processNode(node);
            if (group.hasTypes()) {
            	for (MarketItem item : itemsTemplate.listMarketGroupItems(group.getId())) {
            		CheckBoxTreeItem treeItem = new CheckBoxTreeItem<>(item);
            		node.getChildren().add(treeItem);
					treeItem.setSelected(item.isSelected());

                    treeItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
//                        MarketItem item = (MarketItem) treeItem.getValue();
                        item.setSelected(newValue);
                        itemsTemplate.updateItemSelected(item);
                    });
            	}                
            }
        }
    }
}