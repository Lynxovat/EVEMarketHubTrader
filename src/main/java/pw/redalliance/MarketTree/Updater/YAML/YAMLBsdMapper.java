package pw.redalliance.MarketTree.Updater.YAML;

import java.util.Map;

public interface YAMLBsdMapper<KeyT, ValT> {
    boolean map(Map<String, Object> entry);
    KeyT getKey();
    ValT getValue();
}
