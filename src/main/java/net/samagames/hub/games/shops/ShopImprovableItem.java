package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.IItemDescription;
import net.samagames.api.shops.ITransaction;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
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
    private final List<ItemLevel> levels;
    private final String defaultDescription;

    public ShopImprovableItem(Hub hub, int storageId, int slot, Integer defaultStorageId) throws Exception
    {
        super(hub, storageId, slot, new int[0]);

        this.levels = new ArrayList<>();

        if (defaultStorageId != null)
        {
            IItemDescription defaultItemDescription = SamaGamesAPI.get().getShopsManager().getItemDescription(defaultStorageId);
            this.defaultDescription = defaultItemDescription.getItemDesc();
        }
        else
        {
            this.defaultDescription = null;
        }
    }

    @Override
    public void resetCurrents(Player player)
    {
        try
        {
            if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(this.storageId) != null)
                SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(this.storageId, false);

            for (ItemLevel level : this.levels)
                if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(level.getStorageId()) != null)
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem((int) level.getStorageId(), false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addLevel(int storageId)
    {
        try
        {
            IItemDescription itemDescription = SamaGamesAPI.get().getShopsManager().getItemDescription(storageId);
            this.levels.add(new ItemLevel(storageId, itemDescription.getItemDesc(), itemDescription.getPriceCoins()));
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

                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).addItem(this.storageId, this.itemDescription.getPriceCoins(), 0, true, (aBoolean, throwable) ->
                    {
                        player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez débloqué le niveau supérieur pour cette amélioration.");

                        this.hub.getScoreboardManager().update(player);
                        this.hub.getGuiManager().openGui(player, parent);
                    });
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

        if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(this.levels.get(0).getStorageId()) == null)
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

        ItemStack icon = this.getIcon();
        ItemMeta meta = icon.getItemMeta();
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

        if (this.defaultDescription != null)
        {
            lore.add(ChatColor.GREEN + "Par défaut : " + this.defaultDescription);
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

        meta.setLore(lore);
        icon.setItemMeta(meta);

        return icon;
    }

    private int getLevel(Player player)
    {
        int level = 0;

        for (ItemLevel itemLevel : this.levels)
        {
            ITransaction transaction = SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(itemLevel.getStorageId());

            if (transaction != null && transaction.isSelected())
                return level;

            level++;
        }

        return 0;
    }

    private ItemLevel getNextItem(Player player)
    {
        int level = this.getLevel(player);
        level++;

        if (level >= this.levels.size())
            return null;

        return this.levels.get(level);
    }
}
