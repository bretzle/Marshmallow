package marshmallow;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import lombok.extern.slf4j.Slf4j;
import marshmallow.admin.BotAdmin;
import marshmallow.config.Configuration;
import marshmallow.config.ConstantsConfig;
import marshmallow.gui.ConsoleColor;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import net.dv8tion.jda.core.utils.cache.CacheFlag;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;

@Slf4j
public class Marshmallow {

    protected static Marshmallow marshmallow;
    private final Settings settings;
    private final Configuration config;
    private final ConstantsConfig constants;
    private final BotAdmin botAdmins;
    private ShardManager shardManager = null;

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

        botAdmins = new BotAdmin(this, Collections.unmodifiableSet(new HashSet<>(
                config.getStringList("botAccess")
        )));

        log.info("Added {} Bot Admins.", config.getStringList("botAccess").size());

        try {
            shardManager = buildShardManager();
        } catch (LoginException e) {
            e.printStackTrace();
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

    public Configuration getConfig() {
        return config;
    }

    public ConstantsConfig getConstants() {
        return constants;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    private ShardManager buildShardManager() throws LoginException {
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder()
                .setSessionController(new SessionControllerAdapter())
                .setToken(config.getString("discord.token"))
                .setGame(Game.watching("me boot up..."))
                .setBulkDeleteSplittingEnabled(false)
                .setEnableShutdownHook(false)
                .setAutoReconnect(true)
                .setAudioEnabled(true)
                .setContextEnabled(true)
                .setDisabledCacheFlags(EnumSet.of(CacheFlag.GAME))
                .setShardsTotal(settings.getShardCount());

        return builder.build();
    }
}
