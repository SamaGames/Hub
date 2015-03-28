package net.samagames.hub.npcs.actions;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PartyTutorialAction extends HistoryAction
{
    public PartyTutorialAction()
    {
        this.history = new Object[][] {
                { ChatColor.RED + "Prochainement :)", 5 }
        };
    }

    @Override
    protected void historyCallback(Player player)
    {
        super.historyCallback(player);

        PlayerData playerData = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId());

        //if(!SamaGamesAPI.get().getAchievementManager().isUnlocked(player, "lobby.tutorial.party"))
        {
            //SamaGamesAPI.get().getAchievementManager().getAchievementByName("lobby.tutorial.party").unlock(player);
            playerData.creditCoins(20, "Tutoriel sur les groupes !", false);
            playerData.creditStars(5, "Tutoriel sur les groupes !", false);

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        }
    }
}
