package pw.redalliance.MarketTree.Updater.YAML;

import pw.redalliance.MarketTree.MarketType;

import java.util.Map;

/**
 * Created by Lynx on 13.10.2015.
 */
public class YAMLMarketTypeMapper extends YAMLObjectMapper<MarketType> {
	private Map<Integer,String> categories;

	public YAMLMarketTypeMapper(Map<Integer,String> categories) {
		super();
		this.categories = categories;
	}

    @Override
    protected MarketType mapResult() {
    	if (!value_b("published")) {
    		return null;
    	}
		int marketGroupID = value_i("marketGroupID");
		if (marketGroupID == 0) {
			return null;
		}
    	MarketType type = new MarketType();
		type.setMarketGroupId(marketGroupID);
    	type.setName(value_s("name.en"));
    	type.setCategory(categories.get(value_i("groupID")));
    	type.setVolume(value_d("volume"));
    	type.setBasePrice(value_d("basePrice"));
    	type.setIconId(value_i("iconId"));
        return type;
    }

}