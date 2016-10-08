package net.samagames.hub.commands.admin;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandSimulateBan extends AbstractCommand
{
    public CommandSimulateBan(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args.length >= 2)
            this.hub.getSamaritanListener().doStuff(args[0], StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "), true);
        
        return true;
    }
}
