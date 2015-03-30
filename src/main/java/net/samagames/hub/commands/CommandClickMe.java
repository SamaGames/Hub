package net.samagames.hub.commands;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.profile.GuiClickMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandClickMe extends AbstractCommand
{
    public CommandClickMe()
    {
        super(null);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args == null || args.length < 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /click <Joueur>");
            return true;
        }

        if(SamaGamesAPI.get().getUUIDTranslator().getUUID(args[0], false) == null)
        {
            player.sendMessage(ChatColor.RED + "Ce joueur n'existe pas !");
            return true;
        }

        UUID uuid = SamaGamesAPI.get().getUUIDTranslator().getUUID(args[0], false);

        if(SamaGamesAPI.get().getSettingsManager().isEnabled(uuid, "clickme", true))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiClickMe(args[0], uuid));
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Ce joueur a désactivé le ClickMe !");
        }

        return true;
    }
}
