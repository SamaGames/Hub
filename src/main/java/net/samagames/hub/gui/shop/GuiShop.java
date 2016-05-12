package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiShop extends AbstractGui
{
    public GuiShop(Hub hub)
    {
        super(hub);
    }

    @Override
    public void display(Player player)
    {
        int[] slots = new int[] { 10, 11, 12, 13, 14, 15, 16, 17 };
        int slot = 0;
        int lines = 1;

        for(String gameIdentifier : this.hub.getGameManager().getGames().keySet())
        {
            AbstractGame game = this.hub.getGameManager().getGameByIdentifier(gameIdentifier);

            if (game.hasShop())
            {
                slot++;

                if(slot == slots.length)
                {
                    slot = 0;
                    lines++;
                }
            }
        }

        this.inventory = this.hub.getServer().createInventory(null, 9 + (9 * lines) + (9 * 2), "Boutique");

        slot = 0;
        lines = 0;

        for(String gameIdentifier : this.hub.getGameManager().getGames().keySet())
        {
            AbstractGame game = this.hub.getGameManager().getGameByIdentifier(gameIdentifier);

            if (game.hasShop())
            {
                ItemStack icon = game.getIcon();
                ItemMeta itemMeta = icon.getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                icon.setItemMeta(itemMeta);
                this.setSlotData(ChatColor.YELLOW + game.getName(), icon, slots[slot] + (9 * lines), null, "game_" + game.getCodeName());
                slot++;

                if(slot == slots.length)
                {
                    slot = 0;
                    lines++;
                }
            }
        }

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("back"))
        {
            this.hub.getGuiManager().closeGui(player);
        }
        else if(action.startsWith("game_"))
        {
            AbstractGame game = this.hub.getGameManager().getGameByIdentifier(action.split("_")[1]);
            this.hub.getGuiManager().openGui(player, new GuiShopCategory(this.hub, game, game.getShopConfiguration(), this));
        }
    }
}
