package pw.redalliance.MarketTree.Updater.YAML;

import pw.redalliance.MarketTree.MarketType;

import java.util.Map;

/**
 * Created by Lynx on 13.10.2015.
 */
public class YAMLMarketTypeMapper extends YAMLObjectMapper<MarketType> {
	private Map<Integer,YAMLGroup> groups;    

	public YAMLMarketTypeMapper(Map<Integer,YAMLGroup> groups) {
		super();
        this.groups = groups;		
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
        YAMLGroup group = groups.get(value_i("groupID"));
        type.setGroup(group.group);
    	type.setCategory(group.category);
    	type.setVolume(value_d("volume"));
    	type.setBasePrice(value_d("basePrice"));
    	type.setIconId(value_i("iconId"));
        return type;
    }

}