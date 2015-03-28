package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.achievements.Achievement;
import net.samagames.api.achievements.AchievementCategory;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.Gui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GuiAchievements extends Gui
{
    private final AchievementCategory category;

    public GuiAchievements(AchievementCategory category)
    {
        this.category = category;
    }

    public GuiAchievements()
    {
        this(null);
    }

    @Override
    public void display(Player player)
    {
        if(this.category != null)
            this.displayCategory(player);
        else
            this.displayRoot(player);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("back-root"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiAchievements());
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiProfile());
        }
        else if(action.startsWith("category_"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiAchievements(SamaGamesAPI.get().getAchievementManager().getAchievementCategoryByName(action.split("_")[1])));
        }
    }

    private void displayRoot(Player player)
    {
        int[] baseSlots = { 10, 11, 12, 13, 14, 15, 16 };
        int lines = 1;
        int i = 0;

        for(AchievementCategory category : SamaGamesAPI.get().getAchievementManager().getAchievementsCategories())
        {
            i++;

            if(i == 7)
            {
                i = 0;
                lines++;
            }
        }

        int line = 1;
        int slots = (9 * lines) + (9 * 3);
        this.inventory = Bukkit.createInventory(null, slots, "Objectifs");

        for(AchievementCategory category : SamaGamesAPI.get().getAchievementManager().getAchievementsCategories())
        {
            this.setSlotData(ChatColor.GOLD + category.getDisplayName(), category.getIcon(), baseSlots[i] + (9 * line), this.formatDescription(category.getDescription()), "category_" + category.getID());

            i++;
        }

        this.setSlotData(GuiUtils.getBackItem(), slots - 5, "back");

        player.openInventory(this.inventory);
    }

    private void displayCategory(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 54, "Objectifs - " + this.category.getDisplayName());

        for(int i = 0; i < SamaGamesAPI.get().getAchievementManager().getAchievements().size(); i++)
        {
            Achievement achievement = SamaGamesAPI.get().getAchievementManager().getAchievements().get(i);

            if(achievement.getParentCategoryID().equals(this.category.getID()))
            {
                if(SamaGamesAPI.get().getAchievementManager().isUnlocked(player, achievement))
                    this.setSlotData(ChatColor.GOLD + achievement.getDisplayName(), Material.DIAMOND, i, this.formatDescription(achievement.getDescription()), "none");
                else
                    this.setSlotData(ChatColor.RED + achievement.getDisplayName(), Material.COAL, i, this.formatDescription(achievement.getDescription()), "none");
            }
        }

        this.setSlotData(GuiUtils.getBackItem(), 50, "back-root");

        player.openInventory(this.inventory);
    }

    private String[] formatDescription(String[] base)
    {
        ArrayList<String> description = new ArrayList<>();

        for(String text : base)
            description.add(ChatColor.GRAY + text);

        String[] array = new String[] {};
        return description.toArray(array);
    }
}
