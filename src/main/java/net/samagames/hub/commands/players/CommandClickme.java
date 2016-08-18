package net.samagames.hub.commands.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.gui.profile.GuiClickMe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Rigner for project Hub.
 */
public class CommandClickme extends AbstractCommand
{
    public CommandClickme(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args == null || args.length < 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /" + command.getLabel() + " <Joueur>");
            return true;
        }

        UUID uuid = SamaGamesAPI.get().getUUIDTranslator().getUUID(args[0], false);

        if (uuid == null)
        {
            player.sendMessage(ChatColor.RED + "Ce joueur n'existe pas !");
            return true;
        }

        if (SamaGamesAPI.get().getSettingsManager().getSettings(uuid).isClickOnMeActivation())
        {
            this.hub.getGuiManager().openGui(player, new GuiClickMe(this.hub, args[0], uuid));
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Ce joueur a désactivé le ClickMe !");
        }

        return true;
    }
}
