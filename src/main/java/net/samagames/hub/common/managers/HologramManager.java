package net.samagames.hub.common.managers;

import net.samagames.hub.Hub;
import net.samagames.tools.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class HologramManager extends AbstractManager
{
    private HashMap<UUID, Hologram> holograms;

    public HologramManager(Hub hub)
    {
        super(hub);

        this.holograms = new HashMap<>();
    }

    @Override
    public void onServerClose()
    {
        this.holograms.keySet().forEach(this::removeHologram);
        this.holograms.clear();
    }

    public UUID registerHologram(Hologram hologram)
    {
        UUID random = UUID.randomUUID();

        hologram.generateLines();
        Bukkit.getOnlinePlayers().forEach(hologram::addReceiver);

        this.holograms.put(random, hologram);

        return random;
    }

    public void removeHologram(UUID uuid)
    {
        if(this.holograms.containsKey(uuid))
        {
            this.holograms.get(uuid).removeLinesForPlayers();
            this.holograms.remove(uuid);
        }
    }

    public void addReceiver(Player player)
    {
        for(Hologram hologram : this.holograms.values())
            hologram.addReceiver(player);

        Hub.getInstance().log(this, Level.INFO, "Added hologram receiver (" + player.getUniqueId().toString() + ")");
    }

    public void removeReceiver(Player player)
    {
        for(Hologram hologram : this.holograms.values())
            hologram.removeReceiver(player);

        Hub.getInstance().log(this, Level.INFO, "Removed hologram receiver (" + player.getUniqueId().toString() + ")");
    }

    public Hologram getHologramByID(UUID uuid)
    {
        if(this.holograms.containsKey(uuid))
            return this.holograms.get(uuid);
        else
            return null;
    }

    @Override
    public String getName() { return "HologramManager"; }
}
