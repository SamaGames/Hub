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

public class ShopImprovableItem extends ShopIcon
{
    private final AbstractGame game;
    private final String[] description;
    private final List<ItemLevel> levels;
    private ItemLevel defaultLevel;

    public ShopImprovableItem(Hub hub, AbstractGame game, String databaseName, String displayName, ItemStack icon, int slot, String[] description)
    {
        super(hub, databaseName, displayName, icon, slot);

        this.game = game;
        this.description = description;
        this.levels = new ArrayList<>();
        this.defaultLevel = null;
    }

    public void addLevel(int cost, String description, String databaseValue)
    {
        this.levels.add(new ItemLevel(cost, description, databaseValue));
    }

    public void addDefault(String description)
    {
        this.defaultLevel = new ItemLevel(0, description, null);
    }

    public void execute(Player player, ClickType clickType)
    {
        String currentItemName = this.getCurrentValue(player);
        ItemLevel next = this.levels.get(0);

        if (currentItemName != null)
            next = this.getNextItem(currentItemName);

        if (next == null)
        {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous avez déjà débloqué le niveau maximum de cette amélioration.");
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
                if(SamaGamesAPI.get().getShopsManager().getItemLevelForPlayer(player.getUniqueId(), this.getActionName()).equals(finalLevel.getDatabaseStorageName()))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(finalLevel.getCost(), (newAmount, difference, error) ->
                {
                    SamaGamesAPI.get().getShopsManager().addOwnedLevel(player.getUniqueId(), this.getActionName(), finalLevel.getDatabaseStorageName());
                    SamaGamesAPI.get().getShopsManager().setCurrentLevel(player.getUniqueId(), this.getActionName(), finalLevel.getDatabaseStorageName());

                    player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez débloqué le niveau supérieur pour cette amélioration.");

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
        String currentItemName = this.getCurrentValue(player);
        int currentLevel = -1;
        ItemLevel next;

        if (currentItemName == null)
        {
            next = this.levels.get(0);
        }
        else
        {
            currentLevel = this.getLevel(currentItemName);

            if (currentLevel == 0)
                next = this.levels.get(1);
            else
                next = this.getNextItem(currentItemName);
        }

        List<String> lore = new ArrayList<>();

        for(String str : this.description)
            lore.add(ChatColor.GRAY + str);

        lore.add("");

        if(this.defaultLevel != null)
        {
            lore.add(ChatColor.GREEN + "Par défaut : " + this.defaultLevel.getDescription());
            lore.add("");
        }

        for(int i = 0; i < this.levels.size(); i++)
        {
            ItemLevel level = this.levels.get(i);
            String prefix = "";

            if(currentLevel >= i)
                prefix += ChatColor.GREEN;
            else if(i == (currentLevel + 1))
                prefix += ChatColor.YELLOW + "" + ChatColor.BOLD;
            else
                prefix += ChatColor.RED;

            lore.add(prefix + "Niveau " + (i + 1) + " : " + level.getDescription());
        }

        lore.add("");

        if(next == null)
            lore.add(ChatColor.GREEN + "Améliorations débloquées");
        else
            lore.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + next.getCost() + " pièces");

        ItemStack icon = this.getIcon();
        ItemMeta meta = icon.getItemMeta();
        meta.setLore(lore);
        icon.setItemMeta(meta);

        return icon;
    }

    public String getCurrentValue(Player player)
    {
        return SamaGamesAPI.get().getShopsManager().getItemLevelForPlayer(player.getUniqueId(), this.getActionName());
    }

    public ItemLevel getItemLevel(int level)
    {
        if (level == -1)
            return null;
        else if (level >= this.levels.size())
            return null;
        else
            return this.levels.get(level);
    }

    public int getLevel(String databaseValue)
    {
        int level = 0;

        for (ItemLevel itemLevel : this.levels)
        {
            if (itemLevel.getDatabaseStorageName().equals(databaseValue))
                return level;

            level++;
        }

        return level;
    }

    public ItemLevel getNextItem(String databaseValue)
    {
        int level = this.getLevel(databaseValue);
        level++;

        if (level >= this.levels.size())
            return null;

        return this.levels.get(level);
    }
}
