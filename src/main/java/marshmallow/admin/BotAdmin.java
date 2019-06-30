package marshmallow.admin;

import lombok.extern.slf4j.Slf4j;
import marshmallow.Marshmallow;
import marshmallow.util.RoleUtil;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BotAdmin {

    private static final AdminUser nullUser = new AdminUser(AdminType.USER);

    private final Set<AdminUser> botAdmins;
    private final Marshmallow marshmallow;

    public BotAdmin(Marshmallow marshmallow, Set<String> botAdmins) {
        this.marshmallow = marshmallow;
        this.botAdmins = new HashSet<>();

        for (String user : botAdmins) {
            try {
                this.botAdmins.add(new AdminUser(
                        Long.parseLong(user), AdminType.BOT_ADMIN
                ));
            } catch (NumberFormatException e) {
                log.warn("{} is and invalid ID, the ID has not been added to the bot admin whitelist.", user);
            }
        }
    }

    @Nonnull
    public AdminUser getUserById(@Nullable String userId) {
        return getUserById(userId, false);
    }

    @Nonnull
    public AdminUser getUserById(@Nullable String userId, boolean skipRoleCheck) {
        if (userId == null) {
            return nullUser;
        }

        try {
            return getUserById(Long.parseLong(userId), skipRoleCheck);
        } catch (NumberFormatException e) {
            return nullUser;
        }
    }

    @Nonnull
    public AdminUser getUserById(long userId) {
        return getUserById(userId, false);
    }

    @Nonnull
    public AdminUser getUserById(long userId, boolean skipRoleCheck) {
        AdminUser user = getUserFromBotAdminSet(userId);
        if (user != null) {
            return user;
        }
        return skipRoleCheck ?
                nullUser : new AdminUser(userId, getRoleAdminType(userId));
    }

    @Nullable
    private AdminUser getUserFromBotAdminSet(long userId) {
        for (AdminUser user : botAdmins) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    private AdminType getRoleAdminType(long userId) {
        Role role = marshmallow.getShardManager().getRoleById(
                marshmallow.getConstants().getBotAdminExceptionRoleId()
        );

        if (role == null) {
            return AdminType.USER;
        }

        Member member = role.getGuild().getMemberById(userId);
        if (member == null) {
            return AdminType.USER;
        }

        if (!RoleUtil.hasRole(member, role)) {
            return AdminType.USER;
        }
        return AdminType.ROLE_ADMIN;
    }
}
