package net.samagames.hub.games.shop;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopItem extends ShopIcon
{
    protected final AbstractGame game;
    protected final String type;
    protected final String[] description;
    protected final int cost;
    protected boolean defaultItem;

    public ShopItem(AbstractGame game, String type, String databaseName, String displayName, ItemStack icon, String[] description, int cost)
    {
        super(databaseName, displayName, icon);

        this.game = game;
        this.type = type;
        this.description = description;
        this.cost = cost;
    }

    @Override
    public void execute(Player player, ClickType clickType)
    {
        if(this.isActive(player))
        {
            player.sendMessage(ChatColor.RED + "Cet objet est déjà équipé.");
        }
        else if(this.isOwned(player))
        {
            SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).setCurrentLevel(player, this.type, this.getActionName());
            player.sendMessage(ChatColor.GREEN + "Vous avez équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
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
            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.cost);
            SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).addOwnedLevel(player, this.type, this.getActionName());
            SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).setCurrentLevel(player, this.type, this.getActionName());

            player.sendMessage(ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
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

        if(this.isActive(player))
            lores.add(ChatColor.GREEN + "Objet actif");
        else if(isDefaultItem() || isOwned(player))
            lores.add(ChatColor.GREEN + "Objet possédé");
        else
            lores.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.cost);

        meta.setLore(lores);
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
        List<String> own = SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getOwnedLevels(player, this.type);
        return (own != null) && own.contains(this.getActionName());
    }

    public boolean isActive(Player player)
    {
        String active = SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getItemLevelForPlayer(player, this.type);
        return (active == null && isDefaultItem()) || (active != null && active.equals(this.getActionName()));
    }
}
