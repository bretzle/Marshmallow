package marshmallow.config;

import marshmallow.Marshmallow;

import java.util.HashMap;

public class ConstantsConfig extends Configuration {

    private final HashMap<String, Long> cache = new HashMap<>();

    public ConstantsConfig(Marshmallow marshmallow) {
        super(marshmallow, null, "constants.yml");
    }
}
