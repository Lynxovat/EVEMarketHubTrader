package pw.redalliance.MarketTree.Updater.YAML;

import pw.redalliance.MarketTree.MarketType;

import java.util.Map;

/**
 * Created by Lynx on 14.10.2015.
 */
public class YAMLMarketGroupToCategoryMapper extends YAMLObjectMapper<String> {
	private Map<Integer,String> categories;

	public YAMLMarketGroupToCategoryMapper(Map<Integer,String> categories) {
		super();
		this.categories = categories;
	}

    @Override
    protected String mapResult() {
        return categories.get(value_i("categoryID"));
    }
}