package pw.redalliance.MarketTree.Updater.YAML;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YAMLBsdReader {
    static public <KeyT, ValT> Map<KeyT, ValT> readMap(String fileName, YAMLBsdMapper<? extends KeyT, ? extends ValT> mapper) {
        Yaml yaml = new Yaml();
        try {
            InputStream ios = new FileInputStream(new File(fileName));
            List<Map<String, Object> > result = (List<Map<String, Object> >) yaml.load(ios);
            Map<KeyT, ValT> outMap = new HashMap<>();

            for (Map<String, Object> entry : result) {
                if (mapper.map(entry)) {
                    outMap.put(mapper.getKey(), mapper.getValue());
                }
            }
            return outMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
