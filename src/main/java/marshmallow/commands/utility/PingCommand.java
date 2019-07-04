package marshmallow.commands.utility;

import marshmallow.Marshmallow;
import marshmallow.commands.Category;
import marshmallow.commands.Command;
import marshmallow.commands.CommandGroup;
import marshmallow.commands.Context;

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
        return null;
    }

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public List<CommandGroup> getGroups() {
        return Collections.singletonList(CommandGroup.MISCELLANEOUS);
    }

    @Override
    public boolean onCommand(Context context, String[] args) {
//        long start = System.currentTimeMillis();
//        context.message.getChannel().sendTyping().queue(v -> {
//            long ping = System.currentTimeMillis() - start;
//
//            context.makeInfo(context.i18n("message"))
//                    .set("heartbeat", context.getJDA().getPing())
//                    .set("rating", ratePing(context, ping))
//                    .set("ping", ping)
//                    .queue();
//        });
        return true;
    }

    private String ratePing(Context context, long ping) {
//        if (ping <= 10) return context.i18n("rating.10");
//        if (ping <= 100) return context.i18n("rating.100");
//        if (ping <= 200) return context.i18n("rating.200");
//        if (ping <= 300) return context.i18n("rating.300");
//        if (ping <= 400) return context.i18n("rating.400");
//        if (ping <= 500) return context.i18n("rating.500");
//        if (ping <= 600) return context.i18n("rating.600");
//        if (ping <= 700) return context.i18n("rating.700");
//        if (ping <= 800) return context.i18n("rating.800");
//        if (ping <= 900) return context.i18n("rating.900");
//        if (ping <= 1600) return context.i18n("rating.1600");
//        if (ping <= 10000) return context.i18n("rating.10000");
//        return context.i18n("rating.other");
        return "";
    }
}
