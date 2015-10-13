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

    protected String value_s(String path) {
        return (String)value(path, String.class, "");
    }

    protected Integer value_i(String path) {
        return (Integer)value(path, Integer.class, 0);
    }

    protected Double value_d(String path) {
        return (Double)value(path, Double.class, 0.0);
    }

    protected Boolean value_b(String path) {
        return (Boolean)value(path, Boolean.class, false);
    }

    protected Object value(String path, Class<?> t, Object defaultValue) {
        String[] parts = path.split("\\.");
        int size = parts.length;
        Map<String,Object> map = result;
        for (int i = 0; i < size-1; ++i) {
            map = mp(map.get(parts[i]));
        }
        Object res = map.get(parts[size-1]);
        if (res != null && res.getClass() == t) {
            return res;
        }
        return defaultValue;
    }

    private static Map<String,Object> mp(Object o) {
        return (Map<String,Object>)o;
    }
}