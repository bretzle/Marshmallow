package marshmallow.config.yaml;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfiguration {

    private Map<String, Object> data;

    public YamlConfiguration(File file) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getName());
        }
        data = new Yaml().load(inputStream);
    }

    public Object get(String key) {
        return get(data, key);
    }

    private Object get(Map<String, Object> map, String key) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
            if (entry.getValue() instanceof Map) {
                return get((Map<String, Object>) entry.getValue(), key);
            }
        }
        return null;
    }

}
