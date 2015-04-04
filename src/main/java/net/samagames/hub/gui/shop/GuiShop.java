package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.games.IGame;
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
        int slot = 0;
        this.inventory = Bukkit.createInventory(null, 9, "Boutique");

        for(String gameIdentifier : Hub.getInstance().getGameManager().getGames().keySet())
        {
            IGame game = Hub.getInstance().getGameManager().getGameByIdentifier(gameIdentifier);

            if (game.hasShop())
            {
                this.setSlotData(ChatColor.GOLD + game.getName(), game.getIcon(), slot, null, "game_" + game.getCodeName());
                slot++;
            }
        }

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 1, "back");

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
            IGame game = Hub.getInstance().getGameManager().getGameByIdentifier(action.split("_")[1]);
            Hub.getInstance().getGuiManager().openGui(player, new GuiShopCategory(game, game.getShopConfiguration(), this));
        }
    }
}
