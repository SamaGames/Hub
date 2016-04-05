package net.samagames.hub.gui;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractGui extends net.samagames.api.gui.AbstractGui
{
    protected final Hub hub;

    public AbstractGui(Hub hub)
    {
        this.hub = hub;
    }

    protected static ItemStack getBackIcon()
    {
        ItemStack stack = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "« Retour");
        stack.setItemMeta(meta);

        return stack;
    }

    protected static ItemStack getCoinsIcon(Player player)
    {
        long coins = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getCoins();

        ItemStack stack = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Vous avez " + NumberUtils.format(coins) + " pièces");
        stack.setItemMeta(meta);

        return stack;
    }

    protected static ItemStack getStarsIcon(Player player)
    {
        long stars = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getStars();

        ItemStack stack = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Vous avez " + NumberUtils.format(stars) + " étoiles");
        stack.setItemMeta(meta);

        return stack;
    }

    protected int getSlot(String action)
    {
        for (int slot : this.actions.keySet())
            if (this.actions.get(slot).equals(action))
                return slot;

        return 0;
    }
}
