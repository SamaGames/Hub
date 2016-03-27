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

public class ShopItem extends ShopIcon
{
    protected final AbstractGame game;
    protected final String type;
    protected final String[] description;
    protected final int cost;
    protected boolean defaultItem;

    public ShopItem(Hub hub, AbstractGame game, String type, String databaseName, String displayName, ItemStack icon, int slot, String[] description, int cost)
    {
        super(hub, databaseName, displayName, icon, slot);

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
        else if(this.isOwned(player) || this.isDefaultItem())
        {
            SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).setCurrentLevel(player, this.type, this.getActionName());
            player.sendMessage(ChatColor.GREEN + "Vous avez équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
        }
        else if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.cost))
        {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cet objet.");
        }
        else
        {
            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                if(SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getItemLevelForPlayer(player, this.type).equals(this.getActionName()))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.cost, (newAmount, difference, error) ->
                {
                    SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).addOwnedLevel(player, this.type, this.getActionName());
                    SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).setCurrentLevel(player, this.type, this.getActionName());

                    player.sendMessage(ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

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
        List<String> own = SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getOwnedLevels(player, this.type);
        return (own != null) && own.contains(this.getActionName());
    }

    public boolean isActive(Player player)
    {
        String active = SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getItemLevelForPlayer(player, this.type);
        return (active == null && isDefaultItem()) || (active != null && active.equals(this.getActionName()));
    }
}
