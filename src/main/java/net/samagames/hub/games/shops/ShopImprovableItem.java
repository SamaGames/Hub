package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.shops.IItemDescription;
import net.samagames.api.shops.ITransaction;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiConfirm;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
public class ShopImprovableItem extends ShopIcon
{
    private final List<ItemLevel> levels;
    private final String defaultDescription;

    public ShopImprovableItem(Hub hub, String categoryName, int storageId, int slot, Integer defaultStorageId) throws Exception
    {
        super(hub, categoryName, storageId, slot, new int[0]);

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
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(level.getStorageId(), false);
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

        if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(this.levels.get(0).getStorageId()) != null)
        {
            if (this.getLevel(player) == 0)
                next = this.levels.get(1);
            else
                next = this.getNextItem(player);
        }

        if (this.getLevel(player) != 0)
            next = this.getNextItem(player);

        if (next == null)
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous avez déjà débloqué le niveau maximum de cette amélioration.");
        }
        else if (!CosmeticAccessibility.valueOf(this.itemDescription.getRankAccessibility()).canAccess(player))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous n'avez pas le grade nécessaire pour acheter cet objet.");
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
                if (this.isOwned(player, finalLevel.getStorageId()))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(finalLevel.getCost(), (newAmount, difference, error) ->
                {
                    this.resetCurrents(player);

                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).addItem(finalLevel.getStorageId(), finalLevel.getCost(), 0, true, (aBoolean, throwable) ->
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

        ItemStack icon = this.getIcon().clone();
        ItemMeta meta = icon.getItemMeta();

        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.add("");

        if (this.defaultDescription != null)
        {
            lore.add(ChatColor.GREEN + "Par défaut : " + this.defaultDescription);
            lore.add("");
        }

        for (int i = 0; i < this.levels.size(); i++)
        {
            ItemLevel level = this.levels.get(i);
            String prefix = "";

            if (currentLevel >= i)
                prefix += ChatColor.GREEN;
            else if (i == (currentLevel + 1))
                prefix += ChatColor.YELLOW + "" + ChatColor.BOLD;
            else
                prefix += ChatColor.RED;

            lore.add(prefix + "Niveau " + (i + 1) + " : " + level.getDescription());
        }

        if (CosmeticAccessibility.valueOf(this.itemDescription.getRankAccessibility()) != CosmeticAccessibility.ALL)
        {
            lore.add("");
            lore.add(ChatColor.GRAY + "Vous devez posséder le grade");
            lore.add(CosmeticAccessibility.valueOf(this.itemDescription.getRankAccessibility()).getDisplay() + ChatColor.GRAY + " pour pouvoir utiliser cet");
            lore.add(ChatColor.GRAY + "objet.");
        }

        lore.add("");

        if (next == null)
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
