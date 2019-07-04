package marshmallow.middleware;

import marshmallow.Marshmallow;
import net.dv8tion.jda.core.utils.Checks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MiddlewareManager {

    private static final Map<String, Middleware> middlewares = new HashMap<>();

    @Nullable
    public static Middleware getMiddleware(@Nonnull String name) {
        return middlewares.getOrDefault(name.toLowerCase(), null);
    }

    @Nullable
    public static String getName(@Nonnull Class<? extends Middleware> clazz) {
        for (Map.Entry<String, Middleware> middleware : middlewares.entrySet()) {
            if (middleware.getValue().getClass().getSimpleName().equalsIgnoreCase(clazz.getSimpleName())) {
                return middleware.getKey();
            }
        }
        return null;
    }

    public static void register(@Nonnull String name, @Nonnull Middleware middleware) {
        Checks.notNull(name, "Middleware name");
        Checks.notNull(middleware, "Middleware");

        if (middlewares.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException(name + " has already been registered as a middleware");
        }
        middlewares.put(name.toLowerCase(), middleware);
    }

    public static void initialize(Marshmallow marshmallow) {
        MiddlewareStack.buildGlobalMiddlewares(marshmallow);
    }
}
