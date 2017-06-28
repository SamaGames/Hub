package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.IItemDescription;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiConfirm;
import net.samagames.hub.utils.NumberUtils;
import net.samagames.tools.GlowEffect;
import net.samagames.tools.PersistanceUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class AbstractCosmetic implements Comparable<AbstractCosmetic>
{
    protected final Hub hub;
    private final String categoryName;
    private final int storageId;
    private final ItemStack icon;
    private final CosmeticRarity rarity;
    private final CosmeticAccessibility accessibility;
    private final int initialPrice;
    private String permissionNeededToView;

    public AbstractCosmetic(Hub hub, String categoryName, int storageId) throws Exception
    {
        this.hub = hub;
        this.categoryName = categoryName;
        this.storageId = storageId;
        this.permissionNeededToView = null;

        IItemDescription itemDescription = SamaGamesAPI.get().getShopsManager().getItemDescription(storageId);

        this.icon = PersistanceUtils.makeStack(hub, itemDescription);
        this.rarity = CosmeticRarity.valueOf(itemDescription.getItemRarity().toUpperCase());
        this.accessibility = CosmeticAccessibility.valueOf(itemDescription.getRankAccessibility().toUpperCase());
        this.initialPrice = itemDescription.getPriceStars();

        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + this.rarity.getColor() + itemDescription.getItemName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + categoryName);

        if (itemDescription.getItemDesc() != null)
            lore.add("");

        if (meta.getLore() != null)
            lore.addAll(meta.getLore());

        meta.setLore(lore);

        this.icon.setItemMeta(meta);
    }

    public void unlock(Player player)
    {
        SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).addItem(this.storageId, 0, 0, false);
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public ItemStack getIcon(Player player)
    {
        ItemStack cloned = this.icon.clone();
        ItemMeta meta = cloned.getItemMeta();

        List<String> lore = meta.getLore();

        if (lore == null)
            lore = new ArrayList<>();
        else
            lore.add("");

        if (!this.isOwned(player))
        {
            cloned = new ItemStack(Material.INK_SACK, 1, (short) 8);
            meta = cloned.getItemMeta();
            meta.setDisplayName(this.icon.getItemMeta().getDisplayName());

            if (this.accessibility != CosmeticAccessibility.ALL)
            {
                lore.add(ChatColor.GRAY + "Vous devez posséder le grade");
                lore.add(this.accessibility.getDisplay() + ChatColor.GRAY + " pour pouvoir utiliser ce");
                lore.add(ChatColor.GRAY + "cosmétique.");
                lore.add("");
            }

            if (this.accessibility != CosmeticAccessibility.STAFF && this.accessibility != CosmeticAccessibility.ADMIN)
            {
                lore.add(ChatColor.YELLOW + "Echangez une perle à " + ChatColor.GOLD + "Graou" + ChatColor.YELLOW + " pour");
                lore.add(ChatColor.YELLOW + "tenter de débloquer ce cosmétique.");
            }
            else
            {
                lore.add(ChatColor.YELLOW + "Cliquez pour débloquer ce cosmétique.");
            }

            cloned.removeEnchantment(GlowEffect.getGlow());
        }
        else
        {
            lore.add(ChatColor.GREEN + "Cliquez pour utiliser");
        }

        meta.setLore(lore);
        cloned.setItemMeta(meta);

        if (this.hub.getCosmeticManager().isEquipped(player, this))
            GlowEffect.addGlow(cloned);

        return cloned;
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public int getStorageId()
    {
        return this.storageId;
    }

    public int getRefundPrice()
    {
        return this.initialPrice / 100;
    }

    public CosmeticRarity getRarity()
    {
        return this.rarity;
    }

    public CosmeticAccessibility getAccessibility()
    {
        return this.accessibility;
    }

    public boolean canView(Player player)
    {
        return this.permissionNeededToView == null || SamaGamesAPI.get().getPermissionsManager().hasPermission(player, this.permissionNeededToView);
    }

    public boolean isOwned(Player player)
    {
        if (this.permissionNeededToView != null && this.canView(player))
            return true;

        return SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(this.storageId) != null;
    }

    @Override
    public int compareTo(AbstractCosmetic cosmetic)
    {
        if (cosmetic.getStorageId() == this.storageId)
            return 1;
        else
            return -1;
    }
}