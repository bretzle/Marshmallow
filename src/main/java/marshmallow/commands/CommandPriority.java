package marshmallow.commands;


public enum CommandPriority {

    HIDDEN(3),
    SYSTEM(2, true),
    SYSTEM_ROLE(1, true),
    IGNORED(0),
    LOWEST(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    HIGHEST(5);

    private final int priority;
    private final boolean system;

    CommandPriority(int priority) {
        this(priority, false);
    }

    CommandPriority(int priority, boolean system) {
        this.priority = priority;
        this.system = system;
    }

    public boolean isGreaterThan(CommandPriority commandPriority) {
        return priority > commandPriority.priority;
    }

    public boolean isSystem() {
        return system;
    }
}
