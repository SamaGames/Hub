package net.samagames.hub.npcs;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.npc.NPCInteractCallback;
import net.samagames.tools.tutorials.Tutorial;
import net.samagames.tools.tutorials.TutorialChapter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.function.Consumer;

public class WelcomeTutorialNPCAction implements NPCInteractCallback
{
    private final NPCTutorial tuturial;

    public WelcomeTutorialNPCAction(Hub hub)
    {
        this.tuturial = new NPCTutorial();

        /**
         * Chapter I
         * Location: On the wheel of the Dimensions sign
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -20.5D, 132.0D, -55.1D, -10F, 22.5F), ChatColor.GOLD + "Salut l'aventurier !", Arrays.asList(
                "Bienvenue sur SamaGames !",
                "Laissez-moi donc vous guider..."
        ), true));

        /**
         * Chapter II
         * Location: On the 'S' under the golden apple
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 36.5D, 133.0D, -4.5D, 89.2F, 26.3F), ChatColor.GOLD + "Kékesé SamaGames ?", Arrays.asList(
                "Ceci est un très bon début ;)",
                "SamaGames est un serveur mini-jeux ;",
                "mais nous savons que jouer seul est ennuyant...",
                "Alors n'oubliez pas d'inviter vos amis !"
        ), true));

        /**
         * Chapter III
         * Location: On a spark of the Uppervoid tnt
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -10.5D, 131.0D, 70.5D, 179.2F, 19.7F), ChatColor.GOLD + "Où suis-je ?", Arrays.asList(
                "Vous êtes actuellement au Hub !",
                "C'est ici que vous vous connectez.",
                "C'est un lieu de détente et de partage ;",
                "entre nos magnifiques joueurs."
        ), true));

        /**
         * Chapter IV
         * Location: On front of the DoubleRunner's signs
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 41.5D, 106.0D, 34.5D, -90.0F, 22.3F), ChatColor.GOLD + "Comment jouer ?", Arrays.asList(
                "Voici des panneaux de jeu.",
                "En cliquant dessus vous serez en attente.",
                "Quand assez de joueurs seront en attente ;",
                "votre serveur de jeu démarrera ;",
                "et vous serez ensuite téléportés."
        ), true));

        /**
         * Chapter V
         * Location: At the spawn before the Uppervoid's yodel
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -12.0D, 109.5D, 7.7D, -22.7F, 33.5F), ChatColor.GOLD + "Vroom vroom ?", Arrays.asList(
                "Et non ! Ce sont des tyroliennes ;)",
                "Utilisez-les et ce sans modération ;",
                "pour vous déplacer à travers le Hub."
        ), true));

        /**
         * Chapter VI
         * Location: In front of a tornado
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 12.5D, 72.0D, -25.5D, -54.0F, 24.6F), ChatColor.GOLD + "Et si je suis perdu ?", Arrays.asList(
                "Ne vous inquiétez pas !",
                "Cherchez les tornades qui vous offrirons",
                "un voyage dans le ciel afin de vous retrouver."
        ), true));

        /**
         * Chapter VII
         * Location: In front of Meow
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -9.0D, 110.5D, -1.5D, -59.0F, 38.4F), ChatColor.GOLD + "Qui est Meow ?", Arrays.asList(
                "Voici Meow, notre chat.",
                "C'est à lui que vous pourrez récupérer",
                "Certains bonus dont ceux offert avec",
                "l'achat d'un grade."
        ), true));

        /**
         * Chapter VIII
         * Location: At the spawn
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -11.5D, 119.0D, -1.5D, 180.0F, 90.0F), ChatColor.GOLD + "C'est tout ?", Arrays.asList(
                "Oh que non cher ami...",
                "Ce hub regorge de secrets perdus...",
                "A vous de voyager et de les trouver !",
                "Bon jeu sur SamaGames !"
        ), true));

        this.tuturial.onTutorialEnds((player, interrupted) ->
        {
            if (interrupted)
                return;

            hub.getServer().getScheduler().runTask(hub, () ->
            {
                SamaGamesAPI.get().getAchievementManager().getAchievementByID(1).unlock(player.getUniqueId());

                if (player.hasPermission("hub.fly"))
                    player.setAllowFlight(true);

                hub.getPlayerManager().getStaticInventory().setInventoryToPlayer(player);
            });
        });
    }

    @Override
    public void done(boolean right, Player player)
    {
        if (right)
        {
            player.getInventory().clear();
            this.tuturial.start(player.getUniqueId());
        }
    }
}
