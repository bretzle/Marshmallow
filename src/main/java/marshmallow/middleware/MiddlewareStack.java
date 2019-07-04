package marshmallow.middleware;

import marshmallow.Marshmallow;
import marshmallow.commands.Command;
import marshmallow.commands.CommandContainer;
import marshmallow.handlers.DatabaseEventHolder;
import marshmallow.middleware.global.IsCategoryEnabled;
import marshmallow.middleware.global.ProcessCommand;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MiddlewareStack {

    private static ProcessCommand processCommand;
    private static IsCategoryEnabled isCategoryEnabled;

    private final Message message;
    private final CommandContainer command;
    private final List<MiddlewareContainer> middlewares = new ArrayList<>();
    private final DatabaseEventHolder databaseEventHolder;
    private final boolean mentionableCommand;

    private int index = -1;

    public MiddlewareStack(Message message, CommandContainer command, DatabaseEventHolder databaseEventHolder) {
        this(message,command,databaseEventHolder, false);
    }

    public MiddlewareStack(Message message, CommandContainer command, DatabaseEventHolder databaseEventHolder, boolean mentionableCommand) {
        this.message = message;
        this.command = command;
        this.mentionableCommand = mentionableCommand;
        this.databaseEventHolder = databaseEventHolder;

        middlewares.add(new MiddlewareContainer(processCommand));

        buildMiddlewareStack();

        middlewares.add(new MiddlewareContainer(isCategoryEnabled));
    }

    static void buildGlobalMiddlewares(Marshmallow marshmallow) {
        processCommand = new ProcessCommand(marshmallow);
        isCategoryEnabled = new IsCategoryEnabled(marshmallow);
    }

    private void buildMiddlewareStack() {
        List<String> middleware = command.getMiddleware();
        if (middleware.isEmpty()) {
            return;
        }

        ListIterator middlewareIterator = middleware.listIterator(middleware.size());
        while (middlewareIterator.hasPrevious()) {
            String previous = (String) middlewareIterator.previous();
            String[] split = previous.split(":");

            Middleware middlewareReference = MiddlewareManager.getMiddleware(split[0]);
            if (middlewareReference == null) {
                continue;
            }

            if (split.length == 1) {
                middlewares.add(new MiddlewareContainer(middlewareReference));
                continue;
            }
            middlewares.add(new MiddlewareContainer(middlewareReference, split[1].split(",")));
        }
    }

    public boolean next() {
        if (index == -1) {
            index = middlewares.size();
        }

        MiddlewareContainer middlewareContainer = middlewares.get(--index);

        return middlewareContainer
                .getMiddleware()
                .handle(message, this, middlewareContainer.getArguments());
    }

    public Command getCommand() {
        return command.getCommand();
    }

    public CommandContainer getCommandContainer() {
        return command;
    }

    public boolean isMentionableCommand() {
        return mentionableCommand;
    }

    public DatabaseEventHolder getDatabaseEventHolder() {
        return databaseEventHolder;
    }
}
