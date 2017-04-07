package net.samagames.hub.common.tasks;

import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_9_R2.boss.CraftBossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 03/02/2017
 */
class AdvertisingTask extends AbstractTask
{
    private static final List<String> LINES;
    private final CraftBossBar bossBar;
    private int i;

    AdvertisingTask(Hub hub)
    {
        super(hub);

        this.bossBar = new CraftBossBar(ChatColor.YELLOW + "SamaGames", BarColor.RED, BarStyle.SOLID);
        this.bossBar.setProgress(0.0D);

        this.hub.getServer().getOnlinePlayers().forEach(this.bossBar::addPlayer);

        this.i = 0;

        this.task = this.hub.getServer().getScheduler().runTaskTimer(hub, this, 20L * 5, 20L * 5);
    }

    @Override
    public void run()
    {
        this.bossBar.setTitle(LINES.get(this.i));

        this.i++;

        if (this.i == LINES.size())
            this.i = 0;
    }

    public void addPlayer(Player player)
    {
        this.bossBar.addPlayer(player);
    }

    public void removePlayer(Player player)
    {
        this.bossBar.removePlayer(player);
    }

    static
    {
        LINES = new ArrayList<>();
        LINES.add(ChatColor.YELLOW + "Toutes les informations sur " + ChatColor.RED + "www.samagames.net" + ChatColor.YELLOW + " !");
        LINES.add(ChatColor.YELLOW + "SamaGames est sur Twitter : " + ChatColor.AQUA + "@SamaGames_MC" + ChatColor.YELLOW + " !");
        LINES.add(ChatColor.YELLOW + "SamaGames est sur YouTube : " + ChatColor.RED + "@SamaGames" + ChatColor.YELLOW + " !");
        LINES.add(ChatColor.YELLOW + "Venez discuter sur TeamSpeak : " + ChatColor.GREEN + "ts.samagames.net" + ChatColor.YELLOW + " !");
    }
}
