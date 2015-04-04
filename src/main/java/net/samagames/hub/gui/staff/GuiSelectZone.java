package net.samagames.hub.gui.staff;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.main.GuiMain;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiSelectZone extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 9, "Sélection de zone");

        this.setSlotData(ChatColor.GOLD + "Zone " + ChatColor.GREEN + "VIP", Material.DIAMOND, 3, null, "vip");
        this.setSlotData(GuiUtils.getBackItem(), 4, "back");
        this.setSlotData(ChatColor.GOLD + "Zone " + ChatColor.BLUE + "Staff", Material.COOKIE, 5, null, "staff");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("vip"))
        {
            player.teleport(Hub.getInstance().getGameManager().getGameByIdentifier("beta_vip").getLobbySpawn());
            Hub.getInstance().getGuiManager().closeGui(player);
        }
        else if(action.equals("staff"))
        {
            player.teleport(Hub.getInstance().getGameManager().getGameByIdentifier("beta_staff").getLobbySpawn());
            Hub.getInstance().getGuiManager().closeGui(player);
            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).creditCoins(1, "Cookie c:");
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiMain());
        }
    }
}
