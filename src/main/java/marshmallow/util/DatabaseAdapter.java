package marshmallow.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import marshmallow.database.Database;
import marshmallow.database.DatabaseManager;
import net.dv8tion.jda.core.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAdapter {

    private static DatabaseManager dbm;
    private static Database database;

    public static void injectDatabase(DatabaseManager dbm) {
        DatabaseAdapter.dbm = dbm;
        DatabaseAdapter.database = dbm.getConnection();
    }

    public static Map<String, Object> getGuild(Guild guild) {
        return getGuild(guild.getId());
    }

    public static Map<String, Object> getGuild(String guildID) throws NullPointerException {
        DBCollection collection = database.getCollection("guilds");
        DBObject query = new BasicDBObject("_id", guildID);

        return collection.find(query).one().toMap();
    }

    public static void insertNewGuild(Guild guild) {
        HashMap<String, Object> items = generateGuild();

        items.put("_id", guild.getId());
        items.put("name", guild.getName());

        DBObject object = new BasicDBObject(items);

        database.getCollection("guilds").insert(object);
    }

    public static void insertNewGuild(String guildID) {
        HashMap<String, Object> items = generateGuild();

        items.put("_id", guildID);

        DBObject object = new BasicDBObject(items);

        database.getCollection("guilds").insert(object);
    }


    private static HashMap<String, Object> generateGuild() {
        HashMap<String, Object> items = new HashMap<>();

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
}
