package net.samagames.hub.common.tasks;

import net.samagames.hub.Hub;
import net.samagames.tools.bossbar.BossBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
class AdvertisingTask extends AbstractTask
{
    private static final List<String> LINES;
    private final BossBar bossBar;
    private int i;

    AdvertisingTask(Hub hub)
    {
        super(hub);

        this.bossBar = BossBarAPI.getBar(ChatColor.YELLOW + "SamaGames", BarColor.RED, BarStyle.SOLID, 0.0D).getValue();
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
