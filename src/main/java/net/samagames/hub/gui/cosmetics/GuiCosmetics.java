package net.samagames.hub.gui.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiCosmetics extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 36, "Cosmétiques");

        this.setSlotData(ChatColor.DARK_AQUA + "●" + ChatColor.AQUA + " Humeurs " + ChatColor.DARK_AQUA + "●", Material.BLAZE_POWDER, 10, new String[] {
            ChatColor.GRAY + "Montrez-nous comment vous vous sentez :)"
        }, "particles");

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("particles"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiParticles());
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().closeGui(player);
        }
    }
}
