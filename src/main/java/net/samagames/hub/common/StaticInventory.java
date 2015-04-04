package net.samagames.hub.common;

import net.md_5.bungee.api.ChatColor;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.main.GuiMain;
import net.samagames.hub.gui.profile.GuiProfile;
import net.samagames.hub.gui.shop.GuiShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;

public class StaticInventory
{
    private HashMap<Integer, ItemStack> items;

    public StaticInventory()
    {
        this.items = new HashMap<>();

        this.items.put(0, this.buildItemStack(Material.COMPASS, 1, 0, this.createTitle("Menu principal"), null));
        this.items.put(1, this.buildItemStack(Material.GOLD_INGOT, 1, 0, this.createTitle("Boutique"), null));
        this.items.put(4, this.buildItemStack(Material.SKULL_ITEM, 1, 3, this.createTitle("Profil"), null));
        this.items.put(7, this.buildItemStack(Material.NETHER_STAR, 1, 0, this.createTitle("Cosmétiques"), null));
        this.items.put(8, this.buildItemStack(Material.BOW, 1, 0, this.createTitle("Stalker 2000"), null));
    }

    public void doInteraction(Player player, ItemStack stack)
    {
        if(stack.getType() == Material.COMPASS)
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiMain());
        }
        else if(stack.getType() == Material.GOLD_INGOT)
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiShop());
        }
        else if(stack.getType() == Material.SKULL_ITEM)
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiProfile());
        }
        else if(stack.getType() == Material.NETHER_STAR)
        {

        }
        else if(stack.getType() == Material.BOW)
        {

        }
    }

    public void setInventoryToPlayer(Player player)
    {
        for(int slot : this.items.keySet())
        {
            if(this.items.get(slot).getType() == Material.SKULL_ITEM)
            {
                SkullMeta meta = (SkullMeta) this.items.get(slot).getItemMeta();
                meta.setOwner(player.getName());
                this.items.get(slot).setItemMeta(meta);
            }

            player.getInventory().setItem(slot, this.items.get(slot));
        }
    }

    private String createTitle(String text)
    {
        return ChatColor.GOLD + "" + ChatColor.BOLD + text + ChatColor.RESET + "" + ChatColor.GRAY + " (Clique droit!)";
    }

    private ItemStack buildItemStack(Material material, int quantity, int data, String name, String[] lores)
    {
        ItemStack stack = new ItemStack(material, quantity, (short) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);

        if(lores != null)
            meta.setLore(Arrays.asList(lores));

        stack.setItemMeta(meta);

        return stack;
    }
}
