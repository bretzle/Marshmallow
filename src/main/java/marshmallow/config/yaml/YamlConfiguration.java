package marshmallow.config.yaml;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
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
        String[] keys = key.split("[.]");

        if (key.contains(" ")) {
            throw new IllegalArgumentException("A key cannot contain whitespace");
        }

        try {
            if (keys.length == 1) {
                if (data.containsKey(key))
                    return data.get(key);
                else
                    throw new IllegalArgumentException("The given key is invalid");
            } else {
                Map<String, Object> nested = (Map<String, Object>) data.get(keys[0]);
                for (int i = 0; i < keys.length - 2; i++) {
                    nested = (Map<String, Object>) nested.get(keys[i]);
                }
                return nested.get(keys[keys.length - 1]);
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The given key is invalid");
        }
    }

    public Map<String, Object> getRawData() {
        return data;
    }

    public List<Object> getList(String key) {
        Object obj = get(key);

        if (obj instanceof List) {
            return (List) obj;
        }
        throw new IllegalArgumentException("The given key does not exist or is not a list");
    }
}
