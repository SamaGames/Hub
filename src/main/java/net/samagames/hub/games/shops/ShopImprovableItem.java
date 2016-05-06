package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.IItemDescription;
import net.samagames.api.shops.ITransaction;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiConfirm;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopImprovableItem extends ShopIcon
{
    private final String[] description;
    private final List<ItemLevel> levels;
    private ItemLevel defaultLevel;

    public ShopImprovableItem(Hub hub, long storageId, String displayName, ItemStack icon, int slot, String[] description)
    {
        super(hub, storageId, displayName, icon, slot);

        this.description = description;
        this.levels = new ArrayList<>();

        try
        {
            IItemDescription itemDescription = SamaGamesAPI.get().getShopsManager().getItemDescription((int) this.storageId);
            this.defaultLevel = new ItemLevel(itemDescription.getPriceCoins(), itemDescription.getItemDesc(), this.storageId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void resetCurrents(Player player)
    {
        if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.defaultLevel.getStorageId()) != null)
            SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.defaultLevel.getStorageId()).setSelected(false);

        for (ItemLevel level : this.levels)
            if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) level.getStorageId()) != null)
                SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) level.getStorageId()).setSelected(false);
    }

    public void addLevel(long storageId)
    {
        try
        {
            IItemDescription itemDescription = SamaGamesAPI.get().getShopsManager().getItemDescription((int) storageId);
            this.levels.add(new ItemLevel(itemDescription.getPriceCoins(), itemDescription.getItemDesc(), storageId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void execute(Player player, ClickType clickType)
    {
        ItemLevel next = this.levels.get(0);

        if (this.getLevel(player) != 0)
            next = this.getNextItem(player);

        if (next == null)
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous avez déjà débloqué le niveau maximum de cette amélioration.");
        }
        else if (!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(next.getCost()))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cette amélioration.");
        }
        else
        {
            final ItemLevel finalLevel = next;

            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                if(this.isOwned(player, finalLevel.getStorageId()))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(finalLevel.getCost(), (newAmount, difference, error) ->
                {
                    this.resetCurrents(player);

                    // TODO: Add the item to the player
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) finalLevel.getStorageId()).setSelected(true);

                    player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez débloqué le niveau supérieur pour cette amélioration.");

                    this.hub.getScoreboardManager().update(player);
                    this.hub.getGuiManager().openGui(player, parent);
                });

                this.hub.getGuiManager().getPlayerGui(player).update(player);
            });

            this.hub.getGuiManager().openGui(player, confirm);
            return;
        }

        this.hub.getGuiManager().getPlayerGui(player).update(player);
    }

    @Override
    public ItemStack getFormattedIcon(Player player)
    {
        int currentLevel = -1;
        ItemLevel next;

        if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.levels.get(0).getStorageId()) == null)
        {
            next = this.levels.get(0);
        }
        else
        {
            currentLevel = this.getLevel(player);

            if (currentLevel == 0)
                next = this.levels.get(1);
            else
                next = this.getNextItem(player);
        }

        List<String> lore = new ArrayList<>();

        for(String str : this.description)
            lore.add(ChatColor.GRAY + str);

        lore.add("");

        if(this.defaultLevel != null)
        {
            lore.add(ChatColor.GREEN + "Par défaut : " + this.defaultLevel.getDescription());
            lore.add("");
        }

        for(int i = 0; i < this.levels.size(); i++)
        {
            ItemLevel level = this.levels.get(i);
            String prefix = "";

            if(currentLevel >= i)
                prefix += ChatColor.GREEN;
            else if(i == (currentLevel + 1))
                prefix += ChatColor.YELLOW + "" + ChatColor.BOLD;
            else
                prefix += ChatColor.RED;

            lore.add(prefix + "Niveau " + (i + 1) + " : " + level.getDescription());
        }

        lore.add("");

        if(next == null)
            lore.add(ChatColor.GREEN + "Améliorations débloquées");
        else
            lore.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + next.getCost() + " pièces");

        ItemStack icon = this.getIcon();
        ItemMeta meta = icon.getItemMeta();
        meta.setLore(lore);
        icon.setItemMeta(meta);

        return icon;
    }

    public ItemLevel getItemLevel(int level)
    {
        if (level == -1)
            return null;
        else if (level >= this.levels.size())
            return null;
        else
            return this.levels.get(level);
    }

    public int getLevel(Player player)
    {
        int level = 0;

        for (ItemLevel itemLevel : this.levels)
        {
            ITransaction transaction = SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) itemLevel.getStorageId());

            if (transaction != null && transaction.isSelected())
                return level;

            level++;
        }

        return level;
    }

    public ItemLevel getNextItem(Player player)
    {
        int level = this.getLevel(player);
        level++;

        if (level >= this.levels.size())
            return null;

        return this.levels.get(level);
    }

    public boolean isOwned(Player player, long storageId)
    {
        return SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) storageId) != null;
    }

    public boolean isActive(Player player, long storageId)
    {
        return this.isOwned(player, storageId) && SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) storageId).isSelected();
    }
}
