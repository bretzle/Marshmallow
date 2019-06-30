package marshmallow.config.yaml;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class YamlConfigurationTest {

    private YamlConfiguration yaml = new YamlConfiguration(new File("testYaml.yml"));
    private List<String> list = Arrays.asList("first", "second", "third");

    @Test
    public void testSimple() {
        assertEquals("im just a value", yaml.get("simpleString"));
        assertEquals(12, yaml.get("simpleInt"));
        assertEquals(548589935159017473L, yaml.get("simpleLong"));
        assertEquals(true, yaml.get("simpleBoolean"));

        assertThrows(IllegalArgumentException.class, () -> yaml.get("invalidKey"));
    }

    @Test
    public void testList() {
        assertEquals(list, yaml.getList("someList"));
    }

    @Test
    public void testNested() {
        assertEquals("im nested", yaml.get("nested.nestedValue"));
        assertEquals("im a token", yaml.get("nested.someToken"));
        assertEquals(list, yaml.getList("nested.nestedList"));

        assertThrows(IllegalArgumentException.class, () -> yaml.get("nested nestedValue"));
        assertThrows(IllegalArgumentException.class, () -> yaml.get("simpleString.nestedValue"));
    }
}