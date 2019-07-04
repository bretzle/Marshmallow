package marshmallow.commands;

import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.database.controllers.GuildController;
import marshmallow.database.transformers.GuildTransformer;
import net.dv8tion.jda.core.entities.Message;

import javax.annotation.Nonnull;
import java.util.*;

@Slf4j
public class CommandManager {

    private static final Set<CommandContainer> COMMANDS = new HashSet<>();

    public static void register(Command command) {
        log.info("Registered " + command.getName() + "command.");
        // todo actually register commands
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
