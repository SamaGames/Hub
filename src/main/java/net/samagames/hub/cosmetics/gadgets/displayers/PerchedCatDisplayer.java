package net.samagames.hub.cosmetics.gadgets.displayers;

import net.md_5.bungee.api.ChatColor;
import net.samagames.hub.Hub;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class PerchedCatDisplayer extends AbstractDisplayer
{
    private static final String TAG = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Chat perché" + ChatColor.DARK_AQUA + "] " + ChatColor.RESET;

    private UUID playerTargeted;
    private BukkitTask waitingTask;
    private BukkitTask waitingInteractionTask;
    private boolean waitingFirstInteraction;
    private boolean waitingSecondInteraction;
    private boolean playerWasFlying;
    private boolean targetWasFlying;

    public PerchedCatDisplayer(Hub hub, Player player)
    {
        super(hub, player);

        this.playerTargeted = null;
        this.waitingTask = null;
        this.waitingFirstInteraction = false;
        this.waitingSecondInteraction = false;
    }

    @Override
    public void display()
    {
        this.player.playSound(this.player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
        this.player.sendMessage(TAG + ChatColor.YELLOW + "Pour commencer à jouer, tapez un joueur !");

        this.waitingFirstInteraction = true;

        this.waitingTask = this.hub.getServer().getScheduler().runTaskLaterAsynchronously(this.hub, () ->
        {
            if (this.waitingFirstInteraction)
            {
                this.player.playSound(this.player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
                this.player.sendMessage(TAG + ChatColor.RED + "Vous n'avez pas provoqué de joueur pendant 10 secondes, le gadget s'annule !");
                this.end();
            }

            this.waitingTask.cancel();
        }, 20L * 10);
    }

    @Override
    public void handleInteraction(Entity who, Entity with)
    {
        if (who instanceof Player && with instanceof Player)
        {
            if (this.waitingFirstInteraction && who.getUniqueId().equals(this.player.getUniqueId()))
            {
                this.playerTargeted = with.getUniqueId();
                this.interactWith((Player) with);

                this.playerWasFlying = this.player.getAllowFlight();
                this.targetWasFlying = ((Player) with).getAllowFlight();

                this.player.setFlying(false);
                this.player.setAllowFlight(false);
                ((Player) with).setFlying(false);
                ((Player) with).setAllowFlight(false);

                this.player.sendMessage(TAG + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " a provoqué " + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + " en duel !");
                with.sendMessage(TAG + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " a provoqué " + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + " en duel !");

                this.player.playSound(with.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
                ((Player) with).playSound(with.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);

                this.waitingFirstInteraction = false;
                this.waitingSecondInteraction = true;

                this.waitingInteractionTask = this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
                {
                    this.player.sendMessage(TAG + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " a provoqué " + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + " en duel !");
                    with.sendMessage(TAG + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " a provoqué " + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + " en duel !");

                    if (this.hub.getServer().getPlayer(this.player.getUniqueId()) != null)
                    {
                        this.player.playSound(this.player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
                        this.player.setAllowFlight(this.playerWasFlying);
                    }

                    if (this.hub.getServer().getPlayer(with.getUniqueId()) != null)
                        ((Player) with).setAllowFlight(this.targetWasFlying);

                    this.waitingSecondInteraction = false;
                    this.end();
                }, 20L * 60);
            }
            else if (this.waitingSecondInteraction && who.getUniqueId().equals(this.playerTargeted))
            {
                this.waitingInteractionTask.cancel();

                this.player.sendMessage(TAG + ChatColor.GOLD + who.getName() + ChatColor.YELLOW + " remporte le duel contre " + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " !");
                who.sendMessage(TAG + ChatColor.GOLD + who.getName() + ChatColor.YELLOW + " remporte le duel contre " + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " !");

                if (this.hub.getServer().getPlayer(who.getUniqueId()) != null)
                {
                    ((Player) who).playSound(who.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
                    ((Player) who).setAllowFlight(this.targetWasFlying);
                }

                if (this.hub.getServer().getPlayer(this.player.getUniqueId()) != null)
                    this.player.setAllowFlight(this.playerWasFlying);

                this.waitingSecondInteraction = false;
                this.end();
            }
        }
    }

    @Override
    public boolean isInteractionsEnabled()
    {
        return true;
    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
