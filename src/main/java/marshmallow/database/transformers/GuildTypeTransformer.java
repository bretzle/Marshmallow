package marshmallow.database.transformers;

import marshmallow.debug.Evalable;
import marshmallow.Marshmallow;
import marshmallow.database.collection.DataRow;

public class GuildTypeTransformer extends Transformer {

    private static final String defaultName = "Default";

    protected String name = defaultName;

    GuildTypeLimits limits = new GuildTypeLimits();

    GuildTypeTransformer() {
        super(null);
    }

    GuildTypeTransformer(DataRow data) {
        super(data);

        if (hasData()) {
            if (data.getString("type_name", null) != null) {
                name = data.getString("type_name");
            }

            if (data.getString("type_limits", null) != null) {
                GuildTypeLimits typeLimits = Marshmallow.gson.fromJson(data.getString("type_limits"), GuildTypeLimits.class);
                if (typeLimits != null) {
                    if (typeLimits.levelRoles < limits.levelRoles) {
                        typeLimits.levelRoles = limits.levelRoles;
                    }

                    if (typeLimits.selfAssignableRoles < limits.selfAssignableRoles) {
                        typeLimits.selfAssignableRoles = limits.selfAssignableRoles;
                    }

                    limits = typeLimits;
                }
            }
        }
    }

    public boolean isDefault() {
        return getName().equals(defaultName);
    }

    public String getName() {
        return name;
    }

    public GuildTypeLimits getLimits() {
        return limits;
    }

    public class GuildTypeLimits extends Evalable {

        protected GuildReactionRoles reactionRoles = new GuildReactionRoles();
        protected GuildTypePlaylist playlist = new GuildTypePlaylist();

        int aliases = 20;
        int selfAssignableRoles = 15;
        int levelRoles = 10;

        public int getAliases() {
            return aliases;
        }

        public int getSelfAssignableRoles() {
            return selfAssignableRoles;
        }

        public int getLevelRoles() {
            return levelRoles;
        }

        public GuildTypePlaylist getPlaylist() {
            return playlist;
        }

        public GuildReactionRoles getReactionRoles() {
            return reactionRoles;
        }

        public class GuildTypePlaylist extends Evalable {

            int lists = 5;
            int songs = 30;

            public int getPlaylists() {
                return lists;
            }

            public int getSongs() {
                return songs;
            }
        }

        public class GuildReactionRoles extends Evalable {

            int messages = 3;
            int rolesPerMessage = 5;

            public int getMessages() {
                return messages;
            }

            public int getRolesPerMessage() {
                return rolesPerMessage;
            }
        }
    }
}
