package marshmallow.database;

import com.mongodb.*;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;

@Slf4j
public class Database {

    private final DatabaseManager databaseManager;
    private final MongoClient mongoClient;
    private final DB db;
    private final String database;
    private final String host;
    private final String port;

    public Database(DatabaseManager dbm) throws UnknownHostException {
        this.databaseManager = dbm;

        database = dbm.getMarshmallow().getConfig().getString("database.database");
        host = dbm.getMarshmallow().getConfig().getString("database.host");
        port = dbm.getMarshmallow().getConfig().getString("database.port");

        mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        db = mongoClient.getDB(database);
    }

    public DBCollection getCollection(String key) {
        return db.getCollection(key);
    }

    public DBObject getObject(DBCollection collection, String key) {
        return collection.find(new BasicDBObject("id", key)).one();
    }

    public void putObject(DBCollection collection, DBObject object) {
        collection.insert(object);
    }
}
