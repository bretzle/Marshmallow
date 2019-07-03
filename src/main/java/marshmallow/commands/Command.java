package marshmallow.commands;

import marshmallow.Marshmallow;

import java.util.List;
import java.util.Objects;

public abstract class Command {

    protected final Marshmallow marshmallow;
    protected final boolean allowDM;

    public Command(Marshmallow marshmallow) {
        this(marshmallow, true);
    }

    public Command(Marshmallow marshmallow, boolean allowDM) {
        this.marshmallow = marshmallow;
        this.allowDM = allowDM;
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract List<String> getUsageInstructions();

    public abstract List<String> getExampleUsage();

    public abstract List<Class<? extends Command>> getRelations();

    public abstract List<String> getTriggers();

    public abstract List<String> getMiddleware();

    public abstract Category getCategory();

    public abstract List<CommandGroup> getGroups();

    public final boolean isAllowDM() {
        return allowDM;
    }

    public abstract boolean onCommand(Context context, String[] args);

    private boolean isSame(Command command) {
        return Objects.equals(command.getName(), getName())
                && Objects.equals(command.getUsageInstructions(), getUsageInstructions())
                && Objects.equals(command.getExampleUsage(), getExampleUsage())
                && Objects.equals(command.getTriggers(), getTriggers());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Command && isSame((Command) obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }
}
