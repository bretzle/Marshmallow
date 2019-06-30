package marshmallow.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;

@Slf4j
public class Database {

    private DatabaseManager databaseManager;
    private MongoClient mongoClient;
    private DB db;

    public Database(DatabaseManager dbm) throws UnknownHostException {
        this.databaseManager = dbm;

        String database = dbm.getMarshmallow().getConfig().getString("database.database");
        String host = dbm.getMarshmallow().getConfig().getString("database.host");
        String port = dbm.getMarshmallow().getConfig().getString("database.port");

        mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        db = mongoClient.getDB(database);
    }
}
