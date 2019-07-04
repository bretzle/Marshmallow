package marshmallow.language;

import marshmallow.config.yaml.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;

public class LanguageContainer {

    private final Language language;
    private final YamlConfiguration config;

    LanguageContainer(@Nonnull Language language) {
        this.language = language;

        config = new YamlConfiguration(
                new InputStreamReader(
                        getClass().getClassLoader().getResourceAsStream("langs/"+language.getCode()+".yml")
                )
        );
    }

    /**
     * Gets the language representation of the language container, both the English and
     * the native name for the language can be loaded through that, as well as the
     * language code and other names that can be associated with the language.
     *
     * @return The language representation the current language container.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Gets the language configuration, the config can be used to
     * load strings, lists, and values directly off the language.
     *
     * @return The language configuration.
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return language.getNativeName();
    }
}
