package marshmallow.admin;

public class AdminUser {

    private final long userId;
    private final AdminType type;

    AdminUser(long userId, AdminType type) {
        this.userId = userId;
        this.type = type;
    }

    AdminUser(AdminType type) {
        this(-1L, type);
    }

    public long getUserId() {
        return userId;
    }

    public boolean isGlobalAdmin() {
        return type.equals(AdminType.BOT_ADMIN);
    }

    public boolean isRoleAdmin() {
        return type.equals(AdminType.ROLE_ADMIN);
    }

    public final boolean isAdmin() {
        return isGlobalAdmin() || isRoleAdmin();
    }
}
