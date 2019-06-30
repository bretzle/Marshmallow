package marshmallow.config.yaml;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlConfiguration {

    @Getter
    private Map<String, Object> data;

    public YamlConfiguration(String fileName) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        data = new Yaml().load(inputStream);
    }
}
