package pw.redalliance.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.BeanPathAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pw.redalliance.MarketItem;
import pw.redalliance.MarketTree.DataBase.ItemJDBCTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class ItemsTable extends Application {
	ItemJDBCTemplate itemsTemplate;	

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Items Table");
		initializeSource();

		TableView table = new TableView<>();
		setupTable(table);
		fillTable(table);

		Scene scene = new Scene(table, 800, 600);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

    private void setupTable(TableView table) {
    }

	private void initializeSource() {
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        itemsTemplate = (ItemJDBCTemplate) context.getBean("ItemJDBCTemplate");
	}

	public class Row {
		private final MarketItem item;
		public Row(MarketItem item) { this.item = item; }

		public void setName() {}
		public String getName() { return item.type.getName(); }

		public void setBye() {}
		public Double getBye() { return item.marketData.getMaxBye(); }

		public void setSell() {}
		public Double getSell() { return item.marketData.getMinSell(); }

		public void setVolume() {}
		public Double getVolume() { return item.marketData.getVolume(); }
	}

	public class TableModel {
		public TableModel(List<MarketItem> items) {
			this.rows = new ArrayList<>();
			this.rows.addAll(items.stream().map(Row::new).collect(Collectors.toList()));
		}
		public List<Row> getRows() {
			return rows;
		}
		public void setRows(List<Row> rows) {}
		private final List<Row> rows;
	}

	private void fillTable(TableView table) {		
		final TableModel holder = new TableModel(itemsTemplate.listSelectedItems());
		final BeanPathAdapter<TableModel> ItemsHolderPA = new BeanPathAdapter<>(holder);

		table.getColumns().addAll(
				createColumn("Name", 	300, "name"),
				createColumn("Bye",  	100, "bye"),
				createColumn("Sell",  	100, "sell"),
				createColumn("Volume",  100, "volume")
		);

		ItemsHolderPA.bindContentBidirectional("rows", null, String.class, table.getItems(), Row.class, null, null);
	}

	private static TableColumn createColumn(String caption, int width, String parameter) {
		TableColumn column = new TableColumn<>(caption);
		column.setMinWidth(width);
		column.setCellValueFactory(new PropertyValueFactory<>(parameter));
		return column;
	}
}