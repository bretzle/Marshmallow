package marshmallow.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import marshmallow.config.yaml.YamlConfiguration;
import marshmallow.factories.MessageFactory;
import marshmallow.gui.chat.MessageType;
import marshmallow.gui.chat.PlaceholderMessage;
import marshmallow.handlers.DatabaseEventHolder;
import marshmallow.language.I18n;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;

import java.awt.*;
import java.util.Arrays;

@Slf4j
public class Context {

    @Getter
    public final Guild guild;
    @Getter
    public final Member member;
    @Getter
    public final TextChannel channel;
    @Getter
    public final Message message;

    @Getter
    private final boolean mentionableCommand;
    private final String aliasArguments;
    private final DatabaseEventHolder databaseEventHolder;
    private final CommandContainer container;

    private YamlConfiguration i18n;
    private String i18nCommandPrefix;

    public Context(CommandContainer container, DatabaseEventHolder databaseEventHolder, Message message) {
        this(container, databaseEventHolder, message, false, new String[0]);
    }

    public Context(CommandContainer container, DatabaseEventHolder databaseEventHolder, Message message, boolean mentionableCommand, String[] aliasArguments) {
        this.container = container;
        this.message = message;

        this.guild = message.getGuild();
        this.member = message.getMember();
        this.channel = message.getTextChannel();
        this.databaseEventHolder = databaseEventHolder;

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

    public Member getAuthor() {
        return member;
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

    public PlaceholderMessage makeError(String message) {
        return MessageFactory.makeError(this.message, message);
    }

    public PlaceholderMessage makeWarning(String message) {
        return MessageFactory.makeWarning(this.message, message);
    }

    public PlaceholderMessage makeSuccess(String message) {
        return MessageFactory.makeSuccess(this.message, message);
    }

    public PlaceholderMessage makeInfo(String message) {
        return MessageFactory.makeInfo(this.message, message);
    }

    public PlaceholderMessage makeEmbeddedMessage(Color color, String message) {
        return MessageFactory.makeEmbeddedMessage(this.message, color, message);
    }

    public PlaceholderMessage makeEmbeddedMessage(MessageType type, MessageEmbed.Field... fields) {
        return makeEmbeddedMessage(type.getColor(), fields);
    }

    public PlaceholderMessage makeEmbeddedMessage(Color color, MessageEmbed.Field... fields) {
        return MessageFactory.makeEmbeddedMessage(this.message.getChannel(), color, fields);
    }

    public PlaceholderMessage makeEmbeddedMessage() {
        return MessageFactory.makeEmbeddedMessage(this.message.getChannel());
    }

    public String i18n(String key) {
        if (i18n == null) {
            i18n = I18n.get(guild);
        }

        String comKey =
                container.getCategory().getName().toLowerCase() + "." +
                        container.getCommand().getClass().getSimpleName() + "." +
                        key;

        return i18n.getString(comKey);
    }

    public Object testI18n(String key) {
        return i18n.get(key);
    }
}
