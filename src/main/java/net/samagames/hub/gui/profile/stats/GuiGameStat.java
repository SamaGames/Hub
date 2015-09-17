package net.samagames.hub.gui.profile.stats;

import net.samagames.api.stats.IPlayerStat;
import net.samagames.api.stats.Leaderboard;
import net.samagames.core.api.stats.PlayerStat;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import net.samagames.tools.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public class GuiGameStat extends AbstractGui
{
    private final String name;
    private final UUID uuid;
    private final AbstractGame game;
    private final String stat;

    public GuiGameStat(String name, UUID uuid, AbstractGame game, String stat)
    {
        this.name = name;
        this.uuid = uuid;
        this.game = game;
        this.stat = stat;
    }

    @Override
    public void display(Player player)
    {
        System.out.println(this.game + " " + stat);
        this.inventory = Bukkit.createInventory(null, 45, "> " + this.game.getName() + " (" + this.game.getDisplayedStatByIdentifier(this.stat).getDisplayName() + ")");

        this.drawLeaderboard();
        this.drawLeaderboardLine();

        PlayerStat playerStat = new PlayerStat(this.uuid, this.game.getCodeName(), this.stat);

        if(playerStat.fill())
        {
            this.setSlotData(ChatColor.RED + "Score de " + this.name, Material.LEATHER_HELMET, 22, new String[]{
                    ChatColor.GRAY + "Score : " + ChatColor.GOLD + playerStat.getValue(),
                    ChatColor.GRAY + "Rang : " + ChatColor.GOLD + playerStat.getRank()
            }, "none");
        }
        else
        {
            this.setSlotData(ChatColor.RED + "Score de " + this.name, Material.LEATHER_HELMET, 22, new String[]{
                    ChatColor.RED + "Jeu jamais joué !"
            }, "none");
        }

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiGameStats(this.name, this.uuid, this.game));
        }
    }

    private void drawLeaderboard()
    {
        Leaderboard leaderboard = Hub.getInstance().getStatsManager().getLeaderbordOf(this.stat);

        if(leaderboard != null)
        {
            IPlayerStat first = leaderboard.getFirst();
            IPlayerStat second = leaderboard.getSecond();
            IPlayerStat third = leaderboard.getThird();

            this.setSlotData(ChatColor.AQUA + "1ère place", Material.DIAMOND_HELMET, 13, new String[]{
                    ChatColor.GRAY + "Détenue par : " + (first.getPlayerUUID() != null ? PlayerUtils.getFullyFormattedPlayerName(first.getPlayerUUID()) : ChatColor.RED + "Personne :("),
                    (first.getPlayerUUID() != null ? ChatColor.GRAY + "Score : " + ChatColor.GOLD + first.getValue() : null)
            }, "none");

            this.setSlotData(ChatColor.GOLD + "2ème place", Material.GOLD_HELMET, 14, new String[]{
                    ChatColor.GRAY + "Détenue par : " + (second.getPlayerUUID() != null ? PlayerUtils.getFullyFormattedPlayerName(second.getPlayerUUID()) : ChatColor.RED + "Personne :("),
                    (second.getPlayerUUID() != null ? ChatColor.GRAY + "Score : " + ChatColor.GOLD + second.getValue() : null)
            }, "none");

            this.setSlotData(ChatColor.WHITE + "3ème place", Material.IRON_HELMET, 12, new String[]{
                    ChatColor.GRAY + "Détenue par : " + (third.getPlayerUUID() != null ? PlayerUtils.getFullyFormattedPlayerName(third.getPlayerUUID()) : ChatColor.RED + "Personne :("),
                    (third.getPlayerUUID() != null ? ChatColor.GRAY + "Score : " + ChatColor.GOLD + third.getValue() : null)
            }, "none");
        }
        else
        {
            this.setSlotData(ChatColor.AQUA + "1ère place", Material.DIAMOND_HELMET, 13, new String[]{
                    ChatColor.GRAY + "Détenue par : " + ChatColor.RED + "Personne :(",
                    ChatColor.GRAY + "Score : /"
            }, "none");

            this.setSlotData(ChatColor.GOLD + "2ème place", Material.GOLD_HELMET, 14, new String[]{
                    ChatColor.GRAY + "Détenue par : " + ChatColor.RED + "Personne :(",
                    ChatColor.GRAY + "Score : /"
            }, "none");

            this.setSlotData(ChatColor.WHITE + "3ème place", Material.IRON_HELMET, 12, new String[]{
                    ChatColor.GRAY + "Détenue par : " + ChatColor.RED + "Personne :(",
                    ChatColor.GRAY + "Score : /"
            }, "none");
        }
    }

    private void drawLeaderboardLine()
    {
        Random random = new Random();
        DyeColor color = DyeColor.values()[random.nextInt(DyeColor.values().length)];

        for(int i = 9; i < 18; i++)
        {
            if(this.inventory.getItem(i) != null)
                continue;

            this.setSlotData(ChatColor.GRAY + "", new ItemStack(Material.STAINED_GLASS_PANE, 1, color.getData()), i, null, "");
        }
    }
}
