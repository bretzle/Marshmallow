package marshmallow.commands;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A container class that acts as a middle man for a command
 */
public class CommandContainer {

    @Getter
    private final Command command;
    @Getter
    private final Category category;
    @Getter
    private final String sourceUri;
    @Getter
    private final Set<String> triggers;
    @Getter
    private final List<String> middleware;

    /**
     * Creates a new {@link Command command} container instance
     *
     * @param command   The command that should be assigned to the container
     * @param category  The command's category
     * @param sourceUri The source URI for the source code
     */
    public CommandContainer(@Nonnull Command command, @Nonnull Category category, @Nullable String sourceUri) {
        this.command = command;
        this.category = category;
        this.sourceUri = sourceUri;

        this.triggers = new HashSet<>(command.getTriggers());
        this.middleware = new ArrayList<>(command.getMiddleware());

//        this.registerThrottleMiddlewares();
    }

    public String getDefaultPrefix() {
        return category.getPrefix();
    }
}
