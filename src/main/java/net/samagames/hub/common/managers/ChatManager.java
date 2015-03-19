package net.samagames.hub.common.managers;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ChatManager extends AbstractManager
{
    private final HashMap<UUID, Date> lastPlayerMessages;
    private final HashMap<UUID, Date> mutedPlayers;
    private final ArrayList<UUID> chatDisablers;

    private int actualSlowDuration;
    private boolean canChat;

    public ChatManager(Hub hub)
    {
        super(hub);

        this.lastPlayerMessages = new HashMap<>();
        this.mutedPlayers = new HashMap<>();
        this.chatDisablers = new ArrayList<>();

        this.canChat = true;
    }

    public void actualizePlayerLastMessage(Player player)
    {
        this.lastPlayerMessages.put(player.getUniqueId(), new Date());
    }

    public void disableChat(Player player)
    {
        if(!this.chatDisablers.contains(player.getUniqueId()))
            this.chatDisablers.add(player.getUniqueId());
    }

    public void unmutePlayer(Player player)
    {
        if(this.mutedPlayers.containsKey(player.getUniqueId()))
            this.mutedPlayers.remove(player.getUniqueId());
    }

    public void removeChatDisabler(Player player)
    {
        if(this.chatDisablers.contains(player.getUniqueId()))
            this.chatDisablers.remove(player.getUniqueId());
    }

    public void setChatEnabled(boolean flag)
    {
        this.canChat = flag;
    }

    public void setActualSlowDuration(int time)
    {
        this.actualSlowDuration = time;
    }

    public int getActualSlowDuration()
    {
        return this.actualSlowDuration;
    }

    public Date getLastPlayerMessageDate(Player player)
    {
        if(this.lastPlayerMessages.containsKey(player.getUniqueId()))
            return this.lastPlayerMessages.get(player.getUniqueId());
        else
            return null;
    }

    public ArrayList<UUID> getChatDisablers()
    {
        return this.chatDisablers;
    }

    public boolean hasPlayerTalked(Player player)
    {
        return this.lastPlayerMessages.containsKey(player.getUniqueId());
    }

    public boolean canChat()
    {
        return this.canChat;
    }

    @Override
    public String getName() { return "ChatManager"; }
}
