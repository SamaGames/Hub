package net.samagames.hub.games.shop;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.games.IGame;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ShopItem
{
    private final IGame game;
    private final String databaseName;
    private final String displayName;
    private final String[] description;
    private final ItemStack icon;
    private final Object[][] levels;

    public ShopItem(IGame game, String databaseName, String displayName, String[] description, ItemStack icon)
    {
        this.game = game;
        this.databaseName = databaseName;
        this.displayName = displayName;
        this.description = description;
        this.icon = icon;
        this.levels = new Object[][] {};
    }

    public void addLevel(int cost, String[] description, String value)
    {
        int level = this.levels.length + 1;

        this.levels[level] = new Object[] {
                cost,
                description,
                value
        };
    }

    public boolean buy(Player player, int level)
    {
        if(!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins((Integer) this.levels[level][0]))
            return false;

        SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins((Integer) this.levels[level][0]);
        SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).addOwnedItem(player, this.databaseName, String.valueOf(this.levels[level][2]));
        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);

        return true;
    }

    public String getDatabaseName()
    {
        return this.databaseName;
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public ItemStack getFormattedIcon(Player player)
    {
        String actualValue = SamaGamesAPI.get().getShopsManager(this.game.getCodeName()).getCurrentItemForPlayer(player, this.databaseName);

        int actualLevelCase = -1;

        ArrayList<String> finalDescription = new ArrayList<>();

        for(String string : this.description)
            finalDescription.add(ChatColor.GRAY + string);

        finalDescription.add("");

        if(actualValue != null)
            actualLevelCase = this.getLevelCaseByValue(actualValue);

        for(int i = 0; i < this.levels.length; i++)
        {
            Object[] level = this.levels[i];

            String prefix = "";

            if(actualValue != null)
            {
                if(actualLevelCase >= this.getLevelCaseByValue(String.valueOf(level[2])))
                    prefix += ChatColor.GREEN;
                else if(this.getLevelCaseByValue(String.valueOf(level[2])) == (actualLevelCase + 1))
                    prefix += ChatColor.YELLOW + "" + ChatColor.BOLD;
                else
                    prefix += ChatColor.RED;
            }

            finalDescription.add(prefix + i + ": " + level[1]);
        }

        finalDescription.add("");
        finalDescription.add(ChatColor.GOLD + "Prix: " + String.valueOf(this.levels[(actualLevelCase + 1)][0]));

        ItemStack icon = this.icon;
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + this.displayName);
        meta.setLore(finalDescription);
        icon.setItemMeta(meta);

        return icon;
    }

    public Object[][] getLevels()
    {
        return this.levels;
    }

    public Object[] getLevelByValue(String value)
    {
        for(Object[] level : this.levels)
            if(String.valueOf(level[2]).equals(value))
                return level;

        return null;
    }

    public int getLevelCaseByValue(String value)
    {
        for(int i = 0; i < this.levels.length; i++)
        {
            Object[] level = this.levels[i];

            if(String.valueOf(level[2]).equals(value))
                return i;
        }

        return -1;
    }
}
