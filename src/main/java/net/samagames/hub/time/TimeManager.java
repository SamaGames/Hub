package net.samagames.hub.time;

import com.maxmind.geoip2.DatabaseReader;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class TimeManager extends AbstractManager
{
    private final HashMap<UUID, BukkitTask> loops;
    private DatabaseReader databaseReader;

    public TimeManager(Hub hub)
    {
        super(hub);

        this.loops = new HashMap<>();

        try
        {
            this.databaseReader = new DatabaseReader.Builder(new File(hub.getDataFolder(), "GeoLiteCity.dat")).build();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void handleLogin(Player player)
    {
        try
        {
            this.loops.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimerAsynchronously(this.hub, new UpdateTimeTask(player), 1L, 5L));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void handleLogout(Player player)
    {
        if(this.loops.containsKey(player.getUniqueId()))
        {
            this.loops.get(player.getUniqueId()).cancel();
            this.loops.remove(player.getUniqueId());
        }
    }

    public DatabaseReader getDatabaseReader()
    {
        return this.databaseReader;
    }

    @Override
    public String getName()
    {
        return "TimeManager";
    }
}
