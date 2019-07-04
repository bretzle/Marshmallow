package marshmallow.database.collection;

import marshmallow.Marshmallow;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Collection implements Cloneable, Iterable<DataRow> {

    private final HashMap<String, String> keys;
    private final List<DataRow> items;

    public Collection() {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public Collection(@Nonnull Collection instance) {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        for (DataRow row : instance.items) {
            items.add(new DataRow(row));
        }
    }

    public Collection(@Nonnull List<Map<String, Object>> items) {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        for (Map<String, Object> row : items) {
            row.keySet().stream().filter(key -> !keys.containsKey(key)).forEach((key) -> {
                keys.put(key, row.get(key).getClass().getTypeName());
            });

            this.items.add(new DataRow(row));
        }
    }

    public Collection(@Nullable ResultSet result) throws SQLException {
        this.keys = new HashMap<>();
        this.items = new ArrayList<>();

        if (result == null) return;

        ResultSetMetaData meta = result.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            keys.put(meta.getColumnLabel(i), meta.getColumnClassName(i));
        }

        while (result.next()) {
            Map<String, Object> array = new HashMap<>();

            for (String key : keys.keySet()) {
                array.put(key, result.getString(key));
            }

            items.add(new DataRow(array));
        }

        if (!result.isClosed()) {
            result.close();
        }
    }

    @NotNull
    @Override
    public Iterator<DataRow> iterator() {
        return new CollectionIterator();
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        return Marshmallow.gson.toJson(items);
    }

    private class CollectionIterator implements Iterator<DataRow> {

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < Collection.this.items.size();
        }

        @Override
        public DataRow next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return Collection.this.items.get(cursor++);
        }
    }
}
