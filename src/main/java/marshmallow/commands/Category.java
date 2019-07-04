package marshmallow.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import marshmallow.Marshmallow;
import net.dv8tion.jda.core.entities.Message;

import java.util.concurrent.TimeUnit;

public class Category {

    public static final Cache<Object, Object> cache = CacheBuilder.newBuilder()
            .recordStats()
            .expireAfterWrite(2500, TimeUnit.MILLISECONDS)
            .build();

    private final Marshmallow marshmallow;
    private final String name;
    private final String prefix;

    private boolean isGlobal = false;

    public Category(Marshmallow marshmallow, String name, String prefix) {
        this.marshmallow = marshmallow;
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

//    public String getPrefix(@Nonnull Message message) {
//        if (isGlobal) {
//            return getPrefix();
//        }
//
//        if (message.getGuild() == null) {
//            return getPrefix();
//        }
//
//        return (String) CacheUtil.getUncheckedUnwrapped(cache, asKey(message), () -> {
//            GuildTransformer transformer = GuildController.fetchGuild(marshmallow, message);
//
//            return transformer == null ? getPrefix() : transformer.getPrefixes().getOrDefault(
//                    getName().toLowerCase(), getPrefix()
//            );
//        });
//    }

    public boolean hasCommands() {
        return CommandManager.getCommands().stream().anyMatch(container -> container.getCategory().equals(this));
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    Category setGlobal(boolean value) {
        isGlobal = value;
        return this;
    }

    public boolean isGlobalOrSystem() {
        return isGlobal || name.equalsIgnoreCase("system");
    }

    private String asKey(Message message) {
        return message.getGuild().getId() + ":" + name;
    }
}
