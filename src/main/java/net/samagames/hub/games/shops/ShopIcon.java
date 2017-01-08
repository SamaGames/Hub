package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.IItemDescription;
import net.samagames.hub.Hub;
import net.samagames.tools.PersistanceUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class ShopIcon
{
    protected final Hub hub;
    protected final int storageId;
    protected final int slot;
    protected final int[] resetIds;
    protected final IItemDescription itemDescription;
    protected final ItemStack icon;

    ShopIcon(Hub hub, String categoryName, int storageId, int slot, int[] resetIds) throws Exception
    {
        this.hub = hub;
        this.storageId = storageId;
        this.slot = slot;
        this.resetIds = resetIds;

        hub.getGameManager().log(Level.INFO, "Fetching shop icon data for the id: " + storageId);

        this.itemDescription = SamaGamesAPI.get().getShopsManager().getItemDescription(storageId);
        this.icon = PersistanceUtils.makeStack(hub, this.itemDescription);

        if (categoryName != null)
        {
            ItemMeta meta = this.icon.getItemMeta();
            List<String> lore = (meta.getLore() != null) ? meta.getLore() : new ArrayList<>();

            lore.add(0, ChatColor.DARK_GRAY + categoryName);

            meta.setLore(lore);
            this.icon.setItemMeta(meta);
        }
    }

    public abstract void execute(Player player, ClickType clickType);
    public abstract ItemStack getFormattedIcon(Player player);

    public void resetCurrents(Player player)
    {
        try
        {
            for (int resetId : this.resetIds)
                if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(resetId) != null)
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(resetId, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getStorageId()
    {
        return this.storageId;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public int getSlot()
    {
        return this.slot;
    }

    public boolean isOwned(Player player)
    {
        return this.isOwned(player, this.storageId);
    }

    public boolean isOwned(Player player, int storageId)
    {
        return SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(storageId) != null;
    }

    public boolean isActive(Player player)
    {
        try
        {
            return this.isOwned(player) && SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).isSelectedItem(this.storageId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
