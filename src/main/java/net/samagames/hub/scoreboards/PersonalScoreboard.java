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

class PersonalScoreboard
{
    private final Hub hub;
    private final UUID player;
    private final ObjectiveSign objectiveSign;

    private String formattedRank;
    private long coins;
    private long stars;
    private int ipCharIndex;

    PersonalScoreboard(Hub hub, Player player)
    {
        this.hub = hub;
        this.player = player.getUniqueId();
        this.ipCharIndex = 1;

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

        this.formattedRank = RankUtils.getFormattedRank(this.player, true);
        this.coins = playerData.getCoins();
        this.stars = playerData.getStars();
    }

    public void setLines()
    {
        this.objectiveSign.setDisplayName(ChatColor.GOLD + "\u2726" + ChatColor.BOLD + " SamaGames " + ChatColor.RESET + ChatColor.GOLD + "\u2726");

        this.objectiveSign.setLine(0, ChatColor.BLUE + "");

        this.objectiveSign.setLine(1, ChatColor.GRAY + "Hub : " + ChatColor.WHITE + "#" + SamaGamesAPI.get().getServerName().split("_")[1]);
        this.objectiveSign.setLine(2, ChatColor.GRAY + "Grade : " + this.formattedRank);

        this.objectiveSign.setLine(3, ChatColor.GREEN + "");

        this.objectiveSign.setLine(4, ChatColor.GRAY + "Pièces : " + ChatColor.GOLD + NumberUtils.format(this.coins) + " \u26C1");
        this.objectiveSign.setLine(5, ChatColor.GRAY + "Étoiles : " + ChatColor.AQUA + NumberUtils.format(this.stars) + " \u2606");

        this.objectiveSign.setLine(6, ChatColor.RED + "");

        this.objectiveSign.setLine(7, ChatColor.YELLOW + this.colorIpAt());

        this.objectiveSign.updateLines();
    }

    private String colorIpAt()
    {
        String ip = "mc.samagames.net";
        ip = ip.substring(0, this.ipCharIndex - 1) + ChatColor.RED + ip.charAt(this.ipCharIndex) + (this.ipCharIndex + 1 < ip.length() ? ChatColor.YELLOW + ip.substring(this.ipCharIndex + 1) : "");

        this.ipCharIndex++;

        if (this.ipCharIndex == ip.length() - 1)
            this.ipCharIndex = 1;

        return ip;
    }
}
