package marshmallow.middleware.global;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.commands.AliasCommandContainer;
import marshmallow.commands.Context;
import marshmallow.factories.MessageFactory;
import marshmallow.gui.ConsoleColor;
import marshmallow.middleware.Middleware;
import marshmallow.middleware.MiddlewareStack;
import marshmallow.util.ArrayUtil;
import marshmallow.util.CheckPermissionUtil;
import marshmallow.util.RestActionUtil;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProcessCommand extends Middleware {

    private final static String commandOutput = ConsoleColor.format(
            "%cyanExecuting Command \"%reset%command%%cyan\" in \"%reset%category%%cyan\" category in shard %reset%shard%:%reset"
                    + "\n\t\t%cyanUser:\t %author%"
                    + "\n\t\t%cyanServer:\t %server%"
                    + "\n\t\t%cyanChannel: %channel%"
                    + "\n\t\t%cyanMessage: %reset%message%");

    private final static String propertyOutput = ConsoleColor.format(
            "%reset%s %cyan[%reset%s%cyan]"
    );

    public ProcessCommand(Marshmallow marshmallow) {
        super(marshmallow);
    }

    @Override
    public boolean handle(@Nonnull Message message, @Nonnull MiddlewareStack stack, String... args) {
        CheckPermissionUtil.PermissionCheckType permissionType = CheckPermissionUtil.canSendMessages(message.getChannel());
        if (!stack.getCommandContainer().getCategory().getName().equals("System") && !permissionType.canSendEmbed()) {
            if (!permissionType.canSendMessage()) {
                return false;
            }

            return runMessageCheck(message, () -> {
                message.getTextChannel().sendMessage("I don't have the `Embed Links` permission, the permission is required for commands to work.")
                        .queue(newMessage -> newMessage.delete().queueAfter(30, TimeUnit.SECONDS, null, RestActionUtil.ignore));

                return false;
            });
        }

        String[] arguments = ArrayUtil.toArguments(message.getContentRaw());

        Marshmallow.getLogger().info(commandOutput
                .replace("%command%", stack.getCommand().getName())
                .replace("%category%", stack.getCommandContainer().getCategory().getName())
                .replace("%author%", generateUsername(message))
                .replace("%server%", generateServer(message))
                .replace("%channel%", generateChannel(message))
                .replace("%message%", message.getContentRaw())
                .replace("%shard%", message.getJDA().getShardInfo().getShardString())
        );

        try {
            String[] commandArguments = Arrays.copyOfRange(arguments, stack.isMentionableCommand() ? 1 : 0, arguments.length);
            if (stack.getCommandContainer() instanceof AliasCommandContainer) {
                AliasCommandContainer container = (AliasCommandContainer) stack.getCommandContainer();
                Context context = new Context(
                        stack.getCommandContainer(),
                        stack.getDatabaseEventHolder(),
                        message,
                        stack.isMentionableCommand(),
                        container.getAliasArguments()
                );
                return runCommand(stack, context, combineArguments(container.getAliasArguments(), commandArguments));
            }
            Context context = new Context(
                    stack.getCommandContainer(),
                    stack.getDatabaseEventHolder(),
                    message,
                    stack.isMentionableCommand(),
                    new String[0]
            );
            return runCommand(stack, context, commandArguments);
        } catch (Exception e) {
            if (e instanceof InsufficientPermissionException) {
                MessageFactory.makeError(message, "Error: " + e.getMessage())
                        .queue(newMessage -> newMessage.delete().queueAfter(30, TimeUnit.SECONDS, null, RestActionUtil.ignore));

                return false;
            } else if (e instanceof FriendlyException) {
                MessageFactory.makeError(message, "Error: " + e.getMessage())
                        .queue(newMessage -> newMessage.delete().queueAfter(30, TimeUnit.SECONDS, null, RestActionUtil.ignore));
            }

//            MDC.putCloseable(SentryConstants.SENTRY_MDC_TAG_GUILD, message.getGuild() != null ? message.getGuild().getId() : "PRIVATE");
//            MDC.putCloseable(SentryConstants.SENTRY_MDC_TAG_SHARD, message.getJDA().getShardInfo().getShardString());
//            MDC.putCloseable(SentryConstants.SENTRY_MDC_TAG_CHANNEL, message.getChannel().getId());
//            MDC.putCloseable(SentryConstants.SENTRY_MDC_TAG_AUTHOR, message.getAuthor().getId());
//            MDC.putCloseable(SentryConstants.SENTRY_MDC_TAG_MESSAGE, message.getContentRaw());
            log.error("An error occurred while running the " + stack.getCommand().getName(), e);
            return false;
        }
    }

    private boolean runCommand(MiddlewareStack stack, Context message, String[] args) {
        return stack.getCommand().onCommand(message, args);
    }

    private String generateUsername(Message message) {
        return String.format(propertyOutput,
                message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator(),
                message.getAuthor().getId()
        );
    }

    private String generateServer(Message message) {
        if (!message.getChannelType().isGuild()) {
            return "PRIVATE";
        }

        return String.format(propertyOutput,
                message.getGuild().getName(),
                message.getGuild().getId()
        );
    }

    private CharSequence generateChannel(Message message) {
        if (!message.getChannelType().isGuild()) {
            return "PRIVATE";
        }

        return String.format(propertyOutput,
                message.getChannel().getName(),
                message.getChannel().getId()
        );
    }

    private String[] combineArguments(String[] aliasArguments, String[] userArguments) {
        int length = aliasArguments.length + userArguments.length;

        String[] result = new String[length];

        System.arraycopy(aliasArguments, 0, result, 0, aliasArguments.length);
        System.arraycopy(userArguments, 0, result, aliasArguments.length, userArguments.length);

        return result;
    }
}
