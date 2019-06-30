package marshmallow.database;

import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.exceptions.DatabaseException;

import java.net.UnknownHostException;

@Slf4j
public class DatabaseManager {

    private final Marshmallow marshmallow;
    private Database connection = null;

    public DatabaseManager(Marshmallow marshmallow) {
        this.marshmallow = marshmallow;
    }

    public boolean connect() {
        try {
            if (connection == null) {
                connection = new Database(this);
            }
        } catch (UnknownHostException e) {
            throw new DatabaseException("Could not connect to the mongoDB", e);
        }
        return true;
    }

    public Marshmallow getMarshmallow() {
        return marshmallow;
    }
}
