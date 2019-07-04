package marshmallow.middleware.global;

import marshmallow.Marshmallow;
import marshmallow.commands.CommandManager;
import marshmallow.database.transformers.ChannelTransformer;
import marshmallow.database.transformers.GuildTransformer;
import marshmallow.middleware.Middleware;
import marshmallow.middleware.MiddlewareStack;
import marshmallow.util.RestActionUtil;
import net.dv8tion.jda.core.entities.Message;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class IsCategoryEnabled extends Middleware {

    public IsCategoryEnabled(Marshmallow marshmallow) {
        super(marshmallow);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean handle(@Nonnull Message message, @Nonnull MiddlewareStack stack, String... args) {
        if (!message.getChannelType().isGuild()) {
            return stack.next();
        }

        if (isCategoryCommands(stack) || stack.getCommandContainer().getCategory().isGlobalOrSystem()) {
            return stack.next();
        }

        GuildTransformer transformer = stack.getDatabaseEventHolder().getGuild();
        if (transformer == null) {
            return stack.next();
        }

        ChannelTransformer channel = transformer.getChannel(message.getChannel().getId());
        if (channel == null) {
            return stack.next();
        }

        if (!channel.isCategoryEnabled(stack.getCommandContainer().getCategory())) {
            if (isHelpCommand(stack) && stack.isMentionableCommand()) {
//                MessageFactory.makeError(message, "The help command is disabled in this channel, you can enable it by using the `:category` command.")
//                        .set("category", CommandManager.getCommand(ToggleCategoryCommand.class).getCommand().generateCommandTrigger(message))
//                        .queue(success -> success.delete().queueAfter(15, TimeUnit.SECONDS, null, RestActionUtil.ignore));
//                todo
            }

            return false;
        }

        return stack.next();
    }

    private boolean isCategoryCommands(MiddlewareStack stack) {
        return stack.getCommand().getClass().getTypeName().equals("com.avairebot.commands.administration.ToggleCategoryCommand") ||
                stack.getCommand().getClass().getTypeName().equals("com.avairebot.commands.administration.CategoriesCommand");
    }

    private boolean isHelpCommand(MiddlewareStack stack) {
        return stack.getCommand().getClass().getTypeName().equals("com.avairebot.commands.help.HelpCommand");
    }
}
