package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopIcon;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
        this.inventory = this.hub.getServer().createInventory(null, 54, "Boutique de " + this.game.getName());

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
                this.setSlotData(item.getFormattedIcon(player), item.getSlot(), "item_" + item.getActionName());
            }
            else
            {
                this.setSlotData(item.getFormattedIcon(player), slot, "item_" + item.getActionName());
                slot++;
            }
        }

        this.setSlotData(this.getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("back"))
        {
            if(this.before != null)
                this.hub.getGuiManager().openGui(player, this.before);
            else
                this.hub.getGuiManager().closeGui(player);
        }
        else
        {
            String iconAction = action.split("_")[1];
            ShopIcon shopIcon = this.category.getIconByAction(iconAction);
            shopIcon.execute(player, clickType);
        }
    }
}
