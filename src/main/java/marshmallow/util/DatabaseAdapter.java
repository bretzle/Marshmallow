package marshmallow.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import marshmallow.database.Database;
import marshmallow.database.DatabaseManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseAdapter {

    private static DatabaseManager dbm;
    private static Database database;

    public static void injectDatabase(DatabaseManager dbm) {
        DatabaseAdapter.dbm = dbm;
        DatabaseAdapter.database = dbm.getConnection();
    }

    public static Map<String, Object> getGuild(Guild guild) throws NullPointerException {
        return getGuild(guild.getId());
    }

    public static Map<String, Object> getGuild(String guildID) throws NullPointerException {
        DBCollection collection = database.getCollection("guilds");
        DBObject query = new BasicDBObject("_id", guildID);

        return collection.find(query).one().toMap();
    }

    public static Map<String, Object> getPlayer(String userID) throws NullPointerException {
        DBCollection collection = database.getCollection("players");
        DBObject query = new BasicDBObject("_id", userID);

        return collection.find(query).one().toMap();
    }

    public static void insertNewGuild(Guild guild) {
        TreeMap<String, Object> items = generateGuild();

        items.put("_id", guild.getId());
        items.put("name", guild.getName());

        DBObject object = new BasicDBObject(items);

        database.getCollection("guilds").insert(object);
    }

    public static void insertNewGuild(String guildID) {
        TreeMap<String, Object> items = generateGuild();

        items.put("_id", guildID);

        DBObject object = new BasicDBObject(items);

        database.getCollection("guilds").insert(object);
    }

    public static void insertNewPlayer(User user, Guild guild) {
        Map<String, Object> playerEntry = generatePlayer(user, guild);

        DBObject object = new BasicDBObject(playerEntry);

        database.getCollection("players").insert(object);
    }

    private static TreeMap<String, Object> generateGuild() {
        TreeMap<String, Object> items = new TreeMap<>();

        items.put("_id", "");
        items.put("name", "");
        items.put("locale", "EN_US");
        items.put("levels", true);
        items.put("level_alerts", false);
        items.put("hierarchy", false);
        items.put("level_channel", "");
        items.put("level_modifier", 1);
        items.put("autorole", "");
        items.put("modlog", "");
        items.put("music_channel_text", "");
        items.put("music_channel_voice", "");
        items.put("music_messages", true);
        items.put("modlog_case", 1);
        items.put("dj_level", 1);
        items.put("default_volume", 100);
        items.put("partner", false);
        items.put("aliases", new ArrayList<>());
        items.put("prefixes", new ArrayList<>());
        items.put("claimable_roles", new ArrayList<>());
        items.put("level_roles", new ArrayList<>());
        items.put("level_exempt_channels", new ArrayList<>());
        items.put("modules", new ArrayList<>());
        items.put("channels", new ArrayList<>());

        return items;
    }

    private static TreeMap<String, Object> generatePlayer(User user, Guild guild) {
        TreeMap<String, Object> map = new TreeMap<>();
        TreeMap<String, Object> experienceMap = new TreeMap<>();

        experienceMap.put("id", guild.getId());
        experienceMap.put("value", 100);

        map.put("_id", user.getId());
        map.put("username", user.getName());
        map.put("discriminator", user.getDiscriminator());
        map.put("avatar", user.getAvatarId());
        map.put("active", true);
        map.put("experience", Arrays.asList(experienceMap));

        return map;
    }
}
