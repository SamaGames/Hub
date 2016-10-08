package net.samagames.hub.npcs;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.npc.NPCInteractCallback;
import net.samagames.tools.tutorials.TutorialChapter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

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
                Pair.of("Bienvenue sur SamaGames !", 50L),
                Pair.of("Laissez-moi donc vous guider...", 50L)
        ), true));

        /**
         * Chapter II
         * Location: On the 'S' under the golden apple
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 36.5D, 133.0D, -4.5D, 89.2F, 26.3F), ChatColor.GOLD + "Kékesé SamaGames ?", Arrays.asList(
                Pair.of("Ceci est un très bon début ;)", 50L),
                Pair.of("SamaGames est un serveur mini-jeux ;", 50L),
                Pair.of("mais nous savons que jouer seul est ennuyant...", 50L),
                Pair.of("Alors n'oubliez pas d'inviter vos amis !", 50L)
        ), true));

        /**
         * Chapter III
         * Location: On a spark of the Uppervoid tnt
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -10.5D, 131.0D, 70.5D, 179.2F, 19.7F), ChatColor.GOLD + "Où suis-je ?", Arrays.asList(
                Pair.of("Vous êtes actuellement au Hub !", 50L),
                Pair.of("C'est ici que vous vous connectez.", 50L),
                Pair.of("C'est un lieu de détente et de partage ;", 50L),
                Pair.of("entre nos magnifiques joueurs.", 50L)
        ), true));

        /**
         * Chapter IV
         * Location: On front of the DoubleRunner's signs
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 41.5D, 106.0D, 34.5D, -90.0F, 22.3F), ChatColor.GOLD + "Comment jouer ?", Arrays.asList(
                Pair.of("Voici des panneaux de jeu.", 50L),
                Pair.of("En cliquant dessus vous serez en attente.", 50L),
                Pair.of("Quand assez de joueurs seront en attente ;", 50L),
                Pair.of("votre serveur de jeu démarrera ;", 50L),
                Pair.of("et vous serez ensuite téléportés.", 50L)
        ), true));

        /**
         * Chapter V
         * Location: At the spawn before the Uppervoid's yodel
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -12.0D, 109.5D, 7.7D, -22.7F, 33.5F), ChatColor.GOLD + "Vroom vroom ?", Arrays.asList(
                Pair.of("Et non ! Ce sont des tyroliennes ;)", 50L),
                Pair.of("Utilisez-les et ce sans modération ;", 50L),
                Pair.of("pour vous déplacer à travers le Hub.", 50L)
        ), true));

        /**
         * Chapter VI
         * Location: In front of a tornado
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 12.5D, 72.0D, -25.5D, -54.0F, 24.6F), ChatColor.GOLD + "Et si je suis perdu ?", Arrays.asList(
                Pair.of("Ne vous inquiétez pas !", 50L),
                Pair.of("Cherchez les tornades qui vous offrirons", 50L),
                Pair.of("un voyage dans le ciel afin de vous retrouver.", 50L)
        ), true));

        /**
         * Chapter VII
         * Location: In front of Meow
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -9.0D, 110.5D, -1.5D, -59.0F, 38.4F), ChatColor.GOLD + "Qui est Meow ?", Arrays.asList(
                Pair.of("Voici Meow, notre chat.", 50L),
                Pair.of("C'est à lui que vous pourrez récupérer", 50L),
                Pair.of("Certains bonus dont ceux offert avec", 50L),
                Pair.of("l'achat d'un grade.", 50L)
        ), true));

        /**
         * Chapter VIII
         * Location: At the spawn
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -11.5D, 119.0D, -1.5D, 180.0F, 90.0F), ChatColor.GOLD + "C'est tout ?", Arrays.asList(
                Pair.of("Oh que non cher ami...", 50L),
                Pair.of("Ce hub regorge de secrets perdus...", 50L),
                Pair.of("A vous de voyager et de les trouver !", 50L),
                Pair.of("Bon jeu sur SamaGames !", 50L)
        ), true));

        this.tuturial.onTutorialEnds((player, interrupted) ->
        {
            if (interrupted)
                return;

            hub.getServer().getScheduler().runTask(hub, () ->
            {
                SamaGamesAPI.get().getAchievementManager().getAchievementByID(2).unlock(player.getUniqueId());

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
