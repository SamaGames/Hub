package net.samagames.hub.gui.profile;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiProfile extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 9, "Profil");

        this.setSlotData(ChatColor.GOLD + "Statistiques", Material.ENCHANTED_BOOK, 0, new String[] { ChatColor.GRAY + "Retrouvez vos scores et classements !" }, "stats");
        this.setSlotData(ChatColor.GOLD + "Objectifs", Material.DIAMOND, 1, new String[] { ChatColor.GRAY + "Allez-vous réussir à tous les", ChatColor.GRAY + "compléter ?" }, "achievements");
        this.setSlotData(ChatColor.GOLD + "Paramètres", Material.DIODE, 7, new String[] { ChatColor.GRAY + "Vos préférences sur le serveur" }, "settings");

        this.setSlotData(GuiUtils.getBackItem(), 8, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("stats"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiStats(player));
        }
        else if(action.equals("achievements"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiAchievements());
        }
        else if(action.equals("settings"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiSettings());
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().closeGui(player);
        }
    }
}
