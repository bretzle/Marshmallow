package marshmallow.audio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum DJGuildLevel {

    NORMAL("Normal", 0, 10),
    ALL("All", 1, 25),
    NONE("None", 2, 0);

    private static final List<String> NAMES = new ArrayList<>();

    static {
        for (DJGuildLevel level : values()) {
            NAMES.add(level.getName());
        }
    }

    private final String name;
    private final int id;
    private final int level;

    DJGuildLevel(String name, int id, int level) {
        this.name = name;
        this.id = id;
        this.level = level;
    }

    public static DJGuildLevel fromName(String name) {
        for (DJGuildLevel level : values()) {
            if (level.getName().equalsIgnoreCase(name)) {
                return level;
            }
        }
        return null;
    }

    public static DJGuildLevel fromId(int id) {
        for (DJGuildLevel level : values()) {
            if (level.getId() == id) {
                return level;
            }
        }
        return null;
    }

    public static DJGuildLevel getNormal() {
        return NORMAL;
    }

    public static Collection<String> getNames() {
        return Collections.unmodifiableCollection(NAMES);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
