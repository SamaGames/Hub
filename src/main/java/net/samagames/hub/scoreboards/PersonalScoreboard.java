package net.samagames.hub.scoreboards;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.AbstractPlayerData;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.NumberUtils;
import net.samagames.hub.utils.RankUtils;
import net.samagames.tools.scoreboards.ObjectiveSign;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PersonalScoreboard
{
    private final Hub hub;
    private final UUID player;
    private final ObjectiveSign objectiveSign;

    private String formattedRank;
    private long coins;
    private long stars;

    public PersonalScoreboard(Hub hub, Player player)
    {
        this.hub = hub;
        this.player = player.getUniqueId();

        this.objectiveSign = new ObjectiveSign(SamaGamesAPI.get().getServerName().toLowerCase(), "SamaGames");
        this.reloadData();
        this.objectiveSign.addReceiver(player);
        this.setLines();
    }

    public void onLogout()
    {
        this.objectiveSign.removeReceiver(this.hub.getServer().getOfflinePlayer(this.player));
    }

    public void reloadData()
    {
        AbstractPlayerData playerData = SamaGamesAPI.get().getPlayerManager().getPlayerData(this.player);

        this.formattedRank = RankUtils.getFormattedRank(this.player);
        this.coins = playerData.getCoins();
        this.stars = playerData.getStars();

        this.setLines();
    }

    private void setLines()
    {
        this.objectiveSign.setDisplayName(ChatColor.GOLD + "\u2726" + ChatColor.BOLD + " SamaGames " + ChatColor.RESET + ChatColor.GOLD + "\u2726");

        this.objectiveSign.setLine(0, ChatColor.BLUE + "");
        this.objectiveSign.setLine(1, ChatColor.GREEN + "" + ChatColor.BOLD + "Serveur");
        this.objectiveSign.setLine(2, ChatColor.GRAY + "Hub " + SamaGamesAPI.get().getServerName().split("_")[1]);
        this.objectiveSign.setLine(3, ChatColor.AQUA + "");
        this.objectiveSign.setLine(4, ChatColor.RED + "" + ChatColor.BOLD + "Rang");
        this.objectiveSign.setLine(5, this.formattedRank);
        this.objectiveSign.setLine(6, ChatColor.GREEN + "");
        this.objectiveSign.setLine(7, ChatColor.GOLD + "" + ChatColor.BOLD + "Pièces");
        this.objectiveSign.setLine(8, ChatColor.GRAY + "" + NumberUtils.format(this.coins));
        this.objectiveSign.setLine(9, ChatColor.DARK_GREEN + "");
        this.objectiveSign.setLine(10, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Étoiles");
        this.objectiveSign.setLine(11, ChatColor.GRAY + "" + NumberUtils.format(this.stars));
        this.objectiveSign.setLine(12, ChatColor.BLACK + "");
        this.objectiveSign.setLine(13, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "TeamSpeak");
        this.objectiveSign.setLine(14, ChatColor.GRAY + "ts.samagames.net");

        this.objectiveSign.updateLines();
    }
}
