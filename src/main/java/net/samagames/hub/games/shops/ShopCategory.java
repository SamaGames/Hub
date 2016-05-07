package net.samagames.hub.games.shops;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiShopCategory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopCategory extends ShopIcon
{
    protected final AbstractGame game;
    protected final List<ShopIcon> contents;

    public ShopCategory(Hub hub, AbstractGame game, int storageId, int slot) throws Exception
    {
        super(hub, storageId, slot, new int[0]);

        this.game = game;
        this.contents = new ArrayList<>();
    }

    public void addContent(ShopIcon icon)
    {
        this.contents.add(icon);
    }

    @Override
    public void execute(Player player, ClickType clickType)
    {
        this.hub.getGuiManager().openGui(player, new GuiShopCategory(this.hub, this.game, this, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player)));
    }

    @Override
    public ItemStack getFormattedIcon(Player player)
    {
        return this.getIcon().clone();
    }

    public ShopIcon getIconByAction(String action)
    {
        for(ShopIcon icon : this.contents)
            if(icon.getStorageId() == Long.parseLong(action))
                return icon;

        return null;
    }

    public List<ShopIcon> getContents()
    {
        return this.contents;
    }
}
