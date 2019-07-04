package marshmallow;

import com.mongodb.*;
import marshmallow.database.collection.DataRow;
import org.apache.commons.cli.*;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        Options options = new Options();

        options.addOption(new Option("h", "help",false,"Displays the help menu."));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            Settings settings = new Settings(cmd, args);

            Marshmallow.marshmallow = new Marshmallow(settings);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("", options);
            System.exit(Constants.EXIT_CODE_NORMAL);
        }
    }


    private static void mongoExample() throws UnknownHostException {
        List<Integer> books = Arrays.asList(27464, 747854);
        DBObject person = new BasicDBObject("_id", "jo")
                .append("name", "Jo Bloggs")
                .append("address", new BasicDBObject("street", "123 Fake St")
                        .append("city", "Faketon")
                        .append("state", "MA")
                        .append("zip", 12345))
                .append("books", books);

        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("Examples");
        DBCollection collection = database.getCollection("people");

        collection.insert(person);

        DBObject query = new BasicDBObject("_id", "jo");
        DBCursor cursor = collection.find(query);
        DBObject jo = cursor.one();

        System.out.println((String)cursor.one().get("name"));
    }

    private static void testGetGuild() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("marshmallow");
        DBCollection collection = database.getCollection("guilds");
        DBObject query = new BasicDBObject("_id", "59618979722847848");

        Map<String, Object> data = collection.find(query).one().toMap();
        DataRow row = new DataRow(data);
        System.out.println(data);
        System.out.println(row);
        System.out.println(row.getBoolean("partner"));
    }
}
