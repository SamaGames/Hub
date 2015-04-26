package net.samagames.hub.gui.shop;

import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;

public class GuiRankShop extends AbstractGui
{
    @Override
    public void display(Player player)
    {

    }

    private void drawRank(ChatColor rankColor, String rankName, Color color, int baseSlot, String[] options)
    {
        ItemStack rankHelmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta rankHelmetMeta = (LeatherArmorMeta) rankHelmet.getItemMeta();
        rankHelmetMeta.setDisplayName(rankColor + rankName);
        rankHelmetMeta.setColor(color);
        rankHelmet.setItemMeta(rankHelmetMeta);

        ItemStack armorStand = new ItemStack(Material.ARMOR_STAND, 1);
        ItemMeta armorStandMeta = armorStand.getItemMeta();
        armorStandMeta.setDisplayName(rankColor + rankName);

        ArrayList<String> lores = new ArrayList<>();
        lores.add(rankColor + "Acheter un rang vous permet");
        lores.add(rankColor + "d'obtenir des fonctionnalités");
        lores.add(rankColor + "inédites :");
        lores.add("");

        for(String str : options)
            lores.add(rankColor + str);

        armorStandMeta.setLore(lores);
        armorStand.setItemMeta(armorStandMeta);

        this.setSlotData(rankHelmet, baseSlot, "none");
        this.setSlotData(armorStand, (baseSlot + 9), "none");


    }
}
