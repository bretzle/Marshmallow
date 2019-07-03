package marshmallow.util;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CacheUtil {

    public static <K, V> Object getUncheckedUnwrapped(@Nonnull Cache<K, V> cache, @Nonnull K key, @Nonnull Callable<V> loader) {
        try {
            return cache.get(key, loader);
        } catch (ExecutionException e) {
            throw new RuntimeException("Cache loader threw exception", e);
        } catch (UncheckedExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());

            // Will never run.
            throw new IllegalStateException(e);
        }
    }

    public static <K, V> V getUncheckedUnwrapped(@Nonnull LoadingCache<K, V> cache, @Nonnull K key) {
        try {
            return cache.getUnchecked(key);
        } catch (UncheckedExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());

            // Will never run.
            throw new IllegalStateException(e);
        }
    }
}
