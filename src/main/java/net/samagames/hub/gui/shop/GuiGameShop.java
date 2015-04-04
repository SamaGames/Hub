package net.samagames.hub.gui.shop;

import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuiGameShop extends AbstractGui
{
    private final AbstractGame game;
    private final int page;

    public GuiGameShop(AbstractGame game, int page)
    {
        this.game = game;
        this.page = page;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 45, "Boutique de " + this.game.getName());



        player.openInventory(this.inventory);
    }
}
