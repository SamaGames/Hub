package net.samagames.hub.commands.admin;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandSimulateBan extends AbstractCommand
{
    public CommandSimulateBan(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (player.isOp() && args.length == 2)
        {
            SamaGamesAPI.get().getPubSub().send("cheat", args[0] + "#####" + args[1]);
        }

        return true;
    }
}
