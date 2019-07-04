package marshmallow.handlers;

import marshmallow.Marshmallow;
import marshmallow.handlers.adapter.MessageEventAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MainEventHandler extends ListenerAdapter implements EventListener {

    private final Marshmallow marshmallow;

    private final MessageEventAdapter messageEvent;

    public MainEventHandler(Marshmallow marshmallow) {
        this.marshmallow = marshmallow;

        this.messageEvent = new MessageEventAdapter(marshmallow);
    }

//    @Override
//    public void onReady(ReadyEvent event) {
//        jdaStateEventAdapter.onConnectToShard(event.getJDA());
//    }
//
//    @Override
//    public void onResume(ResumedEvent event) {
//        jdaStateEventAdapter.onConnectToShard(event.getJDA());
//    }
//
//    @Override
//    public void onReconnect(ReconnectedEvent event) {
//        jdaStateEventAdapter.onConnectToShard(event.getJDA());
//    }
//
//    @Override
//    public void onGuildUpdateRegion(GuildUpdateRegionEvent event) {
//        guildStateEvent.onGuildUpdateRegion(event);
//    }
//
//    @Override
//    public void onGuildUpdateName(GuildUpdateNameEvent event) {
//        guildStateEvent.onGuildUpdateName(event);
//    }
//
//    @Override
//    public void onGuildJoin(GuildJoinEvent event) {
//        guildStateEvent.onGuildJoin(event);
//    }
//
//    @Override
//    public void onGuildLeave(GuildLeaveEvent event) {
//        guildStateEvent.onGuildLeave(event);
//    }
//
//    @Override
//    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
//        channelEvent.onVoiceChannelDelete(event);
//    }
//
//    @Override
//    public void onTextChannelDelete(TextChannelDeleteEvent event) {
//        channelEvent.updateChannelData(event.getGuild());
//        channelEvent.onTextChannelDelete(event);
//    }
//
//    @Override
//    public void onTextChannelCreate(TextChannelCreateEvent event) {
//        channelEvent.updateChannelData(event.getGuild());
//    }
//
//    @Override
//    public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
//        channelEvent.updateChannelData(event.getGuild());
//    }
//
//    @Override
//    public void onTextChannelUpdatePosition(TextChannelUpdatePositionEvent event) {
//        channelEvent.updateChannelData(event.getGuild());
//    }
//
//    @Override
//    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
//        if (!marshmallow.getSettings().isMusicOnlyMode()) {
//            memberEvent.onGuildMemberJoin(event);
//        }
//    }
//
//    @Override
//    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
//        if (!marshmallow.getSettings().isMusicOnlyMode()) {
//            memberEvent.onGuildMemberLeave(event);
//        }
//    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
//        if (changelogEventAdapter.isChangelogMessage(event.getChannel())) {
//            changelogEventAdapter.onMessageReceived(event);
//        }

        messageEvent.onMessageReceived(event);
    }

//    @Override
//    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
//        if (changelogEventAdapter.isChangelogMessage(event.getChannel())) {
//            changelogEventAdapter.onMessageDelete(event);
//        }
//
//        messageEvent.onMessageDelete(event.getChannel(), Collections.singletonList(event.getMessageId()));
//    }
//
//    @Override
//    public void onMessageBulkDelete(MessageBulkDeleteEvent event) {
//        messageEvent.onMessageDelete(event.getChannel(), event.getMessageIds());
//    }
//
//    @Override
//    public void onMessageUpdate(MessageUpdateEvent event) {
//        if (changelogEventAdapter.isChangelogMessage(event.getChannel())) {
//            changelogEventAdapter.onMessageUpdate(event);
//        }
//
//        messageEvent.onMessageUpdate(event);
//    }

//    @Override
//    public void onRoleUpdateName(RoleUpdateNameEvent event) {
//        roleEvent.updateRoleData(event.getGuild());
//        roleEvent.onRoleUpdateName(event);
//    }
//
//    @Override
//    public void onRoleDelete(RoleDeleteEvent event) {
//        roleEvent.updateRoleData(event.getGuild());
//        roleEvent.onRoleDelete(event);
//    }
//
//    @Override
//    public void onRoleCreate(RoleCreateEvent event) {
//        roleEvent.updateRoleData(event.getGuild());
//    }
//
//    @Override
//    public void onRoleUpdatePosition(RoleUpdatePositionEvent event) {
//        roleEvent.updateRoleData(event.getGuild());
//    }
//
//    @Override
//    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {
//        roleEvent.updateRoleData(event.getGuild());
//    }
//
//    @Override
//    public void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event) {
//        if (!marshmallow.getSettings().isMusicOnlyMode()) {
//            PlayerController.updateUserData(event.getUser());
//        }
//    }
//
//    @Override
//    public void onUserUpdateAvatar(UserUpdateAvatarEvent event) {
//        if (!marshmallow.getSettings().isMusicOnlyMode()) {
//            PlayerController.updateUserData(event.getUser());
//        }
//    }
//
//    @Override
//    public void onUserUpdateName(UserUpdateNameEvent event) {
//        if (!marshmallow.getSettings().isMusicOnlyMode()) {
//            PlayerController.updateUserData(event.getUser());
//        }
//    }
//
//    @Override
//    public void onEmoteRemoved(EmoteRemovedEvent event) {
//        reactionEmoteEventAdapter.onEmoteRemoved(event);
//    }
//
//    @Override
//    public void onMessageReactionAdd(MessageReactionAddEvent event) {
//        if (isValidMessageReactionEvent(event)) {
//            reactionEmoteEventAdapter.onMessageReactionAdd(event);
//        }
//    }
//
//    @Override
//    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
//        if (isValidMessageReactionEvent(event)) {
//            reactionEmoteEventAdapter.onMessageReactionRemove(event);
//        }
//    }
//
//    private boolean isValidMessageReactionEvent(GenericMessageReactionEvent event) {
//        return !event.getUser().isBot()
//                && event.getGuild() != null
//                && event.getReactionEmote().getEmote() != null;
//    }
}
