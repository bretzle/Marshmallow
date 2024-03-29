package marshmallow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import lombok.extern.slf4j.Slf4j;
import marshmallow.admin.BotAdmin;
import marshmallow.commands.CategoryManager;
import marshmallow.commands.Command;
import marshmallow.commands.CommandManager;
import marshmallow.config.Configuration;
import marshmallow.config.ConstantsConfig;
import marshmallow.database.DatabaseManager;
import marshmallow.handlers.MainEventHandler;
import marshmallow.gui.ConsoleColor;
import marshmallow.language.I18n;
import marshmallow.middleware.MiddlewareManager;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import net.dv8tion.jda.core.utils.cache.CacheFlag;
import org.reflections.Reflections;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
public class Marshmallow {

    protected static Marshmallow marshmallow;
    private final Settings settings;
    private final Configuration config;
    private final ConstantsConfig constants;
    private final DatabaseManager database;
    private final BotAdmin botAdmins;
    private ShardManager shardManager = null;
    private static Environment applicationEnvironment;

    public static final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .serializeNulls()
            .create();

    public Marshmallow(Settings settings) {
        this.settings = settings;

        System.out.println(getVersionInfo());

        log.debug("====================================================");
        log.debug("Starting the application with debug logging enabled!");
        log.debug("====================================================\n");

        log.info("Bootstrapping Marshmallow v" + AppInfo.getAppInfo().version);
        Reflections.log = null;

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

        applicationEnvironment = Environment.fromName(config.getString("environment", Environment.PRODUCTION.getName()));
        if (applicationEnvironment == null) {
            throw new IllegalArgumentException("Environment must be development or production");
        }
        log.info("Starting application in \"{}\" mode", applicationEnvironment.getName());
        if (applicationEnvironment.equals(Environment.DEVELOPMENT)) {
            RestAction.setPassContext(true);
            log.info("Enabling rest action context parsing and printing stack traces.");
        }

        log.info("Registering and connecting to database");
        database = new DatabaseManager(this);
        database.connect();

        log.info("Registering default middlewares");
        MiddlewareManager.initialize(this);

        log.info("Registering default command categories");

        String defaultPrefix = config.getString("default-prefix", Constants.DEFAULT_COMMAND_PREFIX);
        String systemPrefix = config.getString("system-prefix", Constants.DEFAULT_SYSTEM_PREFIX);

        CategoryManager.addCategory(this, "Administration", defaultPrefix);
        CategoryManager.addCategory(this, "Help", defaultPrefix);
        CategoryManager.addCategory(this, "Fun", defaultPrefix);
        CategoryManager.addCategory(this, "Interaction", defaultPrefix);
        CategoryManager.addCategory(this, "Music", defaultPrefix);
        CategoryManager.addCategory(this, "Search", defaultPrefix);
        CategoryManager.addCategory(this, "Utility", defaultPrefix);
        CategoryManager.addCategory(this, "System", systemPrefix);

        log.info("Registering commands...");
        loadPackage("marshmallow.commands", CommandManager::register);
        log.info("Registered {} commands successfully!", CommandManager.getCommands().size());

        log.info("Preparing I18n");
        I18n.start(this);

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

    public Settings getSettings() {
        return settings;
    }

    public DatabaseManager getDatabase() {
        return database;
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
                .setShardsTotal(settings.getShardCount())
                .addEventListeners(new MainEventHandler(this));

        return builder.build();
    }

    private void loadPackage(String path, Consumer<Command> callback) {
        Set<Class<? extends Command>> types = new Reflections(path).getSubTypesOf(Command.class);

        Class[] arguments = new Class[1];
        arguments[0] = Marshmallow.class;

        for (Class<? extends Command> clazz : types) {
            try {
                callback.accept(clazz.getDeclaredConstructor(arguments).newInstance(this));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("Failed to create a new instance of package {}", clazz.getName(), e);
            }
        }
    }
}
