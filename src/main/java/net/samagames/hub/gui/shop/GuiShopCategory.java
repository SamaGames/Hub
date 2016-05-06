package net.samagames.hub.gui.shop;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopIcon;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiShopCategory extends AbstractGui
{
    private final AbstractGame game;
    private final ShopCategory category;
    private final AbstractGui before;

    public GuiShopCategory(Hub hub, AbstractGame game, ShopCategory category, AbstractGui before)
    {
        super(hub);

        this.game = game;
        this.category = category;
        this.before = before;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Boutique du jeu : " + this.game.getName());

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        int slot = 0;

        for(ShopIcon item : this.category.getContents())
        {
            if(item.getSlot() != -1)
            {
                this.setSlotData(item.getFormattedIcon(player), item.getSlot(), "item_" + item.getStorageId());
            }
            else
            {
                this.setSlotData(item.getFormattedIcon(player), slot, "item_" + item.getStorageId());
                slot++;
            }
        }

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 6, "back");
        this.setSlotData(getCoinsIcon(player), this.inventory.getSize() - 4, "none");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.equals("back"))
        {
            if(this.before != null)
                this.hub.getGuiManager().openGui(player, this.before);
            else
                this.hub.getGuiManager().closeGui(player);
        }
        else if (!action.equals("none"))
        {
            String iconAction = action.split("_")[1];
            ShopIcon shopIcon = this.category.getIconByAction(iconAction);
            shopIcon.execute(player, clickType);
        }
    }
}
