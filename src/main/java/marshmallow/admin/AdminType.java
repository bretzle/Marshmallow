package marshmallow.admin;

public enum AdminType {

    BOT_ADMIN(true),
    ROLE_ADMIN(true),
    USER(false);

    private final boolean admin;

    AdminType(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }
}
