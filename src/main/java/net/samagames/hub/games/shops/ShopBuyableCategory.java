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

public class ShopBuyableCategory extends ShopCategory
{
    private final int cost;

    public ShopBuyableCategory(Hub hub, AbstractGame game, long storageId, String displayName, ItemStack icon, int slot, String[] description, int cost)
    {
        super(hub, game, storageId, displayName, icon, slot, description);

        this.cost = cost;
    }

    @Override
    public void execute(Player player, ClickType clickType)
    {
        if(this.isOwned(player))
        {
            super.execute(player, clickType);
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
                    // TODO: Add the item to the player
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId).setSelected(true);

                    player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

                    this.hub.getScoreboardManager().update(player);
                    this.hub.getGuiManager().openGui(player, parent);
                });
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

        if(isOwned(player))
            lore.add(ChatColor.GREEN + "Objet possédé");
        else
            lore.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.cost + " pièces");

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public boolean isOwned(Player player)
    {
        if(this.cost == 0)
            return true;

        return SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionSelectedByID((int) this.storageId) != null;
    }
}
