package net.samagames.hub.gui.cosmetics;

import net.samagames.hub.gui.AbstractGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuiJukebox extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 54, "Jukebox");

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {

    }
}
