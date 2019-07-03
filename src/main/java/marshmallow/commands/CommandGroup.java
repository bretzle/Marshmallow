package marshmallow.commands;

import javax.annotation.Nonnull;

public enum CommandGroup {

    MODERATION("Moderation"),
    COMMAND_CUSTOMIZATION("Command Customization"),
    LEVEL_AND_EXPERIENCE("Level & Experience"),
    ROLE_ASSIGNMENTS("Role Assignments"),
    JOIN_LEAVE_MESSAGES("Join/Leave Messages"),
    INTERACTIONS("Interactions"),
    INFORMATION("Informative"),
    BOT_INFORMATION("Bot Information"),
    MUSIC_QUEUE("Music Queue"),
    MUSIC_TRACK_MODIFIER("Track Modifier"),
    MUSIC_START_PLAYING("Start playing"),
    MUSIC_SETTINGS("Music Settings"),
    MUSIC_SKIP("Skip Track"),
    MISCELLANEOUS("Misc");

    private final String name;

    CommandGroup(String name) {
        this.name = name;
    }

    @Nonnull
    public String getName() {
        return name;
    }
}
