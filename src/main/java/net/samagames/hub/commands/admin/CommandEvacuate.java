package net.samagames.hub.commands.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEvacuate extends AbstractCommand
{
    private boolean lock;
    private int timer;

    public CommandEvacuate(Hub hub)
    {
        super(hub);

        this.lock = false;
        this.timer = 60;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (sender instanceof Player)
        {
            if (!this.hasPermission((Player) sender))
            {
                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
                return true;
            }
        }
        
        if (this.lock)
            return true;

        this.lock = true;

        if (args.length != 1)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /evacuate <destination>");
            return true;
        }

        this.hub.getServer().getScheduler().runTaskTimerAsynchronously(this.hub, () ->
        {
            if (this.timer == 60 || this.timer == 30 || this.timer == 10 || (this.timer <= 5 && this.timer > 0))
                this.hub.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Votre hub va redémarrer dans " + ChatColor.AQUA + ChatColor.BOLD + this.timer + " seconde" + (this.timer > 1 ? "s" : ""));

            this.hub.getServer().getOnlinePlayers().stream().filter(p -> this.timer > 0).forEach(p ->
            {
                if (this.timer <= 5)
                    p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.8F, 1.8F);
                else if (this.timer > 5 && this.timer <= 30)
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.8F, 1.0F);

                Titles.sendTitle(p, 0, 22, 0, ChatColor.RED + "Attention !", ChatColor.GOLD + "Votre hub va redémarrer dans " + ChatColor.AQUA + this.timer + " seconde" + (this.timer > 1 ? "s" : ""));
            });

            if (this.timer == 0)
            {
                this.hub.getServer().broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Fermeture du hub...");

                for (Player p : this.hub.getServer().getOnlinePlayers())
                    p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.9F, 1.0F);
            }
            else if (this.timer == -1)
            {
                for (Player p : this.hub.getServer().getOnlinePlayers())
                {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(args[0]);
                    p.sendPluginMessage(this.hub, "BungeeCord", out.toByteArray());
                }

                this.hub.getServer().getScheduler().runTaskLaterAsynchronously(this.hub, () -> this.hub.getServer().shutdown(), 3 * 20L);
            }

            this.timer--;
        }, 20L, 20L);

        return true;
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        return true;
    }
}
