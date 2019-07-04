package marshmallow.database.controllers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.database.collection.DataRow;
import marshmallow.database.transformers.GuildTransformer;
import marshmallow.util.CacheUtil;
import marshmallow.util.DatabaseAdapter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import javax.annotation.CheckReturnValue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GuildController {

    public static final Cache<Long, GuildTransformer> cache = CacheBuilder.newBuilder()
            .recordStats()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    @CheckReturnValue
    public static GuildTransformer fetchGuild(Marshmallow marshmallow, Message message) {
        if (!message.getChannelType().isGuild()) {
            return null;
        }
        return fetchGuild(marshmallow, message.getGuild());
    }

    @CheckReturnValue
    public static GuildTransformer fetchGuild(Marshmallow marshmallow, Guild guild) {
        return (GuildTransformer) CacheUtil.getUncheckedUnwrapped(cache, guild.getIdLong(), () -> loadGuildFromDatabase(marshmallow, guild));
    }

    public static void forgetCache(long guildId) {
        cache.invalidate(guildId);
    }

    private static GuildTransformer loadGuildFromDatabase(Marshmallow marshmallow, Guild guild) {
        log.debug("Guild cache for " + guild.getId() + " was refreshed");

        try {
            return new GuildTransformer(guild, new DataRow(DatabaseAdapter.getGuild(guild)));
        } catch (NullPointerException e) {
            log.info("Guild: " + guild.getId() + " does not exist. Generating now...");
            DatabaseAdapter.insertNewGuild(guild);
        }
        return new GuildTransformer(guild, new DataRow(DatabaseAdapter.getGuild(guild)));
    }
}
