package marshmallow.config.yaml;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Arrays;
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

    public YamlConfiguration(Reader reader) {
        data = new Yaml().load(reader);
    }

    public Map<String, Object> getRawData() {
        return data;
    }

    public Object get(String key) {
        List<String> keys = Arrays.asList(key.split("[.]"));
        Object value = null;

        for (String subKey : keys) {
            if (subKey.equals(keys.get(0))) {
                value = data.get(subKey);
            } else {
                value = ((Map) value).get(subKey);
            }
        }

        return value;
    }

    public String getString(String key) {
        Object obj = get(key);
        return (String) obj;
    }

    public String getString(String key, String fallback) {
        try {
            return getString(key);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }

    public List<Object> getList(String key) {
        Object obj = get(key);

        if (obj instanceof List) {
            return (List) obj;
        }
        throw new IllegalArgumentException("The given key does not exist or is not a list");
    }

    public boolean contains(String key) {
        try {
            get(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
