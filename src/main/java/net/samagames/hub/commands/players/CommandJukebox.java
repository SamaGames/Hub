package net.samagames.hub.commands.players;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.gui.cosmetics.GuiJukebox;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandJukebox extends AbstractCommand
{
    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        Hub.getInstance().getGuiManager().openGui(player, new GuiJukebox());
        return true;
    }
}
