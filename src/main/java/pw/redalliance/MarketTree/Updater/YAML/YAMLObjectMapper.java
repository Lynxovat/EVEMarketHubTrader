package pw.redalliance.MarketTree.Updater.YAML;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Lynx on 13.10.2015.
 */
public class YAMLObjectMapper<T> implements YAMLMapper {
    private Map<String,Object> result;

    @Override
    public T map(Object o) {
        this.result = mp(o);
        return mapResult();
    }

    protected T mapResult() {
        return (T)result;
    }

    protected String value(String path) {
        String[] parts = path.split("\\.");
        int size = parts.length;
        Map<String,Object> map = result;
        for (int i = 0; i < size-1; ++i) {
            map = mp(map.get(parts[i]));
        }
        return (String)map.get(parts[size-1]);
    }

    private static Map<String,Object> mp(Object o) {
        return (Map<String,Object>)o;
    }


}
