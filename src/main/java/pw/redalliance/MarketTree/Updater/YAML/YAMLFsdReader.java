package pw.redalliance.MarketTree.Updater.YAML;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Lynx on 13.10.2015.
 */
public class YAMLFsdReader {
    static public <T> Map<Integer,T> readYAML(String fileName, YAMLMapper<? extends T> mapper) {
        Yaml yaml = new Yaml();
        try {
            InputStream ios = new FileInputStream(new File(fileName));
            Map<Integer,Object> result = (Map<Integer,Object>) yaml.load(ios);
            Map<Integer,T> outMap = new HashMap<>();

            for (Entry<Integer,Object> entry : result.entrySet()) {
                T mappedValue = mapper.map(entry.getValue());
                if (mappedValue != null) {
                    outMap.put(entry.getKey(), mappedValue);
                }
            }
            return outMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
