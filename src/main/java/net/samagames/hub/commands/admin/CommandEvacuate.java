package net.samagames.hub.commands.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.tools.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEvacuate extends AbstractCommand
{
    private boolean lock;

    public CommandEvacuate()
    {
        this.lock = false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender instanceof Player)
        {
            if(!hasPermission((Player) sender))
            {
                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
                return true;
            }
        }
        
        if(this.lock)
            return true;

        this.lock = true;

        if(args.length != 1)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /evacuate <Destination>");
            return true;
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), new Runnable()
        {
            int timer = 60;

            @Override
            public void run()
            {
                if (this.timer == 60 || this.timer == 30 || this.timer == 10 || (this.timer <= 5 && this.timer > 0))
                    Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Votre hub va redémarrer dans " + ChatColor.AQUA + ChatColor.BOLD + this.timer + " seconde" + (this.timer > 1 ? "s" : ""));

                Bukkit.getOnlinePlayers().stream().filter(p -> this.timer > 0).forEach(p ->
                {
                    if (this.timer <= 5)
                        p.playSound(p.getLocation(), Sound.BLAZE_DEATH, 0.8F, 1.8F);
                    else if (this.timer > 10 && this.timer <= 30)
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 0.8F, 1.0F);

                    if (sender instanceof Player)
                        Titles.sendTitle((Player) sender, 0, 22, 0, ChatColor.RED + "Attention !", ChatColor.GOLD + "Votre hub va redémarrer dans " + ChatColor.AQUA + this.timer + " seconde" + (this.timer > 1 ? "s" : ""));
                });

                if (this.timer == 0)
                {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Fermeture du hub...");

                    for (Player p : Bukkit.getOnlinePlayers())
                    {
                        p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 0.9F, 1.0F);
                    }
                }
                else if (this.timer == -1)
                {
                    for (Player p : Bukkit.getOnlinePlayers())
                    {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(args[0]);
                        p.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
                    }

                    Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () -> Bukkit.getServer().shutdown(), 3 * 20L);
                }

                this.timer--;
            }
        }, 20L, 20L);

        return true;
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        return false;
    }
}
