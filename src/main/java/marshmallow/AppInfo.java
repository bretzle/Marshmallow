package marshmallow;

import java.io.IOException;
import java.util.Properties;

public class AppInfo {

    private static AppInfo instance;

    public final String version;
    public final String groupID;
    public final String artifactID;

    private AppInfo() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            System.out.println("Failed to load app.properties. Check to see if the file exists");
        }

        this.version = properties.getProperty("version");
        this.groupID = properties.getProperty("groupID");
        this.artifactID = properties.getProperty("artifactID");
    }

    public static AppInfo getAppInfo() {
        if (instance == null) {
            instance = new AppInfo();
        }
        return instance;
    }
}
