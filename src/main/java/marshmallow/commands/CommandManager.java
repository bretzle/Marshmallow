package marshmallow.commands;

import lombok.extern.slf4j.Slf4j;
import marshmallow.Constants;
import marshmallow.Marshmallow;
import marshmallow.database.controllers.GuildController;
import marshmallow.database.transformers.GuildTransformer;
import marshmallow.exceptions.IllegalCommandException;
import marshmallow.middleware.MiddlewareManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.Checks;

import javax.annotation.Nonnull;
import java.util.*;

@Slf4j
public class CommandManager {

    private static final Set<CommandContainer> COMMANDS = new HashSet<>();

    public static void register(Command command) {
        Category category = CategoryManager.fromCommand(command);
        Checks.notNull(category, String.format("%s :: %s", command.getName(), "Invalid command category, command category"));

//        try {
//            Checks.notNull(command.getDescription(new FakeCommandMessage()), String.format("%s :: %s", command.getName(), "Command description"));
//            Checks.notNull(command.getDescription(null), String.format("%s :: %s", command.getName(), "Command description with null"));
//            Checks.notNull(command.getDescription(), String.format("%s :: %s", command.getName(), "Command description with no arguments"));
//        } catch (StackOverflowError e) {
//            throw new MissingCommandDescriptionException(command);
//        }

        for (String trigger : command.getTriggers()) {
            for (CommandContainer container : COMMANDS) {
                for (String subTrigger : container.getTriggers()) {
                    if (Objects.equals(category.getPrefix()+trigger, container.getDefaultPrefix()+subTrigger)) {
                        throw new IllegalCommandException(category.getPrefix() + trigger, command.getName(), container.getCommand().getName());
                    }
                }
            }
        }

        for (String middleware : command.getMiddleware()) {
            String[] parts = middleware.split(":");

            if (MiddlewareManager.getMiddleware(parts[0])==null) {
                throw new IllegalArgumentException("Middleware reference may not be null, " + parts[0] + " is not a valid middleware");
            }
        }

        String commandURI = null;

        CommandSource annotation = command.getClass().getAnnotation(CommandSource.class);
        if (annotation != null && annotation.uri().trim().length() > 0) {
            commandURI = annotation.uri();
        } else if (command.getClass().getTypeName().startsWith(Constants.PACKAGE_COMMAND_PATH)) {
            String[] split = command.getClass().toString().split("\\.");

            commandURI = String.format(Constants.SOURCE_URI, split[split.length - 2], split[split.length - 1]);
            COMMANDS.add(new CommandContainer(command, category, commandURI));
        }
    }

    public static Collection<CommandContainer> getCommands() {
        return COMMANDS;
    }

    public static CommandContainer getCommand(Marshmallow marshmallow, Message message, @Nonnull String contentRaw) {
        CommandContainer container = getCommand(message);
        if (container != null) {
            return container;
        }

        return getCommandByAlias(marshmallow, message, contentRaw);
    }

    public static CommandContainer getCommand(Message message) {
        return getCommand(message, message.getContentRaw().split(" ")[0].toLowerCase());
    }

    public static CommandContainer getCommand(Message message, @Nonnull String command) {
        List<CommandContainer> commands = new ArrayList<>();
        for (CommandContainer container : COMMANDS) {
            String prefix = container.getCommand().generateCommandPrefix(message);
            for (String trigger : container.getTriggers()) {
                if (command.equalsIgnoreCase(prefix + trigger)) {
                    commands.add(container);
                }
            }
        }

        return getHighPriorityCommandFromCommands(commands);
    }

    private static CommandContainer getHighPriorityCommandFromCommands(List<CommandContainer> commands) {
        if (commands.isEmpty()) {
            return null;
        }

        if (commands.size() == 1) {
            return commands.get(0);
        }

        //noinspection ConstantConditions
        return commands.stream().sorted((first, second) -> {
            if (first.getPriority().equals(second.getPriority())) {
                return 0;
            }
            return first.getPriority().isGreaterThan(second.getPriority()) ? -1 : 1;
        }).findFirst().get();
    }

    public static CommandContainer getCommandByAlias(Marshmallow marshmallow, Message message, @Nonnull String command) {
        GuildTransformer transformer = GuildController.fetchGuild(marshmallow, message);
        if (transformer == null || transformer.getAliases().isEmpty()) {
            return null;
        }

        String[] aliasArguments = null;
        String commandString = command.split(" ")[0].toLowerCase();
        List<CommandContainer> commands = new ArrayList<>();
        for (Map.Entry<String, String> entry : transformer.getAliases().entrySet()) {
            if (commandString.equalsIgnoreCase(entry.getKey())) {
                CommandContainer commandContainer = getRawCommand(entry.getValue().split(" ")[0]);
                if (commandContainer != null) {
                    commands.add(commandContainer);
                    aliasArguments = entry.getValue().split(" ");
                }
            }
        }

        CommandContainer commandContainer = getHighPriorityCommandFromCommands(commands);

        if (commandContainer == null) {
            return null;
        }

        if (aliasArguments == null || aliasArguments.length == 1) {
            return commandContainer;
        }
        return new AliasCommandContainer(commandContainer, Arrays.copyOfRange(aliasArguments, 1, aliasArguments.length));
    }

    public static CommandContainer getRawCommand(@Nonnull String command) {
        List<CommandContainer> commands = new ArrayList<>();
        for (CommandContainer container : COMMANDS) {
            String commandPrefix = container.getDefaultPrefix();
            for (String trigger : container.getTriggers()) {
                if (command.equalsIgnoreCase(commandPrefix + trigger)) {
                    commands.add(container);
                }
            }
        }

        return getHighPriorityCommandFromCommands(commands);
    }
}
