package net.samagames.hub.scoreboards;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ScoreboardManager extends AbstractManager
{
    private final Map<UUID, PersonalScoreboard> scoreboards;
    private final ScheduledFuture glowingTask;
    private final ScheduledFuture reloadingTask;
    private int ipCharIndex;
    private int cooldown;

    public ScoreboardManager(Hub hub)
    {
        super(hub);

        this.scoreboards = new HashMap<>();
        this.ipCharIndex = 0;
        this.cooldown = 0;

        this.glowingTask = this.hub.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            String ip = this.colorIpAt();

            for (PersonalScoreboard scoreboard : this.scoreboards.values())
                this.hub.getExecutorMonoThread().execute(() -> scoreboard.setLines(ip));
        }, 200, 200, TimeUnit.MILLISECONDS);

        this.reloadingTask = this.hub.getScheduledExecutorService().scheduleAtFixedRate(() ->
        {
            for (PersonalScoreboard scoreboard : this.scoreboards.values())
                this.hub.getExecutorMonoThread().execute(scoreboard::reloadData);
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable()
    {
        this.glowingTask.cancel(true);
        this.reloadingTask.cancel(true);

        this.scoreboards.values().forEach(PersonalScoreboard::onLogout);
    }

    @Override
    public void onLogin(Player player)
    {
        if (this.scoreboards.containsKey(player.getUniqueId()))
        {
            this.log(Level.WARNING, "The player '" + player.getUniqueId().toString() + "' already have a scoreboard!");
            return;
        }

        this.scoreboards.put(player.getUniqueId(), new PersonalScoreboard(this.hub, player));
        this.log(Level.INFO, "Added scoreboard to '" + player.getUniqueId() + "'.");
    }

    @Override
    public void onLogout(Player player)
    {
        if (this.scoreboards.containsKey(player.getUniqueId()))
        {
            this.scoreboards.get(player.getUniqueId()).onLogout();
            this.scoreboards.remove(player.getUniqueId());

            this.log(Level.INFO, "Removed scoreboard to '" + player.getUniqueId() + "'.");
        }
    }

    public void update(Player player)
    {
        if (this.scoreboards.containsKey(player.getUniqueId()))
            this.scoreboards.get(player.getUniqueId()).reloadData();
    }

    private String colorIpAt()
    {
        String ip = "mc.samagames.net";

        if (this.cooldown > 0)
        {
            this.cooldown--;
            return ip;
        }

        StringBuilder formattedIp = new StringBuilder();

        if (this.ipCharIndex > 0)
        {
            formattedIp.append(ip.substring(0, this.ipCharIndex - 1));
            formattedIp.append(ChatColor.GOLD).append(ip.substring(this.ipCharIndex - 1, this.ipCharIndex));
        }
        else
        {
            formattedIp.append(ip.substring(0, this.ipCharIndex));
        }

        formattedIp.append(ChatColor.RED).append(ip.charAt(this.ipCharIndex));

        if (this.ipCharIndex + 1 < ip.length())
        {
            formattedIp.append(ChatColor.GOLD).append(ip.charAt(this.ipCharIndex + 1));

            if (this.ipCharIndex + 2 < ip.length())
                formattedIp.append(ChatColor.YELLOW).append(ip.substring(this.ipCharIndex + 2));

            this.ipCharIndex++;
        }
        else
        {
            this.ipCharIndex = 0;
            this.cooldown = 20;
        }

        return formattedIp.toString();
    }
}
