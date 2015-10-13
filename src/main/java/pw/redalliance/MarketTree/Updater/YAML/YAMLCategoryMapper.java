package pw.redalliance.MarketTree.Updater.YAML;

/**
 * Created by Lynx on 13.10.2015.
 */
public class YAMLCategoryMapper extends YAMLObjectMapper<String> {
    @Override
    protected String mapResult() {
        return value_s("name.en");
    }
}
