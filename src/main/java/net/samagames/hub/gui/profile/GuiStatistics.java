package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 26/12/2016
 */
class GuiStatistics extends AbstractGui
{
    private static final int[] BASE_SLTOS = {10, 11, 12, 13, 14, 15, 16};
    private int lines = 0;
    private int slot = 0;

    GuiStatistics(Hub hub)
    {
        super(hub);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 45, "Statistiques");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player)
    {
        IPlayerStats playerStats = SamaGamesAPI.get().getStatsManager().getPlayerStats(player.getUniqueId());

        this.setGameStatisticsSlotData("Hub", new ItemStack(Material.COMPASS, 1), Arrays.asList(
                Pair.of("Woots reçus", playerStats.getJukeBoxStatistics()::getWoots),
                Pair.of("Woots donnés", playerStats.getJukeBoxStatistics()::getWootsGiven),
                Pair.of("Mehs reçus", playerStats.getJukeBoxStatistics()::getMehs)
        ));

        this.setGameStatisticsSlotData("UHC", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUHCOriginalStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUHCOriginalStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUHCOriginalStatistics()::getKills),
                Pair.of("Morts", playerStats.getUHCOriginalStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("UHCRun", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUHCRunStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUHCRunStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUHCRunStatistics()::getKills),
                Pair.of("Morts", playerStats.getUHCRunStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("DoubleRunner", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getDoubleRunnerStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getDoubleRunnerStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getDoubleRunnerStatistics()::getKills),
                Pair.of("Morts", playerStats.getDoubleRunnerStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("UHCRandom", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUHCRandomStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUHCRandomStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUHCRandomStatistics()::getKills),
                Pair.of("Morts", playerStats.getUHCRandomStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("RandomRun", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getRandomRunStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getRandomRunStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getRandomRunStatistics()::getKills),
                Pair.of("Morts", playerStats.getRandomRunStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("Run4Flag", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUltraFlagKeeperStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUltraFlagKeeperStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUltraFlagKeeperStatistics()::getKills),
                Pair.of("Morts", playerStats.getUltraFlagKeeperStatistics()::getDeaths),
                Pair.of("Drapeaux capturés", playerStats.getUltraFlagKeeperStatistics()::getFlagsCaptured),
                Pair.of("Drapeaux retournés", playerStats.getUltraFlagKeeperStatistics()::getFlagsReturned)
        ));

        this.setGameStatisticsSlotData("Quake", new ItemStack(Material.DIAMOND_HOE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getQuakeStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getQuakeStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getQuakeStatistics()::getKills),
                Pair.of("Morts", playerStats.getQuakeStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("Uppervoid", new ItemStack(Material.STICK, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUppervoidStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUppervoidStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUppervoidStatistics()::getKills),
                Pair.of("TNT lancées", playerStats.getUppervoidStatistics()::getTntLaunched),
                Pair.of("Grenades lancées", playerStats.getUppervoidStatistics()::getGrenades),
                Pair.of("Blocs cassés", playerStats.getUppervoidStatistics()::getBlocks)
        ));

        this.setGameStatisticsSlotData("Dimensions", new ItemStack(Material.EYE_OF_ENDER, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getDimensionStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getDimensionStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getDimensionStatistics()::getKills),
                Pair.of("Morts", playerStats.getDimensionStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("ChunkWars", new ItemStack(Material.ENDER_PORTAL_FRAME, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getChunkWarsStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getChunkWarsStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getChunkWarsStatistics()::getKills),
                Pair.of("Morts", playerStats.getChunkWarsStatistics()::getDeaths)
        ));

        this.setSlotData(ChatColor.YELLOW + "Voir votre profil en ligne", new ItemStack(Material.NETHER_STAR, 1), this.inventory.getSize() - 6, null, "website");
        this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        switch (action)
        {
            case "website":
                new FancyMessage(ChatColor.YELLOW + "Cliquez sur ").then("[Accéder]").color(ChatColor.GOLD).style(ChatColor.BOLD).link("https://www.samagames.net/stats/" + player.getName() + ".html").then(" pour accéder à vos statistiques en ligne.").color(ChatColor.YELLOW).send(player);
                break;

            case "back":
                this.hub.getGuiManager().openGui(player, new GuiProfile(this.hub));
                break;
        }
    }

    private void setGameStatisticsSlotData(String game, ItemStack icon, List<Pair<String, Callable<Integer>>> statistics)
    {
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + game);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Statistique de " + game);
        lore.add("");

        for (Pair<String, Callable<Integer>> statistic : statistics)
        {
            try
            {
                lore.add(ChatColor.GRAY + "- " + statistic.getLeft() + " : " + ChatColor.WHITE + statistic.getRight().call());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        lore.add("");
        lore.add(ChatColor.GRAY + "Le détail de vos statistiques est");
        lore.add(ChatColor.GRAY + "disponible sur votre profil en ligne.");
        lore.add(ChatColor.GRAY + "Cliquez sur l'étoile pour y accéder.");

        meta.setLore(lore);
        icon.setItemMeta(meta);

        this.setSlotData(icon, (BASE_SLTOS[this.slot] + (this.lines * 9)), "none");

        this.slot++;

        if (this.slot == 7)
        {
            this.slot = 0;
            this.lines++;
        }
    }
}
