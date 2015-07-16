package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class PerchedCatDisplayer extends AbstractDisplayer
{
    private final String tag;

    private UUID playerTargetted;
    private BukkitTask waitingTask;
    private BukkitTask waitingInteractionTask;
    private boolean waitingFirstInteraction;
    private boolean waitingSecondInteraction;

    public PerchedCatDisplayer(Player player)
    {
        super(player);

        this.tag = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Chat perché" + ChatColor.GOLD + "] " + ChatColor.RESET;

        this.playerTargetted = null;
        this.waitingTask = null;
        this.waitingFirstInteraction = false;
        this.waitingSecondInteraction = false;
    }

    @Override
    public void display()
    {
        Hub.getInstance().getGuiManager().closeGui(this.player);

        this.player.playSound(this.player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
        this.player.sendMessage(ChatColor.YELLOW + "Pour commencer à jouer, tape un joueur !");
        this.player.sendMessage(ChatColor.RED + "Note: Tu ne peux pas utiliser un autre gadget avant la fin du jeu !");

        this.waitingFirstInteraction = true;

        this.waitingTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () ->
        {
            if(this.waitingFirstInteraction)
            {
                this.player.sendMessage(ChatColor.RED + "Vous n'avez pas provoqué de joueur pendant 10 secondes, le gadget s'annule !");
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
                this.player.setFlying(false);
                ((Player) with).setFlying(false);

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
                        FireworkUtils.launchfw(this.player.getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.ORANGE).withFade(Color.YELLOW).withFlicker().build());
                    }

                    this.waitingSecondInteraction = false;
                    this.end();
                }, 20L * 60);
            }
            else if(this.waitingSecondInteraction && with.getUniqueId().equals(this.playerTargetted))
            {
                this.waitingInteractionTask.cancel();

                Bukkit.broadcastMessage(this.tag + ChatColor.GOLD + with.getName() + ChatColor.YELLOW + " remporte le duel contre " + ChatColor.GOLD + this.player.getName() + ChatColor.YELLOW + " !");

                if(Bukkit.getPlayer(with.getUniqueId()) != null)
                {
                    ((Player) with).playSound(with.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
                    FireworkUtils.launchfw(with.getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.ORANGE).withFade(Color.YELLOW).withFlicker().build());
                }

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
