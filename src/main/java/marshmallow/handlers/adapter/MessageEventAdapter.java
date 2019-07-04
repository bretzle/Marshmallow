package marshmallow.handlers.adapter;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.commands.CommandContainer;
import marshmallow.commands.CommandManager;
import marshmallow.database.controllers.GuildController;
import marshmallow.database.controllers.PlayerController;
import marshmallow.database.transformers.GuildTransformer;
import marshmallow.handlers.DatabaseEventHolder;
import marshmallow.middleware.MiddlewareStack;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MessageEventAdapter {

    private static final ExecutorService commandService = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setNameFormat("marshmallow-command-thread-%d")
                    .build()
    );

    private final Marshmallow marshmallow;

    public MessageEventAdapter(Marshmallow marshmallow) {
        this.marshmallow = marshmallow;
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getChannelType().isGuild() && !event.getTextChannel().canTalk()) return;

        log.info("received event: " + event.toString());

        loadDatabasePropertiesIntoMemory(event).thenAccept(databaseEventHolder -> {
            if (databaseEventHolder.getGuild() != null && databaseEventHolder.getPlayer() != null) {
                // todo give experience for message
            }

            CommandContainer container = CommandManager.getCommand(marshmallow, event.getMessage(), event.getMessage().getContentRaw());
            if (container!=null&& canExecuteCommand(event, container)) {
                invokeMiddleWareStack(new MiddlewareStack(event.getMessage(), container, databaseEventHolder, true));
                return;
            }
        });
    }

    private boolean canExecuteCommand(MessageReceivedEvent event, CommandContainer container) {
        return container.getCommand().isAllowDM() || event.getChannelType().isGuild();
    }

    private CompletableFuture<DatabaseEventHolder> loadDatabasePropertiesIntoMemory(final MessageReceivedEvent event) {
        return CompletableFuture.supplyAsync(() -> {
            if (!event.getChannelType().isGuild()) {
                return new DatabaseEventHolder(null, null);
            }

            GuildTransformer guild = GuildController.fetchGuild(marshmallow, event.getMessage());

            if (guild == null || !guild.isLevels() || event.getAuthor().isBot()) {
                return new DatabaseEventHolder(guild, null);
            }

            return new DatabaseEventHolder(guild, PlayerController.fetchPlayer(marshmallow, event.getMessage()));
        });
    }

    private void invokeMiddleWareStack(MiddlewareStack stack) {
        commandService.submit(stack::next);
    }
}
