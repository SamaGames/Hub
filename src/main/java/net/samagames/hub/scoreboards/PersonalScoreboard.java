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
class PersonalScoreboard
{
    private final Hub hub;
    private final UUID player;
    private final ObjectiveSign objectiveSign;

    private String formattedRank;
    private long coins;
    private long powders;
    private int pearls;

    PersonalScoreboard(Hub hub, Player player)
    {
        this.hub = hub;
        this.player = player.getUniqueId();

        this.objectiveSign = new ObjectiveSign(SamaGamesAPI.get().getServerName().toLowerCase(), "SamaGames");

        this.reloadData();
        this.objectiveSign.addReceiver(player);
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
        this.powders = playerData.getPowders();
        this.pearls = this.hub.getInteractionManager().getGraouManager().getPlayerPearls(this.player).size();
    }

    public void setLines(String ip)
    {
        this.objectiveSign.setDisplayName(ChatColor.GOLD + "\u2726" + ChatColor.RED + ChatColor.BOLD + " SamaGames " + ChatColor.RESET + ChatColor.GOLD + "\u2726");

        this.objectiveSign.setLine(0, ChatColor.BLUE + "");

        this.objectiveSign.setLine(1, ChatColor.GRAY + "Hub : " + ChatColor.WHITE + "#" + SamaGamesAPI.get().getServerName().split("_")[1]);
        this.objectiveSign.setLine(2, ChatColor.GRAY + "Grade : " + this.formattedRank);

        this.objectiveSign.setLine(3, ChatColor.GREEN + "");

        this.objectiveSign.setLine(4, ChatColor.GRAY + "Pièces : " + ChatColor.GOLD + NumberUtils.format(this.coins) + " \u26C1");
        this.objectiveSign.setLine(5, ChatColor.GRAY + "Perles : " + ChatColor.GREEN + NumberUtils.format(this.pearls) + " \u25C9");
        this.objectiveSign.setLine(6, ChatColor.GRAY + "Poussières d'" + ChatColor.AQUA + "\u272F" + ChatColor.GRAY + " : " + ChatColor.AQUA + NumberUtils.format(this.powders) + " \u2055");

        this.objectiveSign.setLine(7, ChatColor.RED + "");

        this.objectiveSign.setLine(8, ChatColor.YELLOW + ip);

        this.objectiveSign.updateLines();
    }
}
