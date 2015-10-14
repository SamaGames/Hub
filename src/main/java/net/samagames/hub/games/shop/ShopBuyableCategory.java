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

public class ShopBuyableCategory extends ShopCategory
{
    private final int cost;

    public ShopBuyableCategory(AbstractGame game, String actionName, String displayName, ItemStack icon, int slot, String[] description, int cost)
    {
        super(game, actionName, displayName, icon, slot, description);

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
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cet objet.");
        }
        else if(clickType == ClickType.LEFT)
        {
            player.sendMessage(ChatColor.GOLD + "Faites un clic droit pour valider votre achat.");
        }
        else
        {
            GuiConfirm confirm = new GuiConfirm(Hub.getInstance().getGuiManager().getPlayerGui(player), () ->
            {
                //check if multilple click
                if(SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getItemLevelForPlayer(player, this.getActionName()) == "flag")
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.cost, (newAmount, difference, error) -> {
                    SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).addOwnedLevel(player, this.getActionName(), "flag");
                    SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).setCurrentLevel(player, this.getActionName(), "flag");
                    Hub.getInstance().getScoreboardManager().update(player.getUniqueId(), true);
                    player.sendMessage(ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
                });
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

        lores.add(ChatColor.GRAY + "");

        if(isOwned(player))
            lores.add(ChatColor.GREEN + "Objet possédé");
        else
            lores.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.cost + " pièces");

        meta.setLore(lores);
        stack.setItemMeta(meta);

        return stack;
    }

    public boolean isOwned(Player player)
    {
        if(this.cost == 0)
            return true;

        List<String> own = SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getOwnedLevels(player, this.getActionName());
        return (own != null) && own.contains("flag");
    }
}
