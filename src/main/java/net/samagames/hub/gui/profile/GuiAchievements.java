package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.achievements.Achievement;
import net.samagames.api.achievements.AchievementCategory;
import net.samagames.api.achievements.IncrementationAchievement;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GuiAchievements extends AbstractGui
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
            Hub.getInstance().getGuiManager().openGui(player, new GuiAchievements(SamaGamesAPI.get().getAchievementManager().getAchievementCategoryByID(action.split("_")[1])));
        }
    }

    private void displayRoot(Player player)
    {
        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int lines = 0;
        int slot = 0;

        for(AchievementCategory category : SamaGamesAPI.get().getAchievementManager().getAchievementsCategories())
        {
            slot++;

            if(slot == 8)
            {
                slot = 0;
                lines++;
            }
        }

        this.inventory = Bukkit.createInventory(null, 9 + (lines * 9) + (9 * 2), "Objectifs");
        lines = 0;
        slot = 0;

        for(AchievementCategory category : SamaGamesAPI.get().getAchievementManager().getAchievementsCategories())
        {
            this.setSlotData(ChatColor.GOLD + category.getDisplayName(), category.getIcon(), (baseSlots[slot] + (lines * 9)), this.formatDescription(category.getDescription()), "category_" + category.getID());

            slot++;

            if (slot == 7)
            {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

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
                {
                    this.setSlotData(ChatColor.GREEN + achievement.getDisplayName(), Material.GOLDEN_APPLE, i, this.formatDescription(achievement.getDescription()), "none");
                }
                else
                {
                    if(achievement instanceof IncrementationAchievement)
                    {
                        this.setSlotData(ChatColor.RED + achievement.getDisplayName() + " [" + ((IncrementationAchievement) achievement).getActualState(player) + "/" + ((IncrementationAchievement) achievement).getObjective() + "]", Material.APPLE, i, this.formatDescription(achievement.getDescription()), "none");
                    }
                    else
                    {
                        this.setSlotData(ChatColor.RED + achievement.getDisplayName(), Material.APPLE, i, this.formatDescription(achievement.getDescription()), "none");
                    }
                }
            }
        }

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize(), "back-root");

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
