package net.samagames.hub.commands.admin;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.Song;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.cosmetics.jukebox.JukeboxDiskCosmetic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

/**
 * Created by Rigner for project Hub.
 */
public class CommandNBS extends AbstractCommand
{
    public CommandNBS(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if(args.length == 1)
        {
            if (new File(this.hub.getDataFolder(), "nbs" + File.separator + args[0] + ".nbs").exists())
            {
                try
                {
                    Song song = NBSDecoder.parse(args[0]);
                    JukeboxDiskCosmetic disk = new JukeboxDiskCosmetic(this.hub, 0, song, (int)(song.getLength() / song.getSpeed()));
                    this.hub.getCosmeticManager().getJukeboxManager().play(disk, player);
                }
                catch (Exception ex)
                {
                    player.sendMessage(ChatColor.RED + "Erreur lors du chargement de la musique !");
                }
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Cette musique n'éxiste pas !");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Veuillez indiquer une musique !");
        }

        return true;
    }
}
