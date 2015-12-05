package net.samagames.hub.gui.main;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiSelectZone extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 9, "Sélectionnez votre zone");

        this.setSlotData("Zone VIP", Material.DIAMOND, 0, null, "vip");
        this.setSlotData("Zone Staff", Material.COOKIE, 1, null, "staff");

        this.setSlotData(GuiUtils.getBackItem(), 8, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.equals("vip"))
        {
            player.teleport(Hub.getInstance().getGameManager().getGameByIdentifier("beta_vip").getLobbySpawn());
        }
        else if (action.equals("staff"))
        {
            player.teleport(Hub.getInstance().getGameManager().getGameByIdentifier("beta_staff").getLobbySpawn());
        }
        else
        {
            Hub.getInstance().getGuiManager().closeGui(player);
        }
    }
}
