package marshmallow.commands;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandManager {

    public static void register(Command command) {
        log.info("Registered " + command.getName() + "command.");
    }
}
