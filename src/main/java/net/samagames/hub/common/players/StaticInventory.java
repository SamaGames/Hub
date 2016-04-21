package net.samagames.hub.common.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.permissions.IPermissionsEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.cosmetics.GuiCosmetics;
import net.samagames.hub.gui.main.GuiMain;
import net.samagames.hub.gui.profile.GuiProfile;
import net.samagames.hub.gui.shop.GuiShop;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;

public class StaticInventory
{
    private final Hub hub;
    private final HashMap<Integer, ItemStack> items;

    StaticInventory(Hub hub)
    {
        this.hub = hub;

        this.items = new HashMap<>();
        this.items.put(0, buildItemStack(Material.COMPASS, 1, 0, createTitle("Menu principal"), null));
        this.items.put(1, buildItemStack(Material.SKULL_ITEM, 1, 3, createTitle("Profil"), null));
        this.items.put(7, buildItemStack(Material.ENDER_CHEST, 1, 0, createTitle("Caverne aux trésors"), null));
        this.items.put(8, buildItemStack(Material.GOLD_INGOT, 1, 0, createTitle("Boutique"), null));
    }

    public void doInteraction(Player player, ItemStack stack)
    {
        if(stack.getType() == Material.COMPASS)
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
        else if(stack.getType() == Material.SKULL_ITEM)
            this.hub.getGuiManager().openGui(player, new GuiProfile(this.hub));
        else if(stack.getType() == Material.ENDER_CHEST)
            this.hub.getGuiManager().openGui(player, new GuiCosmetics(this.hub));
        else if(stack.getType() == Material.GOLD_INGOT)
            this.hub.getGuiManager().openGui(player, new GuiShop(this.hub));
        else if (stack.getType() == Material.BARRIER && this.hub.getParkourManager().getPlayerParkour(player.getUniqueId()) != null)
            this.hub.getParkourManager().getPlayerParkour(player.getUniqueId()).quitPlayer(player);
        else if (player.getInventory().getHeldItemSlot() == 6)
            this.hub.getCosmeticManager().getGadgetManager().useSelectedCosmetic(player, stack);
        else if (stack.getType() == Material.ELYTRA)
        {
            if (stack.getEnchantments().isEmpty())
            {
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                ItemMeta meta = elytra.getItemMeta();
                meta.spigot().setUnbreakable(true);
                elytra.setItemMeta(meta);
                player.getInventory().setChestplate(elytra);
                stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
            else
            {
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
                stack.removeEnchantment(Enchantment.DURABILITY);
            }
            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 1F, 1F);
        }
        else if (stack.getType() == Material.FEATHER && player.isGliding() && player.getVelocity().lengthSquared() != 0)
        {
            player.setVelocity(player.getVelocity().add(player.getLocation().getDirection().normalize().multiply(1.5D)));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 2F, 2F);
        }
    }

    public void setInventoryToPlayer(Player player)
    {
        for (int i = 0; i < 9; i++)
            player.getInventory().setItem(i, null);

        for (int slot : this.items.keySet())
        {
            if(this.items.get(slot).getType() == Material.SKULL_ITEM)
            {
                SkullMeta meta = (SkullMeta) this.items.get(slot).getItemMeta();
                meta.setOwner(player.getName());

                this.items.get(slot).setItemMeta(meta);
            }

            player.getInventory().setItem(slot, this.items.get(slot));
        }

        IPermissionsEntity permissionsEntity = SamaGamesAPI.get().getPermissionsManager().getPlayer(player.getUniqueId());
        if (permissionsEntity.getGroupId() > 2)
        {
            ItemStack itemStack = buildItemStack(Material.ELYTRA, 1, 0, createTitle("Aîles"), null);
            //if (SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isElytraEnabled())
            {
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                ItemMeta meta = elytra.getItemMeta();
                meta.spigot().setUnbreakable(true);
                elytra.setItemMeta(meta);
                itemStack.addEnchantment(Enchantment.DURABILITY, 1);
                player.getInventory().setChestplate(elytra);
            }
            player.getInventory().setItem(4, itemStack);
        }

        player.getInventory().setHeldItemSlot(0);
    }

    private static String createTitle(String text)
    {
        return ChatColor.GOLD + "" + ChatColor.BOLD + text + ChatColor.RESET + "" + ChatColor.GRAY + " (Clic-droit)";
    }

    private static ItemStack buildItemStack(Material material, int quantity, int data, String name, String[] lores)
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
