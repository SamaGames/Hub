package net.samagames.hub.npcs;

import net.samagames.hub.Hub;
import net.samagames.tools.npc.NPCInteractCallback;
import net.samagames.tools.tutorials.Tutorial;
import net.samagames.tools.tutorials.TutorialChapter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class WelcomeTutorialNPCAction implements NPCInteractCallback
{
    private final Tutorial tuturial;

    public WelcomeTutorialNPCAction(Hub hub)
    {
        this.tuturial = new Tutorial();

        /**
         * Chapter I
         * Location: On the wheel of the Dimensions sign
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -18.5D, 130.0D, -55.5D, -7.1F, 23.5F), ChatColor.GOLD + "Salut l'aventurié !", Arrays.asList(new String[] {
                "Bienvenue sur SamaGames !",
                "Laissez-moi donc vous guider...",
        }), true));

        /**
         * Chapter II
         * Location: On the 'S' under the golden apple
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 36.5D, 131.0D, -4.5D, 86.2F, 26.9F), ChatColor.GOLD + "Kékesé SamaGames ?", Arrays.asList(new String[] {
                "Ceci est un très bon début ;)",
                "SamaGames est un serveur mini-jeux ;",
                "mais nous savons que jouer seul est ennuyant...",
                "Alors n'oubliez pas d'inviter vos amis !"
        }), true));

        /**
         * Chapter III
         * Location: On a spark of the Uppervoid tnt
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -6.5D, 131.0D, 70.5D, 176.1F, 18.0F), ChatColor.GOLD + "Où suis-je ?", Arrays.asList(new String[] {
                "Vous êtes actuellement au Hub !",
                "C'est ici que vous vous connectez.",
                "C'est un lieu de détente et de partage ;",
                "entre nos magnifiques joueurs."
        }), true));

        /**
         * Chapter IV
         * Location: On front of the DoubleRunner's signs
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), 44.5D, 104.0D, 34.5D, -90.0F, 0.0F), ChatColor.GOLD + "Comment jouer ?", Arrays.asList(new String[] {
                "Voici des panneaux de jeu.",
                "En cliquant dessus vous serez en attente.",
                "Quand assez de joueurs seront en attente ;",
                "votre serveur de jeu démarrera ;",
                "et vous serez ensuité téléportés."
        }), true));

        /**
         * Chapter V
         * Location: At the spawn before the Uppervoid's yodel
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -12.0D, 109.5D, 7.7D, -22.7F, 33.5F), ChatColor.GOLD + "Vroom vroom ?", Arrays.asList(new String[] {
                "Et non ! Ce sont des tyroliennes ;)",
                "Utilisez-les et ce sans modération ;",
                "pour vous déplacer à travers le Hub."
        }), true));

        /**
         * Chapter VI
         * Location: At the spawn before the Uppervoid's yodel
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -12.0D, 109.5D, 7.7D, -22.7F, 33.5F), ChatColor.GOLD + "Et si je suis perdu ?", Arrays.asList(new String[] {
                "Ne vous inquiétez pas !",
                "Cherchez les tornades qui nous offrirons ;",
                "un voyage dans le ciel pour vous retrouver."
        }), true));

        /**
         * Chapter VII
         * Location: On front of Meow
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -8.5D, 108.0D, -1.5D, -56.7F, 0.0F), ChatColor.GOLD + "Qui est Meow ?", Arrays.asList(new String[] {
                "Voici Meow, notre chat.",
                "Il est encore en train d'emménager...",
                "Allez parfois prendre de ses nouvelles ;",
                "il aura beaucoup à vous dire bientôt..."
        }), true));

        /**
         * Chapter VIII
         * Location: At the spawn
         */
        this.tuturial.addChapter(new TutorialChapter(new Location(hub.getWorld(), -11.5D, 119.0D, -1.5D, 180.0F, 90.0F), ChatColor.GOLD + "C'est tout ?", Arrays.asList(new String[] {
                "Oh que non cher ami...",
                "Ce hub regorge de secret perdus...",
                "A vous de voyager et de les trouver !",
                "Bon jeu sur SamaGames !"
        }), true));
    }

    @Override
    public void done(boolean right, Player player)
    {
        this.tuturial.start(player.getUniqueId());
    }
}
