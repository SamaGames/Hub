package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiShop extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        int[] slots = new int[] { 10, 11, 12, 13, 14, 15, 16, 17 };
        int slot = 0;
        int lines = 1;

        for(String gameIdentifier : Hub.getInstance().getGameManager().getGames().keySet())
        {
            AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(gameIdentifier);

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

        this.inventory = Bukkit.createInventory(null, 9 + (9 * lines) + (9 * 2), "Boutique");

        slot = 0;
        lines = 0;

        for(String gameIdentifier : Hub.getInstance().getGameManager().getGames().keySet())
        {
            AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(gameIdentifier);

            if (game.hasShop())
            {
                this.setSlotData(game.getName(), game.getIcon(), slots[slot] + (9 * lines), null, "game_" + game.getCodeName());
                slot++;

                if(slot == slots.length)
                {
                    slot = 0;
                    lines++;
                }
            }
        }

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory)
;   }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().closeGui(player);
        }
        else if(action.startsWith("game_"))
        {
            AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(action.split("_")[1]);
            Hub.getInstance().getGuiManager().openGui(player, new GuiShopCategory(game, game.getShopConfiguration(), this));
        }
    }
}
