package net.samagames.hub.npcs.actions;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

public abstract class AbstractNPCAction
{
    public void startTalk(Player player)
    {
        Hub.getInstance().getNPCManager().talk(player);
    }
    public void stopTalk(Player player)
    {
        Hub.getInstance().getNPCManager().talkFinished(player);
    }
    public abstract void execute(Player player);
}
