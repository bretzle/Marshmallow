package marshmallow.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import marshmallow.config.yaml.YamlConfiguration;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

@Slf4j
public class Context {

    @Getter public final Guild guild;
    @Getter public final Member member;
    @Getter public final TextChannel channel;
    @Getter public final Message message;

    @Getter private final boolean mentionableCommand;
    private final String aliasArguments;

    private YamlConfiguration i18n;
    private String i18nCommandPrefix;

    public Context(CommandContainer container, Message message) {
        this(container, message, false, new String[0]);
    }

    public Context(CommandContainer container, Message message, boolean mentionableCommand, String[] aliasArguments) {
        if (container != null) setI18nCommandPrefix(container);

        this.message = message;
        this.guild = message.getGuild();
        this.member = message.getMember();
        this.channel = message.getTextChannel();

        this.mentionableCommand = mentionableCommand;
        this.aliasArguments = aliasArguments.length == 0 ? null : String.join(" ", aliasArguments);
    }

    public boolean canDelete() {
        return isGuildMessage() && getGuild().getSelfMember().hasPermission(
                getChannel(), Permission.MESSAGE_MANAGE
        );
    }

    public AuditableRestAction<Void> delete() {
        return canDelete() ? message.delete() : new AuditableRestAction.EmptyRestAction<>(getJDA());
    }

    public JDA getJDA() {
        return message.getJDA();
    }

    public String getContentDisplay() {
        return parseContent(message.getContentDisplay());
    }

    public String getContentStripped() {
        return parseContent(message.getContentStripped());
    }

    public String getContentRaw() {
        String[] parts = message.getContentRaw().split(" ");

        return (aliasArguments == null ? "" : aliasArguments) + String.join(" ",
                Arrays.copyOfRange(parts, isMentionableCommand() ? 2 : 1, parts.length)
        );
    }

    private String parseContent(String content) {
        String[] parts = content.split(" ");

        if (!isMentionableCommand()) {
            return String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        }

        int nameSize = (isGuildMessage() ?
                message.getGuild().getSelfMember().getEffectiveName() :
                message.getJDA().getSelfUser().getName()
        ).split(" ").length + 1;

        return String.join(" ", Arrays.copyOfRange(parts, nameSize, parts.length));
    }

    public boolean isGuildMessage() {
        return message.getChannelType().isGuild();
    }

    public void setI18nPrefix(@Nullable String i18nPrefix) {
        this.i18nCommandPrefix = i18nPrefix;
    }

    public void setI18nCommandPrefix(@Nonnull CommandContainer container) {
        setI18nPrefix(
                container.getCategory().getName().toLowerCase() + "."
                        + container.getCommand().getClass().getSimpleName()
        );
    }
}
