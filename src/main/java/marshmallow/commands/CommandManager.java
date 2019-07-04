package marshmallow.commands;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CommandManager {

    private static final Set<CommandContainer> COMMANDS = new HashSet<>();

    public static void register(Command command) {
        log.info("Registered " + command.getName() + "command.");
    }

    public static Collection<CommandContainer> getCommands() {
        return COMMANDS;
    }
}
