package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
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

public class ShopItem extends ShopIcon
{
    protected final String[] description;
    protected final int cost;
    protected boolean defaultItem;

    public ShopItem(Hub hub, long storageId, String displayName, ItemStack icon, int slot, String[] description, int cost)
    {
        super(hub, storageId, displayName, icon, slot);

        this.description = description;
        this.cost = cost;
    }

    /**
     * Edit this method to unselect all the items related to
     * this one
     */
    public void resetCurrents(Player player) {}

    @Override
    public void execute(Player player, ClickType clickType)
    {
        if(this.isActive(player))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Cet objet est déjà équipé.");
        }
        else if(this.isOwned(player) || this.isDefaultItem())
        {
            this.resetCurrents(player);
            SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId).setSelected(true);

            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
        }
        else if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.cost))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cet objet.");
        }
        else
        {
            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                if(this.isOwned(player))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.cost, (newAmount, difference, error) ->
                {
                    this.resetCurrents(player);

                    // TODO: Add the item to the player
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId).setSelected(true);

                    player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

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
        ItemStack stack = getIcon().clone();
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();

        for(String str : this.description)
            lore.add(ChatColor.GRAY + str);

        lore.add(ChatColor.GRAY + "");

        if(this.isActive(player))
            lore.add(ChatColor.GREEN + "Objet actif");
        else if(isDefaultItem() || isOwned(player))
            lore.add(ChatColor.GREEN + "Objet possédé");
        else
            lore.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.cost + " pièces");

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public void setDefaultItem(boolean defaultItem)
    {
        this.defaultItem = defaultItem;
    }

    public boolean isDefaultItem()
    {
        return this.defaultItem;
    }

    public boolean isOwned(Player player)
    {
        return SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId) != null;
    }

    public boolean isActive(Player player)
    {
        return this.isOwned(player) && SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId).isSelected();
    }
}
