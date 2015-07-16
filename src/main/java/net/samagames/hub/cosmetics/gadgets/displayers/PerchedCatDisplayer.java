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
    private final String tag;

    private UUID playerTargeted;
    private BukkitTask waitingTask;
    private BukkitTask waitingInteractionTask;
    private boolean waitingFirstInteraction;
    private boolean waitingSecondInteraction;
    private boolean playerWasFlying;
    private boolean targetWasFlying;

    public PerchedCatDisplayer(Player player)
    {
        super(player);

        this.tag = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Chat perché" + ChatColor.GOLD + "] " + ChatColor.RESET;

        this.playerTargeted = null;
        this.waitingTask = null;
        this.waitingFirstInteraction = false;
        this.waitingSecondInteraction = false;
    }

    @Override
    public void display()
    {
        Hub.getInstance().getGuiManager().closeGui(this.player);

        this.player.playSound(this.player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
        this.player.sendMessage(this.tag + ChatColor.YELLOW + "Pour commencer à jouer, tape un joueur !");

        this.waitingFirstInteraction = true;

        this.waitingTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () ->
        {
            if(this.waitingFirstInteraction)
            {
                this.player.sendMessage(this.tag + ChatColor.RED + "Vous n'avez pas provoqué de joueur pendant 10 secondes, le gadget s'annule !");
                this.end();
            }

            this.waitingTask.cancel();
        }, 20L * 10);
    }

    @Override
    public void handleInteraction(Entity who, Entity with)
    {
        if(who instanceof Player && with instanceof Player)
        {
            if(this.waitingFirstInteraction && who.getUniqueId().equals(this.player.getUniqueId()))
            {
                this.playerTargeted = with.getUniqueId();

                this.playerWasFlying = this.player.isFlying();
                this.targetWasFlying = ((Player) with).isFlying();

                this.player.setFlying(false);
                this.player.setAllowFlight(false);
                ((Player) with).setFlying(false);
                ((Player) with).setAllowFlight(false);

                Bukkit.broadcastMessage(this.tag + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " a provoqué " + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + " en duel !");

                this.player.playSound(with.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
                ((Player) with).playSound(with.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);

                this.waitingFirstInteraction = false;
                this.waitingSecondInteraction = true;

                this.waitingInteractionTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () ->
                {
                    Bukkit.broadcastMessage(this.tag + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " remporte le duel contre " + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + " !");

                    if(Bukkit.getPlayer(this.player.getUniqueId()) != null)
                    {
                        this.player.playSound(this.player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
                        this.player.setAllowFlight(this.playerWasFlying);
                    }

                    if(Bukkit.getPlayer(with.getUniqueId()) != null)
                        ((Player) with).setAllowFlight(this.targetWasFlying);

                    this.waitingSecondInteraction = false;
                    this.end();
                }, 20L * 60);
            }
            else if(this.waitingSecondInteraction && who.getUniqueId().equals(this.playerTargeted))
            {
                this.waitingInteractionTask.cancel();

                Bukkit.broadcastMessage(this.tag + ChatColor.GOLD + who.getName() + ChatColor.YELLOW + " remporte le duel contre " + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " !");

                if(Bukkit.getPlayer(who.getUniqueId()) != null)
                {
                    ((Player) who).playSound(who.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
                    ((Player) who).setAllowFlight(this.targetWasFlying);
                }

                if(Bukkit.getPlayer(this.player.getUniqueId()) != null)
                    this.player.setAllowFlight(this.playerWasFlying);

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
