package net.samagames.hub.npcs.actions;

import net.md_5.bungee.api.ChatColor;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.PlayerData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class FriendTutorialAction extends HistoryAction
{
    public FriendTutorialAction()
    {
        this.history = new Object[][] {
                { ChatColor.GREEN + "Bienvenue sur " + ChatColor.GOLD + "SamaGames" + ChatColor.GREEN + " !", 3 },

                { ChatColor.GREEN + "Jouer seul est parfois désolant, c'est pour cette raison qu'un système d'amis est présent sur le serveur !", 6 },
                { ChatColor.GREEN + "Pour l'utiliser, c'est " + ChatColor.GOLD + "très simple" + ChatColor.GREEN + ":", 4 },
                { ChatColor.GREEN + "Accédez à ce système via la commande " + ChatColor.GOLD + "/friends" + ChatColor.GREEN + " ou " + ChatColor.GOLD + "/f" + ChatColor.GREEN + ", cela revient au même.", 5 },
                { ChatColor.GOLD + "/friends add <Joueur>" + ChatColor.GREEN + " vous permet d'inviter le joueur donné en ami.", 6 },
                { ChatColor.GREEN + "Il devra alors cliquer sur " + ChatColor.BOLD + "[Accepter]" + ChatColor.RESET + ChatColor.GREEN + " ou " + ChatColor.RED + ChatColor.BOLD + "[Refuser]" + ChatColor.RESET + ChatColor.GREEN + " pour accepter ou refuser votre demande.", 6 },
                { ChatColor.GREEN + "Quant à elle, " + ChatColor.GOLD + "/friends requests" + ChatColor.GREEN + " vous permet de voir vos demandes d'amis ;", 6 },
                { ChatColor.GREEN + "Tandis que la commande " + ChatColor.GOLD + "/friends list" + ChatColor.GREEN + " vous permet de voir votre liste d'amis.", 6 },
                { ChatColor.GREEN + "Une dernière chose, " + ChatColor.GOLD + "/friends tp <Joueur>" + ChatColor.GREEN + " vous permet de vous téléporter.", 6 },
                { ChatColor.GREEN + "A un de vos " + ChatColor.GOLD + "amis" + ChatColor.GREEN + " bien sûr ! En jeu si la partie n'est pas commencée, ou sur le même hub !", 5 },
                { ChatColor.GREEN + "Vous êtes fin prêt pour ajouter tous vos amis !", 4 },

                { ChatColor.GREEN + "Bon jeu sur " + ChatColor.GOLD + "SamaGames" + ChatColor.GREEN + " !", 1 }
        };
    }

    @Override
    protected void historyCallback(Player player)
    {
        super.historyCallback(player);

        PlayerData playerData = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId());

        //if(!SamaGamesAPI.get().getAchievementManager().isUnlocked(player, "lobby.tutorial.friends"))
        {
            //SamaGamesAPI.get().getAchievementManager().getAchievementByName("lobby.tutorial.friends").unlock(player);
            playerData.creditCoins(20, "Tutoriel sur les amis !", false);
            playerData.creditStars(5, "Tutoriel sur les amis !", false);

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        }
    }
}
