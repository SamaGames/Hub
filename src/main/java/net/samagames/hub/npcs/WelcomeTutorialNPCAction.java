package net.samagames.hub.npcs;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.tools.npc.NPCInteractCallback;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WelcomeTutorialNPCAction implements NPCInteractCallback, Listener
{
    private final Hub hub;
    private final NPCTutorial tutorial;
    private final List<UUID> expected;

    public WelcomeTutorialNPCAction(Hub hub)
    {
        this.hub = hub;
        this.tutorial = new NPCTutorial();
        this.expected = new ArrayList<>();

        /**
         * Chapter I
         * Location: On the wheel of the Dimensions sign
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), -20.5D, 132.0D, -55.1D, -10F, 22.5F), ChatColor.GOLD + "Salut l'aventurier !", Arrays.asList(
                Pair.of("Bienvenue sur SamaGames !", 72L),
                Pair.of("Laissez-moi donc vous guider...", 43L)
        ), Material.GREEN_RECORD.getId()));

        /**
         * Chapter II
         * Location: On the 'S' under the golden apple
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), 36.5D, 133.0D, -4.5D, 89.2F, 26.3F), ChatColor.GOLD + "Kékesé SamaGames ?", Arrays.asList(
                Pair.of("Ceci est un très bon début ;)", 75L),
                Pair.of("SamaGames est un serveur mini-jeux ;", 50L),
                Pair.of("mais nous savons que jouer seul est ennuyant...", 40L),
                Pair.of("Alors n'oubliez pas d'inviter vos amis !", 42L)
        ), Material.RECORD_3.getId()));

        /**
         * Chapter III
         * Location: On a spark of the Uppervoid tnt
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), -10.5D, 131.0D, 70.5D, 179.2F, 19.7F), ChatColor.GOLD + "Où suis-je ?", Arrays.asList(
                Pair.of("Vous êtes actuellement au Hub !", 57L),
                Pair.of("C'est ici que vous vous connectez.", 42L),
                Pair.of("C'est un lieu de détente et de partage ;", 40L),
                Pair.of("entre nos magnifiques joueurs.", 44L)
        ), Material.RECORD_4.getId()));

        /**
         * Chapter IV
         * Location: On front of the DoubleRunner's signs
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), 41.5D, 106.0D, 34.5D, -90.0F, 22.3F), ChatColor.GOLD + "Comment jouer ?", Arrays.asList(
                Pair.of("Voici des panneaux de jeu.", 50L),
                Pair.of("En cliquant dessus vous serez en attente.", 54L),
                Pair.of("Quand assez de joueurs seront en attente ;", 35L),
                Pair.of("votre serveur de jeu démarrera ;", 36L),
                Pair.of("et vous serez ensuite téléportés.", 45L)
        ), Material.RECORD_5.getId()));

        /**
         * Chapter V
         * Location: At the spawn before the Uppervoid's yodel
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), -12.0D, 109.5D, 7.7D, -22.7F, 33.5F), ChatColor.GOLD + "Vroom vroom ?", Arrays.asList(
                Pair.of("Et non ! Ce sont des tyroliennes ;)", 60L),
                Pair.of("Utilisez-les et ce sans modération ;", 44L),
                Pair.of("pour vous déplacer à travers le Hub.", 57L)
        ), Material.RECORD_6.getId()));

        /**
         * Chapter VI
         * Location: In front of a tornado
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), 12.5D, 72.0D, -25.5D, -54.0F, 24.6F), ChatColor.GOLD + "Et si je suis perdu ?", Arrays.asList(
                Pair.of("Ne vous inquiétez pas !", 45L),
                Pair.of("Cherchez les tornades qui vous offriront", 30L),
                Pair.of("un voyage dans le ciel afin de vous retrouver.", 64L)
        ), Material.RECORD_7.getId()));

        /**
         * Chapter VII
         * Location: In front of Meow
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), -9.0D, 110.5D, -1.5D, -59.0F, 38.4F), ChatColor.GOLD + "Qui est Meow ?", Arrays.asList(
                Pair.of("Voici Meow, notre chat.", 60L),
                Pair.of("C'est à lui que vous pourrez récupérer", 36L),
                Pair.of("Certains bonus dont ceux offert avec", 31L),
                Pair.of("l'achat d'un grade.", 35L)
        ), Material.RECORD_8.getId()));

        /**
         * Chapter VIII
         * Location: At the spawn
         */
        this.tutorial.addChapter(new CoveredTutorialChapter(new Location(hub.getWorld(), -11.5D, 119.0D, -1.5D, 180.0F, 90.0F), ChatColor.GOLD + "C'est tout ?", Arrays.asList(
                Pair.of("Oh que non cher ami...", 43L),
                Pair.of("Ce hub regorge de secrets perdus...", 42L),
                Pair.of("A vous de voyager et de les trouver !", 46L),
                Pair.of("Bon jeu sur SamaGames !", 60L)
        ), Material.RECORD_9.getId()));

        this.tutorial.onTutorialEnds((player, interrupted) ->
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

            hub.getServer().getScheduler().runTaskLater(hub, () ->
            {
                player.setResourcePack("http://resources.samagames.net/samareset.zip");
                player.playEffect(player.getLocation(), Effect.RECORD_PLAY, 0);
            }, 20 * 3);
        });

        hub.getServer().getPluginManager().registerEvents(this, hub);
    }

    @EventHandler
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event)
    {
        if (!this.expected.contains(event.getPlayer().getUniqueId()) || event.getStatus() != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)
            return;

        this.expected.remove(event.getPlayer().getUniqueId());

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> this.tutorial.start(event.getPlayer().getUniqueId()), 20L);
    }

    @Override
    public void done(boolean right, Player player)
    {
        if (right)
        {
            player.getInventory().clear();
            player.setResourcePack("http://resources.samagames.net/WelcomeTutorial.zip");

            this.expected.add(player.getUniqueId());
        }
    }
}
