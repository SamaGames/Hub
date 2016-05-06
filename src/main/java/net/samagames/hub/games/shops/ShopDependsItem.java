package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
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

public class ShopDependsItem extends ShopItem
{
    protected ShopItem dependsOn;

    public ShopDependsItem(Hub hub, long storageId, String displayName, ItemStack icon, int slot, String[] description, int cost, ShopItem dependsOn)
    {
        super(hub, storageId, displayName, icon, slot, description, cost);
        this.dependsOn = dependsOn;
    }

    public void resetCurrents(Player player) {}

    @Override
    public void execute(Player player, ClickType clickType)
    {
        if(this.isActive(player))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Cet objet est déjà équipé.");
        }
        else if (this.isOwned(player) || this.isDefaultItem())
        {
            this.resetCurrents(player);
            SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId).setSelected(true);

            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
        }
        else if (this.dependsOn != null && !hasDepend(player))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Il est nécessaire de posséder " + ChatColor.AQUA + this.dependsOn.getIcon().getItemMeta().getDisplayName() + ChatColor.RED + " pour acheter cela.");
        }
        else if (!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.cost))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cela.");
        }
        else
        {
            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                if(this.isActive(player))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.cost, (newAmount, difference, error) ->
                {
                    this.resetCurrents(player);

                    // TODO: Add the item to the player
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId).setSelected(true);

                    player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

                    this.hub.getScoreboardManager().update(player);
                });

                this.hub.getGuiManager().getPlayerGui(player).update(player);
                this.hub.getGuiManager().openGui(player, parent);
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

        lore.add("");

        if(this.isActive(player))
            lore.add(ChatColor.GREEN + "Objet actif");
        else if(this.isOwned(player) || this.isDefaultItem())
            lore.add(ChatColor.GREEN + "Objet possédé");
        else if(( this.dependsOn != null && this.hasDepend(player)) || this.dependsOn == null)
            lore.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.cost + " pièces");
        else
            lore.add(ChatColor.RED + "Nécessite " + ChatColor.AQUA + this.dependsOn.getIcon().getItemMeta().getDisplayName());

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public boolean hasDepend(Player player)
    {
        return this.dependsOn.isDefaultItem() || SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.dependsOn.getStorageId()) != null;
    }
}
