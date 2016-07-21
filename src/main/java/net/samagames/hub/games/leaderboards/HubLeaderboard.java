package net.samagames.hub.games.leaderboards;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamesNames;
import net.samagames.api.stats.Leaderboard;
import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class HubLeaderboard
{
    private Hub hub;
    private GamesNames game;
    private String displayName;
    private String statName;
    private Location sign;
    private List<HubLeaderBoardStand> stands;

    public HubLeaderboard(Hub hub, GamesNames game, String displayName, String statName, Location sign, List<HubLeaderBoardStand> stands)
    {
        this.hub = hub;
        this.game = game;
        this.displayName = displayName;
        this.statName = statName;
        this.sign = sign;
        this.stands = stands;
    }

    public void refresh()
    {
        final Leaderboard leaderboard = SamaGamesAPI.get().getStatsManager().getLeaderboard(this.game, this.statName);
        if (leaderboard == null)
            return ;
        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            Block block = this.sign.getBlock();
            if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
            {
                Sign sign = (Sign)block.getState();
                sign.setLine(0, "*-*-*-*-*");
                sign.setLine(1, this.displayName);
                sign.setLine(2, "");
                sign.setLine(3, "*-*-*-*-*");
                sign.update();
            }
            for (int i = 0; i < 3 && i < this.stands.size(); i++)
            {
                Leaderboard.PlayerStatData stat = i == 0 ? leaderboard.getFirst() : i == 1 ? leaderboard.getSecond() : leaderboard.getThird();
                if (stat == null)
                    continue;
                Block block2 = this.stands.get(i).sign.getBlock();
                if (block2.getType() == Material.SIGN_POST || block2.getType() == Material.WALL_SIGN)
                {
                    Sign sign = (Sign) block2.getState();
                    sign.setLine(0, "*-*-*-*-*");
                    sign.setLine(1, stat.getName());
                    sign.setLine(2, String.valueOf(stat.getScore()));
                    sign.setLine(3, "*-*-*-*-*");
                    sign.update();
                }
                ArmorStand armorStand = (ArmorStand)this.stands.get(i).hologram.getWorld().getNearbyEntities(this.stands.get(i).hologram, 0.3D, 2D, 0.3D).stream().filter(entity -> entity instanceof ArmorStand).findFirst().orElse(null);
                if (armorStand != null)
                {
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName((i == 0 ? ChatColor.AQUA + "1er": i == 1 ? ChatColor.GOLD + "2e" : ChatColor.GRAY + "3e") + " - " + stat.getName());
                    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
                    SkullMeta meta = (SkullMeta) skull.getItemMeta();
                    meta.setOwner(stat.getName());
                    skull.setItemMeta(meta);
                    armorStand.setHelmet(skull);
                }
            }
        });
    }

    public static class HubLeaderBoardStand
    {
        private Location sign;
        private Location hologram;

        public HubLeaderBoardStand(Location sign, Location hologram)
        {
            this.sign = sign;
            this.hologram = hologram;
        }
    }
}
