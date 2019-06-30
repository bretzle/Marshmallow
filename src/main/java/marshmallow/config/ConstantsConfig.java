package marshmallow.config;

import marshmallow.Constants;
import marshmallow.Marshmallow;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class ConstantsConfig extends Configuration {

    private final HashMap<String, Long> cache = new HashMap<>();

    public ConstantsConfig(Marshmallow marshmallow) {
        super(marshmallow, null, "constants.yml");
    }

    public long getFeedbackChannelId() {
        return loadProperty("feedback-channel", Constants.FEEDBACK_CHANNEL_ID);
    }

    public long getChangelogChannelId() {
        return loadProperty("changelog-channel", Constants.CHANGELOG_CHANNEL_ID);
    }

    public long getActivityLogChannelId() {
        return loadProperty("activity-log-channel", Constants.ACTIVITY_LOG_CHANNEL_ID);
    }

    public long getBotAdminExceptionRoleId() {
        return loadProperty("bot-admin-exception-role", Constants.BOT_ADMIN_EXCEPTION_ROLE);
    }

    @Override
    public void reload() {
        super.reload();
        cache.clear();
    }

    private long loadProperty(@Nonnull String key, long fallback) {
        if (!cache.containsKey(key)) {
            long val = getLong(key, fallback);
            cache.put(key, val > 0 ? val : fallback);
        }
        return cache.get(key);
    }
}
