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
    public ShopBuyableCategory(Hub hub, AbstractGame game, int storageId, int slot) throws Exception
    {
        super(hub, game, storageId, slot);
    }

    @Override
    public void execute(Player player, ClickType clickType)
    {
        if(this.isOwned(player))
        {
            super.execute(player, clickType);
        }
        else if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.itemDescription.getPriceCoins()))
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cet objet.");
        }
        else
        {
            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                if(this.isOwned(player))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.itemDescription.getPriceCoins(), (newAmount, difference, error) ->
                {
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).addItem(this.storageId, this.itemDescription.getPriceCoins(), 0, true, (aBoolean, throwable) ->
                    {
                        SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).refresh();

                        player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

                        this.hub.getScoreboardManager().update(player);
                        this.hub.getGuiManager().openGui(player, parent);
                    });
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
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

        if(isOwned(player))
            lore.add(ChatColor.GREEN + "Objet possédé");
        else
            lore.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.itemDescription.getPriceCoins() + " pièces");

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public boolean isOwned(Player player)
    {
        if(this.itemDescription.getPriceCoins() == 0)
            return true;

        return super.isOwned(player);
    }
}
