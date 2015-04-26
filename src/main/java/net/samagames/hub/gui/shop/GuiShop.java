package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

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

        this.inventory = Bukkit.createInventory(null, 9 + (9 * lines) + (9 * 3), "Boutique");

        slot = 0;
        lines = 0;

        for(String gameIdentifier : Hub.getInstance().getGameManager().getGames().keySet())
        {
            AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(gameIdentifier);

            if (game.hasShop())
            {
                this.setSlotData(ChatColor.GOLD + game.getName(), game.getIcon(), slots[slot] + (9 * lines), null, "game_" + game.getCodeName());
                slot++;

                if(slot == slots.length)
                {
                    slot = 0;
                    lines++;
                }
            }
        }

        this.drawRankBuy();
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

    private void drawRankBuy()
    {
        ItemStack rankStack = new ItemStack(Material.GOLD_HELMET, 1);
        ItemMeta meta = rankStack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Acheter un rang");

        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.YELLOW + "Acheter un grade sur le serveur");
        lores.add(ChatColor.YELLOW + "permet de vous offrir certains");
        lores.add(ChatColor.YELLOW + "droits et fonctionnalités inédites !");

        meta.setLore(lores);
        rankStack.setItemMeta(meta);

        this.setSlotData(rankStack, this.inventory.getSize() - 5 - 9, "rank");

        for(int i = 27; i < 36; i++)
        {
            if(this.inventory.getItem(i) == null)
                this.setSlotData(ChatColor.GRAY + "", new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.YELLOW.getData()), i, null, "none");
        }
    }
}
