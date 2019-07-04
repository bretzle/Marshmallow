package marshmallow.database.collection;

import marshmallow.util.NumberUtil;

import javax.annotation.Nonnull;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataRow {

    private final Map<String, Object> items;
    private final Map<String, String> decodedItems;

    public DataRow(DataRow row) {
        this(row.items);
    }

    public DataRow(Map<String, Object> items) {
        this.items = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.decodedItems = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Map.Entry<String, Object> item : items.entrySet()) {
            this.items.put(item.getKey(), item.getValue());
        }
    }

    public Object get(String name) {
        return get(name, null);
    }

    public Object get(String name, Object def) {
        if (has(name)) return items.get(name);
        return def;
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean def) {
        Object value = get(name, def);

        if (isNull(value)) {
            return def;
        }

        if (isString(value)) {
            String str = String.valueOf(value);

            return isEqual(str, "1", "true");
        }

        return (boolean) value;
    }

    public double getDouble(String name) {
        return getDouble(name, 0.0D);
    }

    public double getDouble(String name, double def) {
        Object value = get(name, def);

        if (isNull(value)) {
            return def;
        }

        if (isString(value)) {
            String str = String.valueOf(value);

            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException ex) {
                return def;
            }
        }

        switch (getType(value)) {
            case "Integer":
                value = ((Integer) value).doubleValue();
                break;

            case "Long":
                value = ((Long) value).doubleValue();
                break;

            case "Float":
                value = ((Float) value).doubleValue();
                break;
        }

        try {
            return (double) value;
        } catch (ClassCastException ex) {
            return def;
        }
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public int getInt(String name, int def) {
        Object value = get(name, def);

        if (isNull(value)) {
            return def;
        }

        if (isString(value)) {
            String str = String.valueOf(value);

            return NumberUtil.parseInt(str, def);
        }

        switch (getType(value)) {
            case "Double":
                value = ((Double) value).intValue();
                break;

            case "Long":
                value = ((Long) value).intValue();
                break;

            case "Float":
                value = ((Float) value).intValue();
                break;
        }

        try {
            return (int) value;
        } catch (ClassCastException ex) {
            return def;
        }
    }

    public long getLong(String name) {
        return getLong(name, 0L);
    }

    public long getLong(String name, long def) {
        Object value = get(name, def);

        if (isNull(value)) {
            return def;
        }

        if (isString(value)) {
            String str = String.valueOf(value);

            try {
                return Long.parseLong(str);
            } catch (NumberFormatException ex) {
                return def;
            }
        }

        switch (getType(value)) {
            case "Double":
                value = ((Double) value).longValue();
                break;

            case "Integer":
                value = ((Integer) value).longValue();
                break;

            case "Float":
                value = ((Float) value).longValue();
                break;
        }

        try {
            return (long) value;
        } catch (ClassCastException ex) {
            return def;
        }
    }

    public float getFloat(String name) {
        return getFloat(name, 0F);
    }

    public float getFloat(String name, float def) {
        Object value = get(name, def);

        if (isNull(value)) {
            return def;
        }

        if (isString(value)) {
            String str = String.valueOf(value);

            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException ex) {
                return def;
            }
        }

        switch (getType(value)) {
            case "Double":
                value = ((Double) value).floatValue();
                break;

            case "Integer":
                value = ((Integer) value).floatValue();
                break;

            case "Long":
                value = ((Long) value).floatValue();
                break;
        }

        try {
            return (float) value;
        } catch (ClassCastException ex) {
            return def;
        }
    }

    public String getString(String name) {
        return getString(name, null);
    }

    public String getString(String name, String def) {
        Object value = get(name, def);

        if (isNull(value)) {
            return def;
        }

        String string = String.valueOf(value);
        if (!string.startsWith("base64:")) {
            return string;
        }

        if (decodedItems.containsKey(name)) {
            return decodedItems.get(name);
        }

        try {
            String decodedString = new String(Base64.getDecoder().decode(
                    string.substring(7)
            ));
            decodedItems.put(name, decodedString);

            return decodedString;
        } catch (IllegalArgumentException ex) {
            return string;
        }
    }

    public boolean has(String name) {
        return items.containsKey(name);
    }

    public Set<String> keySet() {
        return items.keySet();
    }

    public Map<String, Object> getRaw() {
        return items;
    }

    @Override
    public String toString() {
        return items.toString();
    }

    private boolean isString(Object name) {
        return getType(name).equalsIgnoreCase("string");
    }

    private boolean isNull(Object object) {
        return object == null || object == "null";
    }

    @Nonnull
    private String getType(Object name) {
        return name == null ? "unknown-type" : name.getClass().getSimpleName();
    }

    private boolean isEqual(String name, String... items) {
        for (String item : items) {
            if (name.equalsIgnoreCase(item)) {
                return true;
            }
        }

        return false;
    }
}
