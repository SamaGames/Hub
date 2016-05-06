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
    protected final String[] description;
    protected final List<ShopIcon> contents;

    public ShopCategory(Hub hub, AbstractGame game, long storageId, String displayName, ItemStack icon, int slot, String[] description)
    {
        super(hub, storageId, displayName, icon, slot);

        this.game = game;
        this.description = description;
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
        ItemStack icon = this.getIcon().clone();
        ItemMeta meta =  icon.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        for(String str : this.description)
            lore.add(ChatColor.GRAY + str);

        meta.setLore(lore);
        icon.setItemMeta(meta);

        return icon;
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
