package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class PerchedCatDisplayer extends AbstractDisplayer
{
    private UUID playerTargetted;
    private BukkitTask waitingTask;
    private BukkitTask waitingInteractionTask;
    private boolean waitingFirstInteraction;
    private boolean waitingSecondInteraction;

    public PerchedCatDisplayer(Player player)
    {
        super(player);

        this.playerTargetted = null;
        this.waitingTask = null;
        this.waitingFirstInteraction = false;
        this.waitingSecondInteraction = false;
    }

    @Override
    public void display()
    {
        this.player.playSound(this.player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
        this.player.sendMessage(ChatColor.YELLOW + "Pour commencer à jouer, tape un joueur !");
        this.player.sendMessage(ChatColor.RED + "Note: Tu ne peux pas utiliser un autre gadget avant la fin du jeu !");

        this.waitingFirstInteraction = true;

        this.waitingTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () ->
        {
            if(this.waitingFirstInteraction)
            {
                this.player.sendMessage(ChatColor.RED + "Vous n'avez pas provoqués de joueur pendant 10 seconde, le gadget s'annule !");
                this.end();
            }

            this.waitingTask.cancel();
        }, 20L * 10);
    }

    @Override
    public void handleInteraction(Entity with)
    {
        if(with instanceof Player)
        {
            if(this.waitingFirstInteraction && !with.getUniqueId().equals(this.player.getUniqueId()))
            {
                this.playerTargetted = with.getUniqueId();

                this.player.sendMessage(ChatColor.YELLOW + "Vous avez provoqué " + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + "en duel ! Il a " + ChatColor.GOLD + "1 minute" + ChatColor.YELLOW + " pour tenter de vous attraper !");
                with.sendMessage(ChatColor.RED + "Vous avez été provoqué en duel par " + ChatColor.DARK_RED + this.player.getName() + ChatColor.RED + " essayez de l'attraper en moins d'" + ChatColor.DARK_RED + "1 minute" + ChatColor.RED + " sinon, vous perdez !");

                this.waitingFirstInteraction = false;
                this.waitingSecondInteraction = true;

                this.waitingInteractionTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () ->
                {
                    this.player.sendMessage(ChatColor.GREEN + "Vous remportez le duel contre " + with.getName() + "!");
                    with.sendMessage(ChatColor.RED + "Vous perdez le duel contre " + this.player.getName() + "!");

                    this.waitingSecondInteraction = false;
                    this.end();
                }, 20L * 60);
            }
            else if(this.waitingSecondInteraction && with.getUniqueId().equals(this.playerTargetted))
            {
                this.waitingInteractionTask.cancel();

                with.sendMessage(ChatColor.GREEN + "Vous remportez le duel contre " + this.player.getName() + "!");
                this.player.sendMessage(ChatColor.RED + "Vous perdez le duel contre " + with.getName() + "!");

                this.waitingSecondInteraction = false;
                this.end();
            }
        }
    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
