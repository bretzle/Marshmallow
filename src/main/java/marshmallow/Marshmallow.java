package marshmallow;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import lombok.extern.slf4j.Slf4j;
import marshmallow.gui.ConsoleColor;
import net.dv8tion.jda.core.JDAInfo;

@Slf4j
public class Marshmallow {

    protected static Marshmallow marshmallow;
    private final Settings settings;

    public Marshmallow(Settings settings) {
        this.settings = settings;

        System.out.println(getVersionInfo());

        log.debug("====================================================");
        log.debug("Starting the application with debug logging enabled!");
        log.debug("====================================================\n");
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
}
