package marshmallow.middleware;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import marshmallow.Marshmallow;
import marshmallow.util.CacheUtil;
import net.dv8tion.jda.core.entities.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public abstract class Middleware {

    public static final Cache<Long, Boolean> messageCache = CacheBuilder.newBuilder()
            .recordStats()
            .expireAfterWrite(2500, TimeUnit.MILLISECONDS)
            .build();

    protected final Marshmallow avaire;

    public Middleware(Marshmallow avaire) {
        this.avaire = avaire;
    }

    @Nullable
    public String buildHelpDescription(@Nonnull String[] arguments) {
        return null;
    }

    public abstract boolean handle(@Nonnull Message message, @Nonnull MiddlewareStack stack, String... args);

    protected boolean runMessageCheck(@Nonnull Message message, @Nonnull Callable<Boolean> callback) {
        return (boolean) CacheUtil.getUncheckedUnwrapped(messageCache, message.getAuthor().getIdLong(), callback);
    }
}
