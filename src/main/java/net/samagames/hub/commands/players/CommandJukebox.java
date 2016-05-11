package net.samagames.hub.commands.players;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.hub.gui.cosmetics.GuiCosmeticsCategory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by Rigner for project Hub.
 */
public class CommandJukebox extends AbstractCommand
{
    public CommandJukebox(Hub hub)
    {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args)
    {
        this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Jukebox", this.hub.getCosmeticManager().getJukeboxManager(), false));
        return true;
    }
}
