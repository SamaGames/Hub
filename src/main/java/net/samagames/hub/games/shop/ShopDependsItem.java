package net.samagames.hub.games.shop;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
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

    public ShopDependsItem(AbstractGame game, String type, String databaseName, String displayName, ItemStack icon, String[] description, int cost, ShopItem dependsOn)
    {
        super(game, type, databaseName, displayName, icon, description, cost);
        this.dependsOn = dependsOn;
    }

    @Override
    public void execute(Player player, ClickType clickType)
    {
        if(this.isActive(player))
        {
            player.sendMessage(ChatColor.RED + "Cet objet est déjà équipé.");
        }
        else if (isOwned(player))
        {
            SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).setCurrentLevel(player.getUniqueId(), this.type, this.getActionName());
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
        else if (clickType == ClickType.LEFT)
        {
            player.sendMessage(ChatColor.GOLD + "Faites un clic droit pour valider votre achat.");
        }
        else
        {
            GuiConfirm confirm = new GuiConfirm(Hub.getInstance().getGuiManager().getPlayerGui(player), () ->
            {
                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.cost);
                SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).addOwnedLevel(player.getUniqueId(), this.type, this.getActionName());
                SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).setCurrentLevel(player.getUniqueId(), this.type, this.getActionName());
                
                player.sendMessage(ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

                Hub.getInstance().getGuiManager().getPlayerGui(player).update(player);
            });

            Hub.getInstance().getGuiManager().openGui(player, confirm);
            return;
        }

        Hub.getInstance().getGuiManager().getPlayerGui(player).update(player);
    }

    @Override
    public ItemStack getFormattedIcon(Player player)
    {
        ItemStack stack = getIcon().clone();
        ItemMeta meta = stack.getItemMeta();

        ArrayList<String> lores = new ArrayList<>();

        for(String str : this.description)
            lores.add(ChatColor.GRAY + str);

        lores.add("");

        if(this.isActive(player) || this.isDefaultItem())
            lores.add(ChatColor.GREEN + "Objet actif");
        else if(this.isOwned(player))
            lores.add(ChatColor.GREEN + "Objet possédé");
        else if(this.hasDepend(player) || this.dependsOn == null)
            lores.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.cost);
        else
            lores.add(ChatColor.RED + "Nécessite " + ChatColor.AQUA + this.dependsOn.getIcon().getItemMeta().getDisplayName());

        meta.setLore(lores);
        stack.setItemMeta(meta);

        return stack;
    }

    public boolean hasDepend(Player player)
    {
        List<String> own = SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getOwnedLevels(player, this.type);
        return (own != null) && this.dependsOn != null && own.contains(this.dependsOn.getActionName());
    }
}
