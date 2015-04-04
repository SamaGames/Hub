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
        int lines = 1;
        int slot = 1;

        for(AbstractGame game : Hub.getInstance().getGameManager().getGames().values())
        {
            if(game.hasShop())
            {
                slot++;

                if (slot > 5)
                {
                    slot = 0;
                    lines++;
                }
            }
        }

        int slots = 9 + (lines * 9) + 9;
        this.inventory = Bukkit.createInventory(null, slots, "Boutique");

        for(String gameIdentifier : Hub.getInstance().getGameManager().getGames().keySet())
        {
            AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(gameIdentifier);

            if(game.hasShop())
                this.setSlotData(ChatColor.GOLD + game.getName(), game.getIcon(), game.getShopConfiguration().getGuiShopSlot(), null, "game_" + game.getCodeName());
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
            Hub.getInstance().getGuiManager().openGui(player, new GuiGameShop(Hub.getInstance().getGameManager().getGameByIdentifier(action.split("_")[1]), 1));
        }
    }
}
