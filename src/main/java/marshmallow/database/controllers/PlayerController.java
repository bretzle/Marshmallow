package marshmallow.database.controllers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.database.collection.DataRow;
import marshmallow.database.transformers.PlayerTransformer;
import marshmallow.util.CacheUtil;
import marshmallow.util.DatabaseAdapter;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PlayerController {

    public static final Cache<String, PlayerTransformer> cache = CacheBuilder.newBuilder()
            .recordStats()
            .expireAfterAccess(210, TimeUnit.SECONDS) // 3½ minute
            .build();

    private static final Map<Long, PlayerUpdateReference> playerQueue = new LinkedHashMap<>();

    @CheckReturnValue
    public static PlayerTransformer fetchPlayer(Marshmallow marshmallow, Message message) {
        return fetchPlayer(marshmallow, message, message.getAuthor());
    }

    @CheckReturnValue
    public static PlayerTransformer fetchPlayer(Marshmallow marshmallow, Message message, User user) {
        if (!message.getChannelType().isGuild()) return null;

        return (PlayerTransformer) CacheUtil.getUncheckedUnwrapped(cache, asKey(message.getGuild(), user), () -> loadPlayerFromDatabase(marshmallow, message, user));
    }

    private static String asKey(@Nonnull Guild guild, @Nonnull User user) {
        return guild.getId() + ":" + user.getId();
    }

    private static PlayerTransformer loadPlayerFromDatabase(Marshmallow marshmallow, Message message, User user) {
        log.debug("User cache for " + user.getId() + " was refreshed");

        String userID = user.getId();

        try {
            return new PlayerTransformer(user.getIdLong(), message.getGuild().getIdLong(), new DataRow(DatabaseAdapter.getPlayer(userID)));
        } catch (NullPointerException e) {
            log.info("Creating entry for Player:" + userID + " in Guild: " + message.getGuild().getId());
            DatabaseAdapter.insertNewPlayer(user, message.getGuild());
        }
        return new PlayerTransformer(user.getIdLong(), message.getGuild().getIdLong(), new DataRow(DatabaseAdapter.getPlayer(userID)));
    }

    public static class PlayerUpdateReference {

        private final String username;
        private final String discriminator;
        private final String avatar;

        PlayerUpdateReference(@Nonnull User user) {
            this.username = "base64:" + new String(
                    Base64.getEncoder().encode(user.getName().getBytes())
            );
            this.discriminator = user.getDiscriminator();
            this.avatar = user.getAvatarId();
        }

        public String getUsername() {
            return username;
        }

        public String getDiscriminator() {
            return discriminator;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
