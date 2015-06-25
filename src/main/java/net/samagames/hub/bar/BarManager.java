package net.samagames.hub.bar;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.BarAPI.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class BarManager extends AbstractManager
{
    private final ArrayList<BarMessage> messages;
    private final ArrayList<UUID> toAdd;
    private BarMessage actualMessage;
    private int index;
    private int time;

    public BarManager(Hub hub)
    {
        super(hub);

        this.messages = new ArrayList<>();
        this.toAdd = new ArrayList<>();
        this.index = -1;

        this.loadMessages();

        Bukkit.getScheduler().runTaskTimerAsynchronously(hub, () ->
        {
            if(this.actualMessage == null)
            {
                if (!this.messages.isEmpty())
                {
                    this.actualMessage = this.messages.get(this.index);
                    this.time = this.actualMessage.getSeconds();
                }
            }

            if(this.actualMessage != null)
            {
                if(this.time == this.actualMessage.getSeconds())
                {
                    Bukkit.getOnlinePlayers().forEach(p -> this.toAdd.add(p.getUniqueId()));
                }

                for(UUID uuid : this.toAdd)
                {
                    if(Bukkit.getPlayer(uuid) != null)
                        BarAPI.setMessage(Bukkit.getPlayer(uuid), this.actualMessage.getMessage());
                }

                this.toAdd.clear();

                this.time--;

                if(this.time == 0)
                {
                    this.index++;

                    if(this.index > this.messages.size())
                        this.index = 0;

                    this.actualMessage = this.messages.get(this.index);
                }
            }
        }, 20L, 20L);
    }

    public void handleLogin(Player player)
    {
        this.toAdd.add(player.getUniqueId());
    }

    public void handleLogout(Player player)
    {
        if(this.toAdd.contains(player.getUniqueId()))
            this.toAdd.remove(player.getUniqueId());

        if(BarAPI.hasWitherBar(player))
            BarAPI.removeBar(player);
    }

    public void loadMessages()
    {
        this.registerMessage(new JukeboxMessage());
    }

    @Override
    public void onServerClose()
    {
        this.messages.clear();
    }

    public void registerMessage(BarMessage message)
    {
        this.messages.add(message);
    }

    @Override
    public String getName()
    {
        return "BarManager";
    }
}
