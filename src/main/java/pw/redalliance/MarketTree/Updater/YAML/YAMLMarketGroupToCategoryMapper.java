package pw.redalliance.MarketTree.Updater.YAML;

import java.util.Map;

/**
 * Created by Lynx on 14.10.2015.
 */
public class YAMLMarketGroupToCategoryMapper extends YAMLObjectMapper<YAMLGroup> {
	private Map<Integer,String> categories;

	public YAMLMarketGroupToCategoryMapper(Map<Integer,String> categories) {
		super();
		this.categories = categories;
	}

    @Override
    protected YAMLGroup mapResult() {
        return new YAMLGroup(value_s("name.en"), categories.get(value_i("categoryID")));
    }
}