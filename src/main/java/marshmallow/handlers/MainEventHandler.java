package marshmallow.handlers;

import marshmallow.Marshmallow;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MainEventHandler extends ListenerAdapter implements EventListener {

    private final Marshmallow marshmallow;

    public MainEventHandler(Marshmallow marshmallow) {
        this.marshmallow = marshmallow;
    }
}
