package marshmallow.database.transformers;

public class PartnerGuildTypeTransformer extends GuildTypeTransformer {

    PartnerGuildTypeTransformer() {
        this.name = "Partner";

        // Setup partner limits.
        limits.aliases = 300;
        limits.levelRoles = 100;
        limits.selfAssignableRoles = 100;
        limits.playlist.lists = 100;
        limits.playlist.songs = 100;
        limits.reactionRoles.messages = 30;
        limits.reactionRoles.rolesPerMessage = 20;
    }
}
