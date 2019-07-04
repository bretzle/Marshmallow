package marshmallow.database.transformers;

import marshmallow.database.collection.DataRow;

public class PlayerTransformer extends Transformer {

    private final long userID;
    private final long guildID;

    private String username;
    private String usernameRaw;
    private String discriminator;
    private String avatarId;
    private long experience;

    private boolean active = false;

    public PlayerTransformer(long userID, long guildID, DataRow data) {
        super(data);

        this.userID = userID;
        this.guildID = guildID;

        if (hasData()) {
            username = data.getString("username");
            usernameRaw = data.get("username").toString();
            discriminator = data.getString("discriminator");
            avatarId = data.getString("avatar");
            active = data.getBoolean("active", false);

            // todo set experience values
//            List<TreeMap> guilds = (List<TreeMap>) data.get("experiences");
//            for (TreeMap map : guilds) {
//                if (map.get("id").equals(String.valueOf(guildID))) {
//                    this.experience = (long) map.get("value");
//                }
//            }

            reset();
        }
    }

    public boolean isActive() {
        return active;
    }

    public long getUserId() {
        return userID;
    }

    public long getGuildId() {
        return guildID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameRaw() {
        return usernameRaw;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getAvatar() {
        return avatarId;
    }

    public void setAvatar(String avatarId) {
        this.avatarId = avatarId;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public void incrementExperienceBy(long amount) {
        experience = experience + amount;
    }

    protected boolean checkIfTransformerHasData() {
        return data != null
                && data.getString("username") != null
                && data.getString("experience") != null
                && data.getString("discriminator") != null;
    }
}
