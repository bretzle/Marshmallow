package marshmallow.commands.utility;

import marshmallow.Marshmallow;
import marshmallow.commands.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PingCommand extends Command {

    public PingCommand(Marshmallow marshmallow) {
        super(marshmallow);
    }

    @Override
    public String getName() {
        return "Ping Command";
    }

    @Override
    public String getDescription() {
        return "Can be used to check if the bot is still alive";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("`:command` - Returns the latency of the bot.");
    }

    @Override
    public List<String> getExampleUsage() {
        return null;
    }

    @Override
    public List<Class<? extends Command>> getRelations() {
        return null;
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList("Ping");
    }

    @Override
    public List<String> getMiddleware() {
        return new ArrayList<>();
    }

    @Override
    public Category getCategory() {
        return new Category(marshmallow, "utility", "?");
    }

    @Override
    public List<CommandGroup> getGroups() {
        return Collections.singletonList(CommandGroup.MISCELLANEOUS);
    }

    @Override
    public boolean onCommand(Context context, String[] args) {
        long start = System.currentTimeMillis();
        context.message.getChannel().sendTyping().queue(v -> {
            long ping = System.currentTimeMillis() - start;

            context.makeInfo(context.i18n("message"))
                    .set("heartbeat", context.getJDA().getPing())
                    .set("rating", ratePing(context, ping))
                    .set("ping", ping)
                    .queue();
        });
        return true;
    }

    private String ratePing(Context context, long ping) {
        if (ping <= 10) return context.i18n("rating.best");
        if (ping <= 100) return context.i18n("rating.great");
        if (ping <= 200) return context.i18n("rating.nice");
        if (ping <= 300) return context.i18n("rating.decent");
        if (ping <= 400) return context.i18n("rating.average");
        if (ping <= 500) return context.i18n("rating.sslow");
        if (ping <= 600) return context.i18n("rating.kslow");
        if (ping <= 700) return context.i18n("rating.slow");
        if (ping <= 800) return context.i18n("rating.vslow");
        if (ping <= 900) return context.i18n("rating.bad");
        if (ping <= 1600) return context.i18n("rating.blame");
        if (ping <= 10000) return context.i18n("rating.broken");
        return context.i18n("rating.other");
    }
}
