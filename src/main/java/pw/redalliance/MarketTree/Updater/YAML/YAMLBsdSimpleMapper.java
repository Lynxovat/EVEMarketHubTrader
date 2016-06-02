package pw.redalliance.MarketTree.Updater.YAML;

import java.util.Map;

public class YAMLBsdSimpleMapper<KeyT, ValT> extends YAMLBsdAbstractMapper<KeyT, ValT> {
    protected String keyK;
    protected String valueK;

    public YAMLBsdSimpleMapper(String keyK, String valueK) {
        this.keyK = keyK;
        this.valueK = valueK;
    }

    @Override
    public boolean map(Map<String, Object> entry) {
        if (!(entry.containsKey(keyK) && entry.containsKey(valueK))) {
            return false;
        }

        this.key = (KeyT)entry.get(keyK);
        this.value = (ValT)entry.get(valueK);
        return true;
    }
}