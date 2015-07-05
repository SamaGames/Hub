package net.samagames.hub.npcs.actions;

import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.AbstractPlayerData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class WelcomeTutorialAction extends HistoryAction
{
    public WelcomeTutorialAction()
    {
        this.history = new Object[][] {
                { ChatColor.GREEN + "Bienvenue sur " + ChatColor.GOLD + "SamaGames" + ChatColor.GREEN + " !", 3 },

                { ChatColor.GREEN + "Je laisse soin à BestDrako de s'occuper du texte qui sera ici !", 3 },
                { ChatColor.GREEN + "Car oui, pour l'instant il est totalement inutile !", 4 },
                { ChatColor.GREEN + "Bon vu que je suis gentil et que je ne sais plus trop quoi mettre...", 4 },
                { ChatColor.GREEN + "Je vais t'offrir un petit cadeau de bienvenue :)", 3 },

                { ChatColor.GREEN + "Bon jeu sur " + ChatColor.GOLD + "SamaGames" + ChatColor.GREEN + " !", 1 }
        };
    }

    @Override
    protected void historyCallback(Player player)
    {
        super.historyCallback(player);

        AbstractPlayerData playerData = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId());

        playerData.creditCoins(20, "Tutoriel de bienvenue !", false);
        playerData.creditStars(5, "Tutoriel de bienvenue !", false);

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
    }
}
