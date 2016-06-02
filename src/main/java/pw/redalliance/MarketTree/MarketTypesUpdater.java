package pw.redalliance.MarketTree;

import pw.redalliance.MarketTree.Updater.YAML.*;

import java.util.*;

public class MarketTypesUpdater {
    private Map<Integer,MarketType> types;
    private static final String YAML_PATH = "./src/main/resources/";


    public MarketTypesUpdater() {
    }

    public Collection<MarketType> makeMarketTypes() {
        System.out.println("Processing YAML files:");
        readYAMLs();
        readMetaData();
        System.out.println("Types size: " + types.size());
        return types.values();
    }

    private void readYAMLs() {
        String path = YAML_PATH + "fsd/";
        Map<Integer,String> categories = YAMLFsdReader.readYAML(path + "categoryIDs.yaml", new YAMLCategoryMapper());
        System.out.println("categories");
        Map<Integer,YAMLGroup> groups = YAMLFsdReader.readYAML(path + "groupIDs.yaml", new YAMLMarketGroupToCategoryMapper(categories));
        System.out.println("groups");
        types = YAMLFsdReader.readYAML(path + "typeIDs.yaml", new YAMLMarketTypeMapper(groups));
        System.out.println("types");
    }

    private void readMetaData() {
        String path = YAML_PATH + "bsd/";
        Map<Integer, Integer> metaTypes = YAMLBsdReader.readMap(path + "dgmTypeAttributes.yaml", new YAMLBsdSimpleMapper<Integer, Integer>("typeID", "valueInt") {

            private static final String attrKey = "attributeID";

            @Override
            public boolean map(Map<String, Object> entry) {
                if (!(entry.containsKey(attrKey) && (633 == (int)entry.get(attrKey)))) {
                    return false;
                }

                if (!entry.containsKey(this.valueK)) {
                    this.key = (Integer)entry.get(keyK);
                    this.value = ((Double)entry.get("valueFloat")).intValue();
                    return true;
                }

                return super.map(entry);
            }
        });
        System.out.println("attributes");

        Map<Integer, String> metaGroupNames = YAMLBsdReader.readMap(path + "invMetaGroups.yaml", new YAMLBsdSimpleMapper<Integer, String>("metaGroupID", "metaGroupName"));
        System.out.println("meta groups");

        Map<Integer, String> metaGroups = YAMLBsdReader.readMap(path + "invMetaTypes.yaml", new YAMLBsdSimpleMapper<Integer, String>("typeID", "metaGroupID") {
           @Override
           public boolean map(Map<String, Object> entry)  {
                if (!super.map(entry)) {
                    return false;
                }

               if (!metaGroupNames.containsKey(getValue())) {
                    return false;                    
                }

                this.value = metaGroupNames.get(getValue());
                return true;
           }
        });
        System.out.println("meta types");

        for (Map.Entry<Integer, MarketType> entry : types.entrySet()) {
            int typeID = entry.getKey();
            MarketType type = entry.getValue();

            type.setTypeId(typeID);

            if (metaTypes.containsKey(typeID)) {
                type.setMetaLevel(metaTypes.get(typeID));
            }
            if (metaGroups.containsKey(typeID)) {
                type.setMetaGroup(metaGroups.get(typeID));
            }
        }
    }
}