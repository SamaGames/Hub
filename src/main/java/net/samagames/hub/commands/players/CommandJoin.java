package net.samagames.hub.commands.players;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.signs.GameSign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandJoin extends AbstractCommand
{
    public CommandJoin(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        if (args == null || args.length < 2)
            return true;

        try
        {
            AbstractGame gameByIdentifier = this.hub.getGameManager().getGameByIdentifier(args[0]);
            List<GameSign> gameSignByTemplate = gameByIdentifier.getGameSignsByTemplate(args[1]);

            gameSignByTemplate.get(0).click(player);
        }
        catch (Exception ignored) {}

        return true;
    }
}
