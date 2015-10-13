package pw.redalliance.MarketTree.Updater.YAML;

import java.util.Map;

/**
 * Created by Lynx on 13.10.2015.
 */
public interface YAMLMapper<T> {
    public T map(Object o);
}
