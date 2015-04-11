package net.samagames.hub.commands.admin;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.UUID;

public class CommandNPC extends AbstractCommand
{
    public CommandNPC()
    {
        super("lobby.npc");
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args == null || args.length < 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /npc <debug|reload|list|add|remove>");
            return true;
        }

        String commandName = args[0];

        switch(commandName)
        {
            case "debug":
                this.toggleDebug(player, args);
                break;

            case "reload":
                Hub.getInstance().getNPCManager().reloadNPCS();
                break;

            case "list":
                Hub.getInstance().getNPCManager().listNPCS(player);
                break;

            case "add":
                this.addNPC(player, args);
                break;

            case "remove":
                this.removeNPC(player, args);
                break;
        }

        return true;
    }

    private void toggleDebug(Player sender, String[] args)
    {
        if(args.length < 1 || (!args[1].equals("false") && !args[1].equals("true")))
        {
            sender.sendMessage(ChatColor.RED + "Usage: /npc debug <true/false>");
            return;
        }

        boolean flag = Boolean.valueOf(args[1]);
        Hub.getInstance().getNPCManager().toggleDebug(flag);
        sender.sendMessage(ChatColor.GREEN + "Le mode débug a été basculé sur '" + flag + "' !");
    }

    private void addNPC(Player player, String[] args)
    {
        if(args.length < 4)
        {
            player.sendMessage(ChatColor.RED + "Usage: /npc add <name> <profession> <action>");
            return;
        }

        Villager.Profession profession = Villager.Profession.valueOf(args[2]);

        if(profession == null)
        {
            player.sendMessage(ChatColor.RED + "Profession incorrete !");
            return;
        }

        String actionClassName = args[3];

        try
        {
            Class.forName("net.samagames.hub.npcs.actions." + actionClassName);
        }
        catch (ClassNotFoundException e)
        {
            player.sendMessage(ChatColor.RED + "Action incorrecte !");
            return;
        }

        Hub.getInstance().getNPCManager().addNPC(args[1].replaceAll("#", " "), profession, player.getLocation(), actionClassName);
        player.sendMessage(ChatColor.GREEN + "Le NPC a été ajouté avec succès ! Rechargez les NPC avec " + ChatColor.GOLD + "/npc reload" + ChatColor.GREEN + " !");
    }

    private void removeNPC(Player player, String[] args)
    {
        if(args.length < 2)
        {
            player.sendMessage(ChatColor.RED + "Usage: /npc remove <uuid>");
            return;
        }

        UUID uuid = UUID.fromString(args[1]);

        if(!Hub.getInstance().getNPCManager().hasNPC(uuid))
        {
            player.sendMessage(ChatColor.RED + "Ce NPC n'existe pas !");
            return;
        }

        Hub.getInstance().getNPCManager().removeNPC(uuid);
        player.sendMessage(ChatColor.GREEN + "Le NPC a été supprimé avec succès ! Rechargez les NPC avec " + ChatColor.GOLD + "/npc reload" + ChatColor.GREEN + " !");
    }
}
