package marshmallow;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import lombok.extern.slf4j.Slf4j;
import marshmallow.config.Configuration;
import marshmallow.config.ConstantsConfig;
import marshmallow.gui.ConsoleColor;
import net.dv8tion.jda.core.JDAInfo;
import org.slf4j.Logger;

@Slf4j
public class Marshmallow {

    protected static Marshmallow marshmallow;
    private final Settings settings;
    private final Configuration config;
    private final ConstantsConfig constants;

    public Marshmallow(Settings settings) {
        this.settings = settings;

        System.out.println(getVersionInfo());

        log.debug("====================================================");
        log.debug("Starting the application with debug logging enabled!");
        log.debug("====================================================\n");

        log.info("Bootstrapping Marshmallow v" + AppInfo.getAppInfo().version);

        config = new Configuration(this, null, "config.yml");
        constants = new ConstantsConfig(this);

        if (!config.exists() || !constants.exists()) {
            log.info("The {} or {} configuration files are missing.", "config.yml", "constants.yml");
            log.info("Creating file(s) and terminating program...");

            config.saveDefaultConfig();
            constants.saveDefaultConfig();

            System.exit(Constants.EXIT_CODE_NORMAL);
        }
    }

    private String getVersionInfo() {
        return ConsoleColor.format("%red\n\n" +
                ""
                + "%reset"
                + "\n\tVersion:    " + AppInfo.getAppInfo().version
                + "\n\tJVM:        " + System.getProperty("java.version")
                + "\n\tJDA:        " + JDAInfo.VERSION
                + "\n\tLavaPlayer: " + PlayerLibrary.VERSION
        );
    }

    public static Logger getLogger() {
        return log;
    }
}
