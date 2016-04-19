package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
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

public class ShopDependsItem extends ShopItem
{
    protected ShopItem dependsOn;

    public ShopDependsItem(Hub hub, AbstractGame game, String type, String databaseName, String displayName, ItemStack icon, int slot, String[] description, int cost, ShopItem dependsOn)
    {
        super(hub, game, type, databaseName, displayName, icon, slot, description, cost);
        this.dependsOn = dependsOn;
    }

    @Override
    public void execute(Player player, ClickType clickType)
    {
        if(this.isActive(player))
        {
            player.sendMessage(ChatColor.RED + "Cet objet est déjà équipé.");
        }
        else if (isOwned(player) || this.isDefaultItem())
        {
            SamaGamesAPI.get().getShopsManager().setCurrentLevel(player.getUniqueId(), this.type, this.getActionName());
            player.sendMessage(ChatColor.GREEN + "Vous avez équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
        }
        else if (this.dependsOn != null && !hasDepend(player))
        {
            player.sendMessage(ChatColor.RED + "Il est nécessaire de posséder " + ChatColor.AQUA + this.dependsOn.getIcon().getItemMeta().getDisplayName() + ChatColor.RED + " pour acheter cela.");
        }
        else if (!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.cost))
        {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cela.");
        }
        else
        {
            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                if(SamaGamesAPI.get().getShopsManager().getItemLevelForPlayer(player, this.type).equals(this.getActionName()))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.cost, (newAmount, difference, error) ->
                {
                    SamaGamesAPI.get().getShopsManager().addOwnedLevel(player.getUniqueId(), this.type, this.getActionName());
                    SamaGamesAPI.get().getShopsManager().setCurrentLevel(player.getUniqueId(), this.type, this.getActionName());

                    player.sendMessage(ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

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
        if(this.dependsOn.isDefaultItem())
            return true;

        List<String> own = SamaGamesAPI.get().getShopsManager().getOwnedLevels(player, this.type);
        return (own != null && this.dependsOn != null && own.contains(this.dependsOn.getActionName()));
    }
}
