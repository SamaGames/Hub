package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.IShopsManager;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiConfirm;
import net.samagames.hub.utils.NumberUtils;
import net.samagames.tools.GlowEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCosmetic implements Comparable<AbstractCosmetic>
{
    private final Hub hub;
    private final String category;
    private final String key;
    private final ItemStack icon;
    private final int stars;
    private final CosmeticRarity rarity;
    private final CosmeticAccessibility accessibility;
    private final IShopsManager shopsManager;
    private String permissionNeededToView;

    public AbstractCosmetic(Hub hub, String category, String key, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description)
    {
        this.hub = hub;
        this.category = category;
        this.key = key;
        this.icon = icon;

        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + rarity.getColor() + displayName);

        List<String> lore = new ArrayList<>();

        if (description != null)
            for (String str : description)
                lore.add(ChatColor.GRAY + str);

        meta.setLore(lore);

        this.icon.setItemMeta(meta);
        this.stars = stars;
        this.rarity = rarity;
        this.accessibility = accessibility;
        this.shopsManager = SamaGamesAPI.get().getShopsManager();
        this.permissionNeededToView = null;
    }

    public void buy(Player player, boolean magicChest)
    {
        if (magicChest)
        {
            this.shopsManager.addOwnedLevel(player.getUniqueId(), this.category, this.key);
        }
        else
        {
            if (!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughStars(this.stars))
            {
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous n'avez pas assez d'étoiles pour acheter cela.");
                return;
            }

            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawStars(this.stars, (newAmount, difference, error) ->
                {
                    this.shopsManager.addOwnedLevel(player.getUniqueId(), this.category, this.key);
                    this.hub.getScoreboardManager().update(player);
                    this.hub.getGuiManager().openGui(player, parent);

                    player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre achat a bien été effectué, vous pouvez maintenant utiliser votre cosmétique.");
                });
            });

            this.hub.getGuiManager().openGui(player, confirm);
        }
    }

    public void permissionNeededToView(String permissionNeededToView)
    {
        this.permissionNeededToView = permissionNeededToView;
    }

    public String getCategory()
    {
        return this.category;
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

        lore.add("");

        if (!this.isOwned(player))
        {
            cloned.setType(Material.INK_SACK);
            cloned.setDurability((short) 8);

            if (this.accessibility != CosmeticAccessibility.ALL)
            {
                lore.add(ChatColor.WHITE + "Nécessite le grade " + this.accessibility.getDisplay() + ChatColor.WHITE + " !");
                lore.add("");
            }

            lore.add(ChatColor.WHITE + "Débloquez cet objet dans le " + ChatColor.RED + "coffre");
            lore.add(ChatColor.RED + "magique" + ChatColor.WHITE + " ou achetez le pour " + ChatColor.AQUA + NumberUtils.format(this.stars));
            lore.add(ChatColor.AQUA + "étoiles" + ChatColor.WHITE + " !");
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

    public String getKey()
    {
        return this.key;
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

        List<String> owned = this.shopsManager.getOwnedLevels(player.getUniqueId(), this.category);
        return owned != null && owned.contains(this.key);
    }

    @Override
    public int compareTo(AbstractCosmetic cosmetic)
    {
        if (cosmetic.getCategory().equals(this.category) && cosmetic.getKey().equals(this.key))
            return 1;
        else
            return -1;
    }
}